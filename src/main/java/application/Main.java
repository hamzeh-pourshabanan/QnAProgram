package application;

import qa.domain.ports.InputPort;
import qa.domain.ports.QuestionRepository;
import qa.domain.service.QAService;
import qa.infrastructure.DatabaseConfig;
import qa.infrastructure.adapters.CachedQuestionRepository;
import qa.infrastructure.adapters.SQLiteQuestionRepository;
import qa.userInterface.cli.adapter.ConsoleInputAdapter;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
// Initialize dependencies
        DatabaseConfig config = DatabaseConfig.fileBased("qa.db");
        QuestionRepository dbRepo = null;
        try {
            dbRepo = new SQLiteQuestionRepository(config);
        } catch (SQLException e) {
            System.out.println("Database access error: " + e);
        }
        QuestionRepository cachedRepo = new CachedQuestionRepository(dbRepo);
        InputPort service = new QAService(cachedRepo);
        Scanner scanner = new Scanner(System.in);

        // Start CLI
        ConsoleInputAdapter cli = new ConsoleInputAdapter(
                service, scanner
        );
        cli.startInteraction();
    }
}
