package com.accounting.util;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CsvUtil {

    public static void ensureFileExists(String filePath, String[] headers) throws IOException {
        Path path = Paths.get(filePath);
        if (!Files.exists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        if (!Files.exists(path)) {
            try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                    new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
                writer.writeNext(headers);
            }
        }
    }

    public static List<String[]> readAll(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            String[] line;
            boolean isFirst = true;
            while ((line = reader.readNext()) != null) {
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                records.add(line);
            }
        } catch (CsvValidationException e) {
            throw new IOException("CSV validation error", e);
        }
        return records;
    }

    public static void writeAll(String filePath, List<String[]> records) throws IOException {
        String[] headers = getHeaders(filePath);
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(filePath), StandardCharsets.UTF_8))) {
            writer.writeNext(headers);
            for (String[] record : records) {
                writer.writeNext(record);
            }
        }
    }

    public static void appendRecord(String filePath, String[] record) throws IOException {
        try (CSVWriter writer = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(filePath, true), StandardCharsets.UTF_8))) {
            writer.writeNext(record);
        }
    }

    public static String[] getHeaders(String filePath) throws IOException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(
                new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            return reader.readNext();
        } catch (CsvValidationException e) {
            throw new IOException("CSV validation error", e);
        }
    }

    public static <T> List<T> readAndConvert(String filePath, Function<String[], T> converter) throws IOException {
        List<String[]> records = readAll(filePath);
        List<T> result = new ArrayList<>();
        for (String[] record : records) {
            if (record.length > 0 && record[0] != null && !record[0].isEmpty()) {
                String[] processedRecord = processRecord(record);
                T obj = converter.apply(processedRecord);
                if (obj != null) {
                    result.add(obj);
                }
            }
        }
        return result;
    }

    private static String[] processRecord(String[] record) {
        String[] processed = new String[record.length];
        for (int i = 0; i < record.length; i++) {
            if (i == 2 && record[i] != null && !record[i].isEmpty()) {
                try {
                    double amount = Double.parseDouble(record[i]);
                    processed[i] = String.valueOf(Math.round(amount));
                } catch (NumberFormatException e) {
                    processed[i] = record[i];
                }
            } else {
                processed[i] = record[i];
            }
        }
        return processed;
    }
}
