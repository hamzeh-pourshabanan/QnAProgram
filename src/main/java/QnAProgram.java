import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class QnAProgram {
    private static final Map<String, List<String>> knowledgeBase = new HashMap<>();
    public static void main(String[] args) {
        //TODO: Register default commands ...
        // Ask question
        // Add a question and answer

        Scanner scanner = new Scanner(System.in);

        while(true) {
            //TODO: Display menu ...
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            //TODO: Execute command ...
        }
    }
}
