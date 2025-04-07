package qa.userInterface.cli.parser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import qa.common.Constants;
import qa.common.exception.InvalidInputException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class QuestionParserTest {

    private final QuestionParser parser = new QuestionParser();

    @Test
    void parse_validInputWithSingleAnswer() throws InvalidInputException {
        String input = "What is Java? \"A programming language\"";
        ParsedInput result = parser.parse(input);

        assertEquals("What is Java", result.question().text());
        assertEquals(1, result.answers().size());
        assertEquals("A programming language", result.answers().get(0).text());
    }

    @ParameterizedTest
    @MethodSource("invalidInputsProvider")
    void parse_invalidInputs_throwsException(String input, String expectedError) {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> parser.parse(input)
        );
        assertTrue(exception.getMessage().contains(expectedError));
    }

    private static Stream<Arguments> invalidInputsProvider() {
        return Stream.of(
                arguments("No question mark", "Missing '?' separator"),
                arguments("? \"Only answer\"", "Question cannot be empty"),
                arguments("Question?", "At least one answer required"),
                arguments("Question? Not quoted", "At least one answer required between quotes"),
                arguments("Question? \"\"", "At least one answer required"),
                arguments("Question? \"Unclosed quote", "Unclosed quote in answers")
        );
    }

    @Test
    void parse_answerExceedsMaxLength_throwsException() {
        String longAnswer = "a".repeat(Constants.MAX_LENGTH + 1);
        String input = "Q? \"" + longAnswer + "\"";

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> parser.parse(input)
        );

        assertTrue(exception.getMessage().contains(
                String.format("Answer exceeds %d characters", Constants.MAX_LENGTH))
        );
    }

    @Test
    void parse_handlesEmptyInput() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> parser.parse("")
        );
        assertTrue(exception.getMessage().contains("Missing '?' separator"));
    }

    @Test
    void parse_handlesOnlyQuestionMark() {
        InvalidInputException exception = assertThrows(
                InvalidInputException.class,
                () -> parser.parse("?")
        );
        assertTrue(exception.getMessage().contains("Question cannot be empty"));
    }

    @Test
    void parse_handlesMultipleQuestionMarks() throws InvalidInputException {
        String input = "What is? Java? \"A language\"";
        ParsedInput result = parser.parse(input);

        assertEquals("What is", result.question().text());
        assertEquals(1, result.answers().size());
        assertEquals("A language", result.answers().get(0).text());
    }

}