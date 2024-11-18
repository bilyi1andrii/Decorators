package com.example.document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.SneakyThrows;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

@AllArgsConstructor
@Getter
public class SmartDocument implements Document {
    private final String filePath;

    @SneakyThrows
    private Properties loadProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass()
        .getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalArgumentException("config.properties"
                + " file not found in resources");
            }
            properties.load(input);
        }
        return properties;
    }

    @SneakyThrows
    @Override
    public String parse() {
        Properties properties = loadProperties();

        String jnaLibraryPath = properties.getProperty("jna.library.path");
        String tessDataPath = properties.getProperty("tessdata.path");
        String tessLanguage = properties.getProperty("tessdata.language");

        if (jnaLibraryPath != null) {
            System.setProperty("jna.library.path", jnaLibraryPath);
        }

        URL resource = getClass().getClassLoader().getResource(filePath);

        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }

        File file = new File(resource.toURI());

        ITesseract tesseract = new Tesseract();

        tesseract.setDatapath(tessDataPath);
        tesseract.setLanguage(tessLanguage);

        try {
            return tesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "";
        }
    }
}
