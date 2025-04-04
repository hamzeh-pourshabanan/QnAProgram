import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class AddQuestionCommand implements Command{
    @Override
    public void execute(Scanner scanner, Map<String, List<String>> knowledgeBase) {
        System.out.println("Enter question and answers in format: <question>? \"<answer1>\" \"<answer2>\" ...");
        System.out.print("Input: ");
        String input = scanner.nextLine().trim();

        if (!input.contains("?")) {
            System.out.println("Invalid format. Missing '?' separator.");
            return;
        }

        int questionMarkIndex = input.indexOf("?");
        String question = input.substring(0, questionMarkIndex).trim();
        String answersPart = input.substring(questionMarkIndex + 1).trim();

        if (exceedsMaxLength(question)) return;

        if (question.isEmpty()) {
            System.out.println("Question cannot be empty.");
            return;
        }

        ArrayList<String> answers = new ArrayList<>();
        try {
            answers = extractAnswers(answersPart);
        } catch (IllegalArgumentException exception) {
            System.out.println(exception.getMessage());
        }


        if (answers.isEmpty()) {
            System.out.println("At least one answer is required.");
            return;
        }

        knowledgeBase.put(question, answers);
        System.out.println("Question and answers added successfully!");
    }

    private ArrayList<String> extractAnswers(String answersPart) throws IllegalArgumentException {
        ArrayList<String> answers = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder currentAnswer = new StringBuilder();

        for (char c : answersPart.toCharArray()) {
            if (c == '"') {
                if (inQuote) {
                    String answer = currentAnswer.toString().trim();
                    if (!answer.isEmpty()) {
                        if (answer.length() > Constants.MAX_LENGTH) {
                            throw new IllegalArgumentException(
                                    String.format("Answer exceeds maximum length of %d characters", Constants.MAX_LENGTH)
                            );
                        }
                        answers.add(answer);
                    }
                    currentAnswer = new StringBuilder();
                }
                inQuote = !inQuote;
            } else if (inQuote) {
                currentAnswer.append(c);
            }
        }
        return answers;
    }

    @Override
    public String getName() {
        return "Add a question";
    }
}
