package qa.userInterface.cli.parser;

import qa.domain.model.Answer;
import qa.domain.model.Question;

import java.util.List;

/**
 * Result container for parsed data
 * @param question Valid question
 * @param answers List of valid answers
 */
public record ParsedInput(Question question, List<Answer> answers) {}

