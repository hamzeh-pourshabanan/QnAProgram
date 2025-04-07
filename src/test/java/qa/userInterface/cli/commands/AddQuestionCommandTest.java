import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import qa.common.exception.InvalidInputException;
import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.InputPort;
import qa.domain.service.QAService;
import qa.userInterface.cli.commands.AddQuestionCommand;
import qa.userInterface.cli.parser.ParsedInput;
import qa.userInterface.cli.parser.QuestionParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class AddQuestionCommandTest {
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;
    @Mock
    private InputPort mockService;

    @Mock
    private QuestionParser questionParser;

    @InjectMocks
    private AddQuestionCommand command;



    @BeforeEach
    void setUp() {
        command = new AddQuestionCommand();
        mockService = mock(QAService.class);
        questionParser = mock(QuestionParser.class);
        System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    private void provideInput(String data) {
        System.setIn(new ByteArrayInputStream(data.getBytes()));
    }

    private void assertOutputContains(String expected) {
        assertTrue(outputStream.toString().contains(expected),
                "Expected output to contain: " + expected + "\nBut was: " + outputStream.toString());
    }
    @Test
    @DisplayName("Valid input with single answer")
    void testValidInputWithSingleAnswer() {
        String input = "What is Java? \"A programming language\"";
        provideInput(input);
        Question expectedQuestion = new Question("What is Java");
        List<Answer> expectedAnswers = List.of(
                new Answer("A programming language")
        );

        command.execute(new Scanner(System.in), mockService);

        verify(mockService).addQuestion(expectedQuestion, expectedAnswers);
        assertOutputContains("Question and answers added successfully!");
    }

    @Test
    @DisplayName("Valid input with multiple answers")
    void testValidInputWithMultipleAnswers() throws InvalidInputException {
        String input = "How to exit Vim? \":wq\" \":q!\" \"Ctrl+C\"";
        Scanner scanner = new Scanner(input);
        provideInput(input);
        Question expectedQuestion = new Question("How to exit Vim");
        List<Answer> expectedAnswers = Arrays.asList(
                new Answer(":wq"),
                new Answer(":q!"),
                new Answer("Ctrl+C")
        );

        when(questionParser.parse(input))
                .thenReturn(new ParsedInput(expectedQuestion, expectedAnswers));

        command.execute(scanner, mockService);

        verify(mockService).addQuestion(expectedQuestion, expectedAnswers);
        assertOutputContains("Question and answers added successfully!");
    }

    @Test
    @DisplayName("Missing question mark")
    void testMissingQuestionMark() throws InvalidInputException {
        String input = "What is Java \"A programming language\"";
        provideInput(input);
        when(questionParser.parse(input))
                .thenThrow(new InvalidInputException("Missing '?' separator"));
        command.execute(new Scanner(System.in), mockService);
        String output = outputStream.toString();
        assertAll(
                () -> assertTrue(output.contains("Invalid format")),
                () -> assertTrue(output.contains("Missing '?' separator")),
                () -> assertTrue(output.contains("Expected: <question>? \"answer1\" \"answer2\""))
        );
    }

    @Test
    @DisplayName("Empty question")
    void testEmptyQuestion() throws InvalidInputException {
        String input = "? \"Answer\"";
        provideInput(input);
        when(questionParser.parse(input))
                .thenThrow(new InvalidInputException("Question cannot be empty."));
        command.execute(new Scanner(System.in), mockService);

        assertOutputContains("Question cannot be empty");
    }

}