import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QnAProgram {
    private static final Map<String, List<String>> knowledgeBase = new HashMap<>();
    private static final CommandRegistry registry = new CommandRegistry();
    public static void main(String[] args) {
        registry.registerCommand(new AskQuestionCommand());
        registry.registerCommand(new AddQuestionCommand());
        registry.registerCommand(new ExitCommand());

        Scanner scanner = new Scanner(System.in);

        while(true) {
            registry.displayMenu();
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();
            registry.executeCommand(choice, scanner, knowledgeBase);
        }
    }
}
