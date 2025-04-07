package qa.userInterface.cli.commands;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.common.Constants;
import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.InputPort;
import qa.domain.service.QAService;
import qa.userInterface.cli.commands.AskQuestionCommand;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AskQuestionCommandTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private Scanner scanner;
    private AskQuestionCommand command;
    private InputPort mockService;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
        mockService = mock(QAService.class);
        command = new AskQuestionCommand();
    }
    @Test
    @DisplayName("Existing question with single answer")
    void testExistingQuestionSingleAnswer() {
        String input = "How to exit Vim?";
        scanner = new Scanner(input);
        when(mockService.askQuestion(new Question("How to exit Vim")))
                .thenReturn(Collections.singletonList(new Answer(":q!")));

        command.execute(scanner, mockService);

        assertEquals("Enter your question: :q!\n", output.toString());
    }

    @Test
    @DisplayName("Existing question with multiple answers")
    void testExistingQuestionMultipleAnswers() {
        String input = "What is Java";
        scanner = new Scanner(input + "?");
        when(mockService.askQuestion(new Question(input)))
                .thenReturn(Arrays.asList(new Answer("A programming language"), new Answer("Object-oriented")));

        command.execute(scanner, mockService);

        String expected = "Enter your question: A programming language\nObject-oriented\n";
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("Non-existent question")
    void testNonExistentQuestion() {
        String input = "What is Python";
        scanner = new Scanner(input + "?");
        when(mockService.askQuestion(new Question(input)))
                .thenReturn(List.of(new Answer(Constants.UNIVERSE_RESPONSE)));

        command.execute(scanner, mockService);

        String expected = "Enter your question: " + Constants.UNIVERSE_RESPONSE + "\n";
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("Empty question input")
    void testEmptyQuestion() {
        String input = "   ";
        scanner = new Scanner(input);

        assertThrows(IllegalArgumentException.class,
                () -> {
                    when(mockService.askQuestion(new Question(input)))
                            .thenReturn(List.of(new Answer(Constants.UNIVERSE_RESPONSE)));

                    command.execute(scanner, mockService);
                });

    }

    @Test
    @DisplayName("Case sensitivity test")
    void testCaseSensitivity() {
        String input = "WHAT IS JAVA";
        scanner = new Scanner(input + "?");
        when(mockService.askQuestion(new Question(input)))
                .thenReturn(List.of(new Answer(Constants.UNIVERSE_RESPONSE)));

        command.execute(scanner, mockService);

        String expected = "Enter your question: " + Constants.UNIVERSE_RESPONSE + "\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testMaxLengthRestriction() {
        String longString = "a".repeat(256);
        scanner = new Scanner(longString);
        assertThrows(IllegalArgumentException.class, () -> {
            when(mockService.askQuestion(new Question(longString)))
                    .thenReturn(List.of(new Answer(Constants.UNIVERSE_RESPONSE)));

            command.execute(scanner, mockService);
        },
                "Question exceeds 255 chars");
    }
}