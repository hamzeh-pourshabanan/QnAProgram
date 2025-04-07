import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class CommandRegistry {
    private final List<Command> commands = new ArrayList<>();

    public void registerCommand(Command command) {
        commands.add(command);
    }

    public void executeCommand(int choice, Scanner scanner, Map<String, List<String>> knowledgeBase) {
        if (choice >= 1 && choice <= commands.size()) {
            commands.get(choice - 1).execute(scanner, knowledgeBase);
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
