package net.meyfa.statuscodestrainer.logic.test;

import android.support.annotation.NonNull;

import net.meyfa.statuscodestrainer.data.HTTPStatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Logic for managing the tests.
 */
public class TestLogic
{
    private final TestGenerator generator;

    private final int questionCount;
    private int questionIndex = -1;

    private final List<TestResultsItem> results = new ArrayList<>();
    private TestQuestion currentQuestion;
    private boolean answerGiven = false;

    /**
     * @param statuses      The statuses to ask about. These are also the exclusive source for answer options.
     * @param questionCount The number of questions to ask.
     * @throws IllegalArgumentException If fewer statuses are given than the number of questions to ask.
     */
    public TestLogic(@NonNull Collection<HTTPStatus> statuses, int questionCount)
    {
        this.generator = new TestGenerator(statuses, questionCount);
        this.questionCount = questionCount;
    }

    /**
     * @return The total number of questions.
     */
    public int getQuestionCount()
    {
        return questionCount;
    }

    /**
     * @return The current question's index (0 for the first, 1 for the second, etc).
     */
    public int getQuestionIndex()
    {
        return questionIndex;
    }

    /**
     * Checks whether there is another question lined up.
     *
     * @return Whether another question can be obtained.
     */
    public boolean hasNextQuestion()
    {
        return generator.hasNext();
    }

    /**
     * Generates a new question.
     *
     * @return The next question.
     */
    @NonNull
    public TestQuestion nextQuestion()
    {
        currentQuestion = generator.next();

        ++questionIndex;
        answerGiven = false;

        return currentQuestion;
    }

    /**
     * Enters the given string as the answer to the current question.
     *
     * @param answer The answer to enter.
     */
    public void enterAnswer(@NonNull String answer)
    {
        if (answerGiven) {
            return;
        }
        answerGiven = true;

        String correctAnswer = currentQuestion.getCorrectAnswer();
        boolean acceptable = isAcceptable(correctAnswer, answer);

        results.add(new TestResultsItem(correctAnswer, answer, acceptable));
    }

    /**
     * Evaluates the test answers, returning the results.
     *
     * @return The test results.
     */
    public TestResults evaluate()
    {
        return new TestResults(results);
    }

    /**
     * Determines whether {@code givenAnswer} is enough of a match for {@code correctAnswer} to be accepted.
     *
     * @param correctAnswer The correct answer.
     * @param givenAnswer   The answer that was entered.
     * @return Whether the answer that was entered can be accepted as correct.
     */
    private boolean isAcceptable(@NonNull String correctAnswer, @NonNull String givenAnswer)
    {
        String canonicalCorrect = makeCanonical(correctAnswer);
        String canonicalGiven = makeCanonical(givenAnswer);

        return canonicalGiven.equals(canonicalCorrect);
    }

    /**
     * Converts the given answer string into a canonical form for comparison.
     *
     * @param answerString The string to convert.
     * @return The conversion result.
     */
    private String makeCanonical(@NonNull String answerString)
    {
        String canonical = answerString;

        // lowercase
        canonical = canonical.toLowerCase();
        // convert (some) special characters to whitespace
        canonical = canonical.replaceAll("[\\s_,.\"'-]+", " ");
        // remove leading, trailing whitespace
        canonical = canonical.trim();
        // remove code prefix ("200 OK" -> "OK")
        canonical = canonical.replaceFirst("^\\d+\\s*", "");

        return canonical;
    }
}
