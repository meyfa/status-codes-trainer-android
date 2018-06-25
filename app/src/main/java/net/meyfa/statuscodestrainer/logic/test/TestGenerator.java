package net.meyfa.statuscodestrainer.logic.test;

import android.support.annotation.NonNull;

import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.logic.Generator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Generator for test questions.
 */
public class TestGenerator implements Generator<TestQuestion>
{
    private final Random rand;

    private final List<HTTPStatus> statuses;
    private final int questionCount;
    private int nextIndex;

    /**
     * @param statuses      The statuses to ask about.
     * @param questionCount The number of questions to ask.
     * @throws IllegalArgumentException If fewer statuses are given than the number of questions to ask.
     */
    public TestGenerator(@NonNull List<HTTPStatus> statuses, int questionCount)
    {
        if (questionCount > statuses.size()) {
            throw new IllegalArgumentException("too few statuses for question generation");
        }

        this.rand = new Random(System.nanoTime());

        // ask questions in random order
        this.statuses = new ArrayList<>(statuses);
        Collections.shuffle(this.statuses, rand);

        this.questionCount = questionCount;

        // set start position
        this.nextIndex = 0;
    }

    @Override
    public boolean hasNext()
    {
        return nextIndex < questionCount;
    }

    @Override
    public TestQuestion next() throws NoSuchElementException
    {
        if (!hasNext()) {
            throw new NoSuchElementException("no more questions");
        }

        // get status, then increment index
        HTTPStatus status = statuses.get(nextIndex++);

        return new TestQuestion(status.getCode(), status.getMessage());
    }
}
