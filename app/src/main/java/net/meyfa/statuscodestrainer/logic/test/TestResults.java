package net.meyfa.statuscodestrainer.logic.test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * The results of a set of test questions.
 */
public class TestResults implements Serializable
{
    private final List<TestResultsItem> items;

    /**
     * @param items The test result items.
     */
    public TestResults(Collection<TestResultsItem> items)
    {
        this.items = Collections.unmodifiableList(new ArrayList<>(items));
    }

    /**
     * @return The test result items.
     */
    public List<TestResultsItem> getItems()
    {
        return items;
    }
}
