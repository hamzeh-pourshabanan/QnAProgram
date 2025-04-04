import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class AskQuestionCommandTest {
    private final ByteArrayOutputStream output = new ByteArrayOutputStream();
    private Scanner scanner;
    private Map<String, List<String>> knowledgeBase;
    private AskQuestionCommand command;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(output));
        knowledgeBase = new HashMap<>();
        command = new AskQuestionCommand();
        knowledgeBase.put("How to exit Vim?", Collections.singletonList(":q!"));
        knowledgeBase.put("What is Java?", Arrays.asList("A programming language", "Object-oriented"));

    }
    @Test
    @DisplayName("Existing question with single answer")
    void testExistingQuestionSingleAnswer() {
        String input = "How to exit Vim?";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        assertEquals("Enter your question: :q!\n", output.toString());
    }

    @Test
    @DisplayName("Existing question with multiple answers")
    void testExistingQuestionMultipleAnswers() {
        String input = "What is Java?";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        String expected = "Enter your question: A programming language\nObject-oriented\n";
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("Non-existent question")
    void testNonExistentQuestion() {
        String input = "What is Python?";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        String expected = "Enter your question: " + Constants.UNIVERSE_RESPONSE + "\n";
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("Empty question input")
    void testEmptyQuestion() {
        String input = "   ";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        String expected = "Enter your question: " + Constants.UNIVERSE_RESPONSE + "\n";
        assertEquals(expected, output.toString());
    }

    @Test
    @DisplayName("Case sensitivity test")
    void testCaseSensitivity() {
        String input = "WHAT IS JAVA?";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        String expected = "Enter your question: " + Constants.UNIVERSE_RESPONSE + "\n";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testMaxLengthRestriction() {
        String longString = "a".repeat(256);
        scanner = new Scanner(longString);

        command.execute(scanner, knowledgeBase);

        assertEquals("Enter your question: " + Constants.MAX_LENGTH_WARNING + "\n",output.toString());
    }
}