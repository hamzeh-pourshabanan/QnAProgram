package qa.domain.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import qa.domain.model.Answer;
import qa.domain.model.Question;
import qa.domain.ports.QuestionRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class QAServiceTest {

    private QuestionRepository repository;

    private QAService qaService;

    private final Question existingQuestion = new Question("What is Java?");
    private final Question newQuestion = new Question("What is Kotlin?");
    private final List<Answer> sampleAnswers = List.of(
            new Answer("A programming language"),
            new Answer("A platform")
    );

    @BeforeEach
    void setUp() {
        repository = mock(QuestionRepository.class);
        qaService = new QAService(repository);
    }
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

    @Test
    void shouldReturnsStoredAnswersWhenQuestionExists() {
        when(repository.exists(existingQuestion)).thenReturn(true);
        when(repository.findByQuestion(existingQuestion)).thenReturn(sampleAnswers);

        List<Answer> result = qaService.askQuestion(existingQuestion);

        assertEquals(2, result.size());
        assertEquals("A programming language", result.get(0).text());
        verify(repository).exists(existingQuestion);
        verify(repository).findByQuestion(existingQuestion);
    }

    @Test
    void shouldThrowsExceptionWhenRepositoryFails() {
        doThrow(new RuntimeException("DB error"))
                .when(repository).save(any(), any());


        assertThrows(RuntimeException.class,
                () -> qaService.addQuestion(existingQuestion, sampleAnswers));
    }

    @Test
    void constructorShouldRejectNullRepository() {
        assertThrows(NullPointerException.class,
                () -> new QAService(null));
    }

}