package qa.domain.model;

import java.util.Objects;

/**
 * Represents a user's question with validation rules.
 * <p>
 * Immutable value object enforcing:
 * <ul>
 *   <li>Non-null text</li>
 *   <li>Maximum length of 255 chars</li>
 *   <li>Non-blank content</li>
 * </ul>
 */
public record Question(String text) {
    /**
     * @throws IllegalArgumentException if text is blank or exceeds max length
     */
    public Question {
        Objects.requireNonNull(text, "Question cannot be null");
        if (text.isBlank()) throw new IllegalArgumentException("Question cannot be blank");
        if (text.length() > 255) throw new IllegalArgumentException("Question exceeds 255 chars");
    }
}
