package net.meyfa.statuscodestrainer.logic.training;

/**
 * Represents a response generated when the user chooses an answer during training.
 * <p>
 * The response indicates either that the answer is correct, or that it is incorrect.
 * In the latter case, the correct answer is also provided.
 */
public class TrainingResponse
{
    private final int correct, chosen;

    /**
     * @param correct The correct answer's index.
     * @param chosen  The chosen answer's index.
     */
    public TrainingResponse(int correct, int chosen)
    {
        this.correct = correct;
        this.chosen = chosen;
    }

    /**
     * @return Whether the correct answer was chosen
     */
    public boolean isCorrect()
    {
        return chosen == correct;
    }

    /**
     * @return The index of the correct answer.
     */
    public int getCorrectAnswerIndex()
    {
        return correct;
    }

    /**
     * @return The index of the chosen answer.
     */
    public int getChosenAnswerIndex()
    {
        return chosen;
    }
}
