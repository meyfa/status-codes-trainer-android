package net.meyfa.statuscodestrainer.logic.test;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Represents a single item in the test results, i.e. a single question response.
 */
public class TestResultsItem implements Serializable
{
    private final int code;
    private final String correctAnswer, givenAnswer;
    private final boolean accepted;

    /**
     * @param code          The code that was asked about.
     * @param correctAnswer The correct answer to the respective question.
     * @param givenAnswer   The user-provided answer.
     * @param accepted      Whether the user-provided answer was accepted as correct.
     */
    public TestResultsItem(int code, @NonNull String correctAnswer, @NonNull String givenAnswer, boolean accepted)
    {
        this.code = code;

        this.correctAnswer = correctAnswer;
        this.givenAnswer = givenAnswer;

        this.accepted = accepted;
    }

    /**
     * @return The code that was asked about.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return Whether the user-provided answer was accepted as correct.
     */
    public boolean isAccepted()
    {
        return accepted;
    }

    /**
     * @return The correct answer to the respective question.
     */
    @NonNull
    public String getCorrectAnswer()
    {
        return correctAnswer;
    }

    /**
     * @return The user-provided answer.
     */
    @NonNull
    public String getGivenAnswer()
    {
        return givenAnswer;
    }
}
