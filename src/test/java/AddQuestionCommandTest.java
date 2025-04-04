import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AddQuestionCommandTest {
    private AddQuestionCommand command;
    private Map<String, List<String>> knowledgeBase;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @BeforeEach
    void setUp() {
        command = new AddQuestionCommand();
        knowledgeBase = new HashMap<>();
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

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.containsKey("What is Java"));
        assertEquals(List.of("A programming language"), knowledgeBase.get("What is Java"));
        assertOutputContains("Question and answers added successfully!");
    }

    @Test
    @DisplayName("Valid input with multiple answers")
    void testValidInputWithMultipleAnswers() {
        String input = "How to exit Vim? \":wq\" \":q!\" \"Ctrl+C\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertEquals(List.of(":wq", ":q!", "Ctrl+C"), knowledgeBase.get("How to exit Vim"));
        assertOutputContains("Question and answers added successfully!");
    }

    @Test
    @DisplayName("Missing question mark")
    void testMissingQuestionMark() {
        String input = "What is Java \"A programming language\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.isEmpty());
        assertOutputContains("Invalid format. Missing '?' separator.");
    }

    @Test
    @DisplayName("Empty question")
    void testEmptyQuestion() {
        String input = "? \"Answer\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.isEmpty());
        assertOutputContains("Question cannot be empty.");
    }

    @Test
    @DisplayName("No answers provided")
    void testNoAnswersProvided() {
        String input = "What is Java?";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.isEmpty());
        assertOutputContains("At least one answer is required.");
    }

    @Test
    @DisplayName("Empty answer in quotes")
    void testEmptyAnswerInQuotes() {
        String input = "Question? \"\" \"Valid answer\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertEquals(List.of("Valid answer"), knowledgeBase.get("Question"));
    }

    @Test
    @DisplayName("Unclosed quote in answers")
    void testUnclosedQuote() {
        String input = "Question? \"Answer1\" \"Answer2";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertEquals(List.of("Answer1"), knowledgeBase.get("Question"));
    }

    @Test
    @DisplayName("Whitespace in question")
    void testWhitespaceInQuestion() {
        String input = "   What is Java   ? \"Answer\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.containsKey("What is Java"));
    }

    @Test
    @DisplayName("Answer exceeding max length")
    void testAnswerExceedingMaxLength() {
        String longAnswer = "a".repeat(Constants.MAX_LENGTH + 1);
        String input = "Question? \"" + longAnswer + "\"";
        provideInput(input);

        command.execute(new Scanner(System.in), knowledgeBase);

        assertTrue(knowledgeBase.isEmpty());
        assertOutputContains(String.format("Answer exceeds maximum length of %d characters", Constants.MAX_LENGTH));
    }

}