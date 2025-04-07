import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ExitCommand implements Command{
    @Override
    public void execute(Scanner scanner, Map<String, List<String>> knowledgeBase) {
        System.out.println("Goodbye!");
        System.exit(0);
    }

    @Override
    public String getName() {
        return "Exit";
    }
}
