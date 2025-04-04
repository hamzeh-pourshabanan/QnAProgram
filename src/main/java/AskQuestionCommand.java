import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AskQuestionCommand implements Command{
    @Override
    public void execute(Scanner scanner, Map<String, List<String>> knowledgeBase) {
        System.out.print("Enter your question: ");
        String question = scanner.nextLine().trim();

        if (knowledgeBase.containsKey(question)) {
            knowledgeBase.get(question).forEach(System.out::println);
        } else {
            System.out.println("The answer to life, universe and everything is 42");
        }
    }

    @Override
    public String getName() {
        return "Ask a question";
    }
}
