package qa.domain.service;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.InputPort;
import qa.domain.ports.QuestionRepository;

import java.util.List;
import java.util.Objects;

/**
 * Central business logic service coordinating:
 * <ul>
 *   <li>Question/answer validation</li>
 *   <li>Workflow orchestration</li>
 *   <li>Exception translation</li>
 *   <li>Implements input port</li>
 *   <li>Depends on output port (Repository)</li>
 * </ul>
 */
public class QAService implements InputPort {
    private static final Answer DEFAULT_ANSWER =
            new Answer("the answer to life, universe and everything is 42");

    private final QuestionRepository repository;

    /**
     * @param repository Required persistence layer
     */
    public QAService(QuestionRepository repository) {
        Objects.requireNonNull(repository);
        this.repository = repository;
    }
    @Override
    public List<Answer> askQuestion(Question question) {
        if (repository.exists(question)) {
            return repository.findByQuestion(question);
        }
        return List.of(DEFAULT_ANSWER);
    }

    @Override
    public void addQuestion(Question question, List<Answer> answers) {
        repository.save(question, answers);
    }
}
