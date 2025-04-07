package qa.domain.model;

import java.util.Objects;

/**
 * Represents a possible answer to a question with validation.
 * <p>
 * Enforces same constraints as {@link Question}
 */
public record Answer(String text) {
    /**
     * @throws IllegalArgumentException if text is blank or exceeds max length
     */
    public Answer {
        Objects.requireNonNull(text, "Answer cannot be null");
        if (text.isBlank()) throw new IllegalArgumentException("Answer cannot be blank");
        if (text.length() > 255) throw new IllegalArgumentException("Answer exceeds 255 chars");
    }
}
