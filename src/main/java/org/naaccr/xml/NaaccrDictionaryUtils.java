/*
 * Copyright (C) 2015 Information Management Services, Inc.
 */
package org.naaccr.xml;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import org.naaccr.xml.entity.dictionary.NaaccrDictionary;
import org.naaccr.xml.entity.dictionary.NaaccrDictionaryItem;

import au.com.bytecode.opencsv.CSVReader;

public class NaaccrDictionaryUtils {

    public static final String NAACCR_DICTIONARY_FORMAT_CSV = "csv";

    public static NaaccrDictionary readDictionary(File file) throws IOException {
        if (file == null)
            throw new IOException("File is required");
        if (!file.exists())
            throw new IOException("File does not exist");

        NaaccrDictionary dictionary;

        if (file.getName().endsWith(".csv"))
            dictionary = readDictionaryFromCsv(new FileReader(file));
        else
            throw new IOException("Unsupported format");

        return dictionary;
    }

    public static NaaccrDictionary readDictionary(URL url, String format) throws IOException {
        if (url == null)
            throw new IOException("URL is required");

        NaaccrDictionary dictionary;

        switch (format) {
            case NAACCR_DICTIONARY_FORMAT_CSV:
                dictionary = readDictionaryFromCsv(new InputStreamReader(url.openStream()));
                break;
            default:
                throw new IOException("Unsupported format: " + format);

        }

        return dictionary;
    }

    private static NaaccrDictionary readDictionaryFromCsv(Reader reader) throws IOException {
        NaaccrDictionary dictionary = new NaaccrDictionary();

        // always skip first line
        try (CSVReader csvReader = new CSVReader(reader, ',', '"', '\\', 1, false)) {
            for (String[] line : csvReader.readAll()) {
                if (line.length < 3 || line.length > 15)
                    throw new IOException("Wrong number of fields, expected between 3 and 15, got " + line.length + " (Item ID " + line[0] + ")");

                // TODO add validation, trim values, be safer since it could be a user-defined dictionary...
                NaaccrDictionaryItem item = new NaaccrDictionaryItem();
                item.setNaaccrId(line[0]);
                if (line[1] != null && !line[1].isEmpty())
                    item.setNaaccrNum(Integer.valueOf(line[1]));
                item.setLength(Integer.valueOf(line[2]));
                if (line.length > 3 && line[3] != null && !line[3].isEmpty())
                    item.setNaaccrName(line[3]);
                if (line.length > 4 && line[4] != null && !line[4].isEmpty())
                    item.setIsGroup("yes".equals(line[4].toLowerCase()) || "true".equals(line[4].toLowerCase()));
                if (line.length > 5 && line[5] != null && !line[5].isEmpty())
                    item.setGroupNaaccrId(line[5]);
                if (line.length > 6 && line[6] != null && !line[6].isEmpty())
                    item.setRecordTypes(line[6]);
                if (line.length > 7 && line[7] != null && !line[7].isEmpty())
                    item.setStartColumn(Integer.valueOf(line[7]));
                if (line.length > 8 && line[8] != null && !line[8].isEmpty())
                    item.setParentXmlElement(line[8]);
                if (line.length > 9 && line[9] != null && !line[9].isEmpty())
                    item.setRegexValidation(line[9]);
                if (line.length > 10 && line[10] != null && !line[10].isEmpty())
                    item.setDataType(line[10]);
                if (line.length > 11 && line[11] != null && !line[11].isEmpty())
                    item.setSection(line[11]);
                if (line.length > 12 && line[12] != null && !line[12].isEmpty())
                    item.setSourceOfStandard(line[12]);
                if (line.length > 13 && line[13] != null && !line[13].isEmpty())
                    item.setRetiredVersion(line[13]);
                if (line.length > 14 && line[14] != null && !line[14].isEmpty())
                    item.setImplementedVersion(line[14]);
                
                if (item.getRecordTypes() == null)
                    item.setRecordTypes("A,M,C,I");

                dictionary.getItems().add(item);
            }
        }

        return dictionary;
    }
    
    

    public static void main(String[] args) throws IOException {
        NaaccrDictionary dictionary = readDictionary(Thread.currentThread().getContextClassLoader().getResource("fabian/naaccr-dictionary-140.csv"), NAACCR_DICTIONARY_FORMAT_CSV);
        System.out.println("Read " + dictionary.getItems().size() + " items...");
    }
}
