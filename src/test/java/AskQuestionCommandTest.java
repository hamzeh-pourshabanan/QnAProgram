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
    }
    @Test
    @DisplayName("Existing question with single answer")
    void testExistingQuestionSingleAnswer() {
        String input = "How to exit Vim?";
        scanner = new Scanner(input);

        command.execute(scanner, knowledgeBase);

        assertEquals("Enter your question: :q!\n", output.toString());
    }

}