package qa.userInterface.cli.adapter;

import qa.domain.ports.InputPort;
import qa.userInterface.cli.commands.AddQuestionCommand;
import qa.userInterface.cli.commands.AskQuestionCommand;
import qa.userInterface.cli.commands.CommandRegistry;
import qa.userInterface.cli.commands.ExitCommand;

import java.util.Scanner;

public class ConsoleInputAdapter {
    private final InputPort service;
    private final Scanner scanner;

    private final CommandRegistry registry;

    public ConsoleInputAdapter(InputPort service, Scanner scanner) {
        this.service = service;
        this.scanner = scanner;
        this.registry = new CommandRegistry();
    }

    /**
     * Initiates the question-answer interaction loop.
     * @implSpec Should block until user exits
     */
    public void startInteraction() {
        registry.registerCommand(new AskQuestionCommand());
        registry.registerCommand(new AddQuestionCommand());
        registry.registerCommand(new ExitCommand());

        while (true) {
            registry.displayMenu();

            if (scanner.hasNextInt()) {
                int choice = scanner.nextInt();
                scanner.nextLine();
                registry.executeCommand(choice, scanner, service);
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scanner.next();
            }
        }
    }
}
