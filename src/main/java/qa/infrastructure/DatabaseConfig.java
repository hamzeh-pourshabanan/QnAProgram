package qa.infrastructure;

/**
 * Database connection configuration.
 * <p>
 * Example configurations:
 * <pre>
 * new DatabaseConfig("jdbc:sqlite:qa.db")  // File-based
 * </pre>
 */
public record DatabaseConfig(String url) {

    public static DatabaseConfig fileBased(String path) {
        return new DatabaseConfig("jdbc:sqlite:" + path);
    }

    public static DatabaseConfig inMemory() {
        return new DatabaseConfig("jdbc:sqlite::memory:");
    }
}
