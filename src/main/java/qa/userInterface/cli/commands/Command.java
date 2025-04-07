import java.util.List;
import java.util.Map;
import java.util.Scanner;

interface Command {
    void execute(Scanner scanner, Map<String, List<String>> knowledgeBase);
    String getName();
    default boolean exceedsMaxLength(String question) {
        if (question.length() > Constants.MAX_LENGTH) {
            System.out.printf("Question exceeds maximum length of %d characters.\n", Constants.MAX_LENGTH);
            return true;
        }
        return false;
    }
}
