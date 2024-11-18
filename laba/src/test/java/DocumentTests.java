import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import com.example.document.SmartDocument;
import com.example.document.CachedDocument;
import com.example.document.TimedDocument;

public class DocumentTests {

    private static final String TEST_FILE_PATH = "fotos/foto1.png";
    private static final String NON_EXISTENT_FILE_PATH =
        "fotos/non_existent.png";
    private static final String DB_PATH = "cache.db";

    private SmartDocument smartDocument;
    private CachedDocument cachedDocument;
    private TimedDocument timedDocument;

    @BeforeEach
    public void setUp() {
        smartDocument = new SmartDocument(TEST_FILE_PATH);
        cachedDocument = new CachedDocument(smartDocument);
        timedDocument = new TimedDocument(smartDocument);
    }

    @AfterEach
    public void tearDown() {
        File dbFile = new File(DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }
    }

    @Test
    public void testSmartDocumentParsing() {
        String text = smartDocument.parse();
        Assertions.assertNotNull(
            text, "SmartDocument should return non-null text"
        );
        Assertions.assertFalse(
            text.isEmpty(), "SmartDocument should return non-empty text"
        );
    }

    @Test
    public void testSmartDocumentWithNonExistentFile() {
        SmartDocument nonExistentDoc = new SmartDocument(
            NON_EXISTENT_FILE_PATH
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            nonExistentDoc::parse,
            "Should throw an exception for non-existent file"
        );
    }

    @Test
    public void testCachedDocument() {
        String firstParse = cachedDocument.parse();
        Assertions.assertNotNull(
            firstParse, "CachedDocument should return non-null text"
        );

        String secondParse = cachedDocument.parse();
        Assertions.assertEquals(
            firstParse, secondParse,
            "CachedDocument should return cached text on second parse"
        );

        cachedDocument.removeFromCache(TEST_FILE_PATH);
    }

    @Test
    public void testCacheClear() {
        cachedDocument.parse();
        cachedDocument.clearCache();

        String cachedText = cachedDocument.parse();
        Assertions.assertNotNull(
            cachedText,
            "Cache should be cleared, so parse should re-extract text"
        );
    }

    @Test
    public void testDatabaseFileCreation() {
        File dbFile = new File(DB_PATH);
        cachedDocument.parse();

        Assertions.assertTrue(
            dbFile.exists(),
            "Database file should be created in the working directory"
        );
    }

    @Test
    public void testCachePersistenceAcrossInstances() {
        cachedDocument.parse();

        CachedDocument newCachedDoc = new CachedDocument(smartDocument);
        String cachedText = newCachedDoc.parse();
        Assertions.assertNotNull(
            cachedText, "Cached text should persist across instances"
        );
    }

    @Test
    public void testTimedDocument() {
        String text = timedDocument.parse();
        Assertions.assertNotNull(
            text, "TimedDocument should return non-null text"
        );
        Assertions.assertFalse(
            text.isEmpty(), "TimedDocument should return non-empty text"
        );
    }

    @Test
    public void testConfigPropertiesMissing() {
        SmartDocument faultyDoc = new SmartDocument(
            "missing_config.properties"
        );
        Assertions.assertThrows(
            IllegalArgumentException.class,
            faultyDoc::parse,
            "Should throw an exception if config.properties is missing"
        );
    }
}
