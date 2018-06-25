package net.meyfa.statuscodestrainer.logic.test;

import android.support.annotation.NonNull;

/**
 * A question as shown on the test screen.
 */
public class TestQuestion
{
    private final int code;
    private final String correctAnswer;

    /**
     * @param code          The code to ask about.
     * @param correctAnswer The correct answer.
     */
    public TestQuestion(int code, String correctAnswer)
    {
        this.code = code;
        this.correctAnswer = correctAnswer;
    }

    /**
     * @return The code to ask about.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return The correct answer string.
     */
    @NonNull
    public String getCorrectAnswer()
    {
        return correctAnswer;
    }
}
