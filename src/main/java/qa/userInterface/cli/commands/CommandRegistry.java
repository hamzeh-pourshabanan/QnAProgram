package qa.userInterface.cli.commands;

import qa.domain.ports.InputPort;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CommandRegistry {
    private final List<Command> commands = new ArrayList<>();

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void executeCommand(int choice, Scanner scanner, InputPort service) {
        if (choice >= 1 && choice <= commands.size()) {
            commands.get(choice - 1).execute(scanner, service);
        } else {
            System.out.println("Invalid choice.");
        }
    }

    public void displayMenu() {
        System.out.println("\nOptions:");
        for (int i = 0; i < commands.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, commands.get(i).getName());
        }
    }
}
