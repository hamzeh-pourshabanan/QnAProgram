import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface Command {
    void execute(Scanner scanner, Map<String, List<String>> knowledgeBase);
    String getName();
    default boolean exceedsMaxLength(String question) {
        if (question.length() > Constants.MAX_LENGTH) {
            System.out.println("Question exceeds maximum length of 255 characters.");
            return true;
        }
        return false;
    }
}
