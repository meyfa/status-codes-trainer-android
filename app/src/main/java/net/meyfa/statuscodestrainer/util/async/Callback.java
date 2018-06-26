package net.meyfa.statuscodestrainer.util.async;

/**
 * A callback for an asynchronous action.
 *
 * @param <Result> The type of result (preferrably {@link Void} if no result will be obtained).
 */
public interface Callback<Result>
{
    /**
     * Called when the action completes.
     *
     * @param result The result of the action.
     */
    void done(Result result);
}
