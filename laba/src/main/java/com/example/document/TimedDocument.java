package com.example.document;

public class TimedDocument implements Document {
    private final Document document;


    public TimedDocument(Document document) {
        this.document = document;
    }

    @Override
    public String parse() {
        long startTime = System.currentTimeMillis();

        String result = document.parse();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        System.out.println("Time taken to parse: " + duration + " ms");

        return result;
    }
}
