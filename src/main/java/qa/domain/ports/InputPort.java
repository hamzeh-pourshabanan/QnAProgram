package qa.domain.ports;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.userInterface.cli.adapter.ConsoleInputAdapter;

import java.util.List;

/**
 * Primary port for user interaction workflows.
 * <p>
 * Implemented by:
 * <ul>
 *   <li>{@link ConsoleInputAdapter} - CLI interface</li>
 *   <li>Future Web/API adapters</li>
 * </ul>
 */
public interface InputPort {
    /**
     * Processes a question inquiry
     * @param question Trimmed user input excluding question mark (?)
     * @return List of answers if existed, list of default answer otherwise
     */
    List<Answer> askQuestion(Question question);

    /**
     * Processes a question submission
     * @param question Exact user input including question mark and answers
     * @param answers List of input answers
     */
    void addQuestion(Question question, List<Answer> answers);
}
