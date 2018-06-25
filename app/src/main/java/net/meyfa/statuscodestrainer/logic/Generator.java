package net.meyfa.statuscodestrainer.logic;

import java.util.NoSuchElementException;

/**
 * Interface for object generators.
 *
 * @param <T> The type of object that is generated.
 */
public interface Generator<T>
{
    /**
     * @return Whether another object can be generated.
     */
    boolean hasNext();

    /**
     * Generates another object.
     *
     * @return An object.
     * @throws NoSuchElementException If no more elements can be generated.
     */
    T next() throws NoSuchElementException;
}
