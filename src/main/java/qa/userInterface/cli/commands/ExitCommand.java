package qa.userInterface.cli.commands;

import qa.domain.ports.InputPort;

import java.util.Scanner;

public class ExitCommand implements Command {
    @Override
    public void execute(Scanner scanner, InputPort service) {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    @Override
    public String getName() {
        return "Exit";
    }
}
