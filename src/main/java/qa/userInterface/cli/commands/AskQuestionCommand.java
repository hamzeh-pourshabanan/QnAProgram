package qa.userInterface.cli.commands;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.InputPort;

import java.util.List;
import java.util.Scanner;

public class AskQuestionCommand implements Command {
    @Override
    public void execute(Scanner scanner,  InputPort service) {
        System.out.print("Enter your question: ");
        String question = scanner.nextLine().trim();
        try {
            if (question.endsWith("?")){
                question = question.substring(0, question.length() - 1);
            }
            List<Answer> answers = service.askQuestion(new Question(question));
            answers.forEach(a -> System.out.println(a.text()));

        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }

    @Override
    public String getName() {
        return "Ask a question";
    }
}
