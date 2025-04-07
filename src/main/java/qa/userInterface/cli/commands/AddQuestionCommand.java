package qa.userInterface.cli.commands;

import qa.common.exception.InvalidInputException;
import qa.domain.ports.InputPort;
import qa.userInterface.cli.parser.ParsedInput;
import qa.userInterface.cli.parser.QuestionParser;

import java.util.Scanner;

public class AddQuestionCommand implements Command {
    @Override
    public void execute(Scanner scanner, InputPort service) {

        showFormatExample();
        String input = scanner.nextLine().trim();
        try {
            ParsedInput parsed = new QuestionParser().parse(input);
            service.addQuestion(parsed.question(), parsed.answers());
            System.out.println("Question and answers added successfully!");
        } catch (InvalidInputException e) {
            System.out.println("Invalid format: " + e.getMessage());
            System.out.println("Expected: <question>? \"answer1\" \"answer2\"");
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }


    private void showFormatExample() {
        System.out.println("format: <question>? \"<answer1>\" \"<answer2>\" ...");
        System.out.println("Enter you question and answer:");
    }

    @Override
    public String getName() {
        return "Add a question";
    }
}
