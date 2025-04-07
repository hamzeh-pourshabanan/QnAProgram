package qa.domain.service;

import org.junit.jupiter.api.Test;
import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.QuestionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QAServiceTest {

    @Test
    void shouldReturnDefaultAnswerWhenQuestionNotFound() {
        // Given
        QuestionRepository mockRepo = mock(QuestionRepository.class);

        // When
        when(mockRepo.exists(any(Question.class)))
                .thenReturn(false);

        QAService service = new QAService(mockRepo);
        List<Answer> answers = service.askQuestion(new Question("Unknown"));

        // Then
        assertThat(answers)
                .hasSize(1)
                .extracting(Answer::text)
                .contains("the answer to life, universe and everything is 42");

        verify(mockRepo).exists(any(Question.class));
    }

}