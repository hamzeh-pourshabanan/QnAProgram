import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AskQuestionCommand implements Command{
    @Override
    public void execute(Scanner scanner, Map<String, List<String>> knowledgeBase) {
        System.out.print("Enter your question: ");
        String question = scanner.nextLine().trim();
        if (question.endsWith("?")){
            question = question.substring(0, question.length() - 1);;
        }
        if (exceedsMaxLength(question)) return;
        if (knowledgeBase.containsKey(question)) {
            knowledgeBase.get(question).forEach(System.out::println);
        } else {
            System.out.println(Constants.UNIVERSE_RESPONSE);
        }
    }

    @Override
    public String getName() {
        return "Ask a question";
    }
}
