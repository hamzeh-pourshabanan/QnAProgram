package qa.domain.ports;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.infrastructure.adapters.CachedQuestionRepository;
import qa.infrastructure.adapters.SQLiteQuestionRepository;

import java.util.List;

/**
 * Persistence contract for question/answer storage.
 * <p>
 * Current implementations:
 * <ul>
 *   <li>{@link SQLiteQuestionRepository} - Production DB</li>
 *   <li>{@link CachedQuestionRepository} - In-memory ache</li>
 * </ul>
 */
public interface QuestionRepository {
    void save(Question question, List<Answer> answers);
    List<Answer> findByQuestion(Question question);
    boolean exists(Question question);
}
