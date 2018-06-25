package net.meyfa.statuscodestrainer.logic.training;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.logic.Generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Generator for training questions.
 */
public class TrainingGenerator implements Generator<TrainingQuestion>
{
    private static final int ANSWER_COUNT = 3;

    private final Random rand;

    private final List<String> possibleAnswers;
    private final List<HTTPStatus> statuses;
    private int nextIndex;

    /**
     * @param statuses The statuses to ask about. These are also the exclusive source for answer options.
     * @throws IllegalArgumentException If fewer statuses are given than needed to generate at least one answer set.
     */
    public TrainingGenerator(@NonNull Collection<HTTPStatus> statuses)
    {
        this.rand = new Random(System.nanoTime());

        // ask questions in random order
        this.statuses = new ArrayList<>(statuses);
        Collections.shuffle(this.statuses, rand);

        // setup answer options to pick from
        this.possibleAnswers = new ArrayList<>();
        for (HTTPStatus status : this.statuses) {
            this.possibleAnswers.add(status.getMessage());
        }
        if (possibleAnswers.size() < ANSWER_COUNT) {
            throw new IllegalArgumentException("too few options for answer generation, need at least" + ANSWER_COUNT);
        }

        // set start position
        this.nextIndex = 0;
    }

    @Override
    public boolean hasNext()
    {
        return nextIndex < statuses.size();
    }

    @Override
    @NonNull
    public TrainingQuestion next() throws NoSuchElementException
    {
        if (!hasNext()) {
            throw new NoSuchElementException("no more questions");
        }
        // use status at current index, then increment that index
        return constructQuestion(statuses.get(nextIndex++));
    }

    /**
     * Construct a question for the given status.
     *
     * @param status The status.
     * @return A question that asks about the status's code.
     */
    @NonNull
    private TrainingQuestion constructQuestion(@NonNull HTTPStatus status)
    {
        List<String> answers = new ArrayList<>();

        // generate random set of false answers
        chooseRandom(answers, ANSWER_COUNT - 1, status.getMessage());
        // add correct answer at random position
        int correctAnswerIndex = placeRandomly(answers, status.getMessage());

        return new TrainingQuestion(status.getCode(), answers, correctAnswerIndex);
    }

    /**
     * Choose {@code count} many answers and add them to the list {@code dest}.
     * The given element {@code except} will not be chosen.
     *
     * @param dest   The destination list.
     * @param count  The number of answers to choose.
     * @param except The element to avoid.
     */
    private void chooseRandom(@NonNull List<String> dest, int count, @Nullable String except)
    {
        while (count > 0) {
            int index = rand.nextInt(possibleAnswers.size());
            String element = possibleAnswers.get(index);
            if (!dest.contains(element) && !element.equals(except)) {
                dest.add(element);
                --count;
            }
        }
    }

    /**
     * Add the element to the list at a random position.
     *
     * @param dest    The destination list.
     * @param element The element to add.
     * @return The index at which the element was added.
     */
    private int placeRandomly(@NonNull List<String> dest, @NonNull String element)
    {
        int index = rand.nextInt(dest.size() + 1);
        dest.add(index, element);

        return index;
    }
}
