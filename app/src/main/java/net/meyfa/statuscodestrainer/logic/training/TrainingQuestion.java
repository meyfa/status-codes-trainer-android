package net.meyfa.statuscodestrainer.logic.training;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A question as shown on the training screen.
 */
public class TrainingQuestion
{
    private final int code;
    private final List<String> answers;
    private final int correctAnswerIndex;

    /**
     * @param code               The code to ask about.
     * @param answers            The list of possible answers.
     * @param correctAnswerIndex The index of the correct answer.
     */
    public TrainingQuestion(int code, @NonNull List<String> answers, int correctAnswerIndex)
    {
        this.code = code;
        this.answers = Collections.unmodifiableList(new ArrayList<>(answers));
        this.correctAnswerIndex = correctAnswerIndex;
    }

    /**
     * @return The code to ask about.
     */
    public int getCode()
    {
        return code;
    }

    /**
     * @return The list of possible answers.
     */
    @NonNull
    public List<String> getAnswers()
    {
        return answers;
    }

    /**
     * @return The index of the correct answer.
     */
    public int getCorrectAnswerIndex()
    {
        return correctAnswerIndex;
    }
}
