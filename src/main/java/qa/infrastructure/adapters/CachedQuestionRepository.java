package qa.infrastructure.adapters;

import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.QuestionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachedQuestionRepository implements QuestionRepository {
    private final QuestionRepository delegate;
    private final Map<Question, List<Answer>> cache = new HashMap<>();

    public CachedQuestionRepository(QuestionRepository delegate) {
        this.delegate = delegate;
    }

    @Override
    public void save(Question question, List<Answer> answers) {
        delegate.save(question, answers);
        cache.put(question, answers);
    }

    @Override
    public List<Answer> findByQuestion(Question question) {
        return cache.computeIfAbsent(question, delegate::findByQuestion);
    }

    @Override
    public boolean exists(Question question) {
        if (cache.containsKey(question)) return true;
        return delegate.exists(question);
    }
}