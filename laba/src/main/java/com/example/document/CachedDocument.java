package com.example.document;

import lombok.SneakyThrows;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class CachedDocument implements Document {
    private final Document document;
    private final String dbPath;

    public CachedDocument(Document document) {
        this.document = document;
        this.dbPath = initializeDatabasePath();
        initializeDatabase();
    }

    private String initializeDatabasePath() {
        return Paths.get(System.getProperty("user.dir"), "cache.db")
                    .toString();
    }

    @SneakyThrows
    private void initializeDatabase() {
        try (Connection CONN = DriverManager.getConnection(
                "jdbc:sqlite:" + dbPath)) {
            String createTableQuery = "CREATE TABLE IF NOT EXISTS cache ("
                                    + "file_path TEXT PRIMARY KEY, "
                                    + "parsed_text TEXT);";
            try (Statement STMT = CONN.createStatement()) {
                STMT.execute(createTableQuery);
            }
        }
    }

    @SneakyThrows
    private String getCachedText(String filePath) {
        String query = "SELECT parsed_text FROM cache WHERE file_path = ?";
        try (Connection CONN = DriverManager.getConnection(
                "jdbc:sqlite:" + dbPath);
             PreparedStatement PSTMT = CONN.prepareStatement(query)) {
            PSTMT.setString(1, filePath);
            ResultSet rs = PSTMT.executeQuery();
            if (rs.next()) {
                return rs.getString("parsed_text");
            }
        }
        return null;
    }

    @SneakyThrows
    private void cacheText(String filePath, String text) {
        String insertQuery = "INSERT OR REPLACE INTO cache (file_path, "
                           + "parsed_text) VALUES (?, ?)";
        try (Connection CONN = DriverManager.getConnection(
                "jdbc:sqlite:" + dbPath);
             PreparedStatement PSTMT = CONN.prepareStatement(insertQuery)) {
            PSTMT.setString(1, filePath);
            PSTMT.setString(2, text);
            PSTMT.executeUpdate();
        }
    }

    @SneakyThrows
    public void removeFromCache(String filePath) {
        String deleteQuery = "DELETE FROM cache WHERE file_path = ?";
        try (Connection CONN = DriverManager.getConnection(
                "jdbc:sqlite:" + dbPath);
             PreparedStatement PSTMT = CONN.prepareStatement(deleteQuery)) {
            PSTMT.setString(1, filePath);
            PSTMT.executeUpdate();
            System.out.println("Removed from cache: " + filePath);
        }
    }

    @SneakyThrows
    public void clearCache() {
        String deleteAllQuery = "DELETE FROM cache";
        try (Connection CONN = DriverManager.getConnection(
                "jdbc:sqlite:" + dbPath);
             PreparedStatement PSTMT = CONN.prepareStatement(deleteAllQuery)) {
            PSTMT.executeUpdate();
            System.out.println("All cache cleared");
        }
    }

    @Override
    public String parse() {
        String filePath = ((SmartDocument) document).getFilePath();
        String cachedText = getCachedText(filePath);
        if (cachedText != null) {
            System.out.println("Using cached result for: " + filePath);
            return cachedText;
        }

        String parsedText = document.parse();
        cacheText(filePath, parsedText);
        return parsedText;
    }
}
