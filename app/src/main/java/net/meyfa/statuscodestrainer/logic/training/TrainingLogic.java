package net.meyfa.statuscodestrainer.logic.training;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.meyfa.statuscodestrainer.data.HTTPStatus;

import java.util.List;

/**
 * Logic for managing the training.
 */
public class TrainingLogic
{
    private final List<HTTPStatus> statuses;
    private TrainingGenerator generator;

    private TrainingQuestion currentQuestion;
    private boolean answerChosen;

    /**
     * @param statuses The statuses to ask about. These are also the exclusive source for answer options.
     */
    public TrainingLogic(@NonNull List<HTTPStatus> statuses)
    {
        this.statuses = statuses;
        this.generator = new TrainingGenerator(statuses);
    }

    /**
     * Generates a new question.
     *
     * @return The next question.
     */
    @NonNull
    public TrainingQuestion nextQuestion()
    {
        if (!generator.hasNext()) {
            // continue with a new generator if previous one ran out
            generator = new TrainingGenerator(statuses);
        }

        currentQuestion = generator.next();
        answerChosen = false;

        return currentQuestion;
    }

    @Nullable
    public TrainingResponse chooseAnswer(int index)
    {
        if (answerChosen) {
            return null;
        }
        answerChosen = true;

        return new TrainingResponse(currentQuestion.getCorrectAnswerIndex(), index);
    }
}
