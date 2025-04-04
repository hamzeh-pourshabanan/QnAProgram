import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface Command {
    void execute(Scanner scanner, Map<String, List<String>> knowledgeBase);
    String getName();

}
