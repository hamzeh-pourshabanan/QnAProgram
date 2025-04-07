package qa.infrastructure.adapters;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.QuestionRepository;
import qa.infrastructure.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Database persistence adapter using SQLite.
 * <p>
 * Schema:
 * <pre>
 * questions(id, question_text UNIQUE)
 * answers(id, question_id, answer_text)
 * </pre>
 */
public class SQLiteQuestionRepository implements QuestionRepository {
    private final Connection connection;

    /**
     * @param config Database configuration including connection URL
     * @throws SQLException If connection fails
     */
    public SQLiteQuestionRepository(DatabaseConfig config) throws SQLException {
        this.connection = DriverManager.getConnection(config.url());
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS questions (
                    id INTEGER PRIMARY KEY,
                    question_text TEXT UNIQUE NOT NULL
                )
                """);

            stmt.execute("""
                CREATE TABLE IF NOT EXISTS answers (
                    id INTEGER PRIMARY KEY,
                    question_id INTEGER NOT NULL,
                    answer_text TEXT NOT NULL,
                    FOREIGN KEY (question_id) REFERENCES questions(id)
                )
                """);
        }
    }

    @Override
    public void save(Question question, List<Answer> answers) {
        try {
            connection.setAutoCommit(false);

            // 1. Insert question (ignore if exists)
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT OR IGNORE INTO questions (question_text) VALUES (?)")) {
                ps.setString(1, question.text());
                ps.executeUpdate();
            }

            // 2. Get question ID
            int questionId;
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT id FROM questions WHERE question_text = ?")) {
                ps.setString(1, question.text());
                ResultSet rs = ps.executeQuery();
                questionId = rs.getInt("id");
            }

            // 3. Insert answers
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO answers (question_id, answer_text) VALUES (?, ?)")) {
                for (Answer answer : answers) {
                    ps.setInt(1, questionId);
                    ps.setString(2, answer.text());
                    ps.addBatch();
                }
                ps.executeBatch();
            }

            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Database operation failed", e);
        }
    }

    @Override
    public List<Answer> findByQuestion(Question question) {
        final String sql = """
                SELECT a.answer_text
                        FROM answers a
                        JOIN questions q ON a.question_id = q.id
                        WHERE q.question_text = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, question.text());

            ResultSet rs = ps.executeQuery();
            List<Answer> answers = new ArrayList<>();

            while (rs.next()) {
                answers.add(new Answer(rs.getString("answer_text")));
            }

            return answers;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find answers for question: " + question.text(), e);
        }
    }

    @Override
    public boolean exists(Question question) {
        final String sql = "SELECT 1 FROM questions WHERE question_text = ? LIMIT 1";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, question.text());

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check question existence: " + question.text(), e);
        }
    }
}