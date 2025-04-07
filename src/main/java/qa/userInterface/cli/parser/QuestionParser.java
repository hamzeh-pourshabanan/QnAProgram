package qa.userInterface.cli.parser;

import qa.common.Constants;
import qa.common.exception.InvalidInputException;
import qa.domain.model.Answer;
import qa.domain.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles parsing of user input into domain objects.
 * <p>
 * Expected input format: {@code <question>? "answer1" "answer2"...}
 *
 * @see Question
 * @see Answer
 */
public class QuestionParser {

    /**
     * Parses raw user input into structured data
     * @param input User input following format "Q? "A1" "A2""
     * @return ParsedInput containing question and answers
     * @throws InvalidInputException On format violations (missing quotes, etc.)
     */
    public ParsedInput parse(String input) throws InvalidInputException {
        String[] parts = input.split("\\?", 2);
        if (parts.length < 2) throw new InvalidInputException("Missing '?' separator");

        String questionText = parts[0].trim();
        if (questionText.isEmpty()) throw new InvalidInputException("Question cannot be empty");

        List<Answer> answers = extractAnswers(parts[1].trim());
        if (answers.isEmpty()) throw new InvalidInputException("At least one answer required");

        return new ParsedInput(new Question(questionText), answers);
    }

    private List<Answer> extractAnswers(String answersPart) throws InvalidInputException {
        List<Answer> answers = new ArrayList<>();
        boolean inQuote = false;
        StringBuilder currentAnswer = new StringBuilder();

        for (int i = 0; i < answersPart.length(); i++) {
            char c = answersPart.charAt(i);

            if (c == '"') {
                if (inQuote) {
                    // Closing quote - add completed answer
                    String answerText = currentAnswer.toString().trim();
                    if (!answerText.isEmpty()) {
                        validateAnswerLength(answerText);
                        answers.add(new Answer(answerText));
                    }
                    currentAnswer.setLength(0); // Reset builder
                }
                inQuote = !inQuote;
            } else if (inQuote) {
                currentAnswer.append(c);
            }
        }

        if (inQuote) {
            throw new InvalidInputException("Unclosed quote in answers");
        }

        if (answers.isEmpty()) {
            throw new InvalidInputException("At least one answer required between quotes");
        }

        return answers;
    }

    private void validateAnswerLength(String answerText) {
        if (answerText.length() > Constants.MAX_LENGTH) {
            throw new IllegalArgumentException(
                    String.format("Answer exceeds %d characters", Constants.MAX_LENGTH));
        }
    }

}
