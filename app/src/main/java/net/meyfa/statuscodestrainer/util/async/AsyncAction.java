package net.meyfa.statuscodestrainer.util.async;

import android.support.annotation.Nullable;

/**
 * A generic action that can be run asynchronously and which will invoke a callback on completion.
 *
 * @param <Result> The type of result (preferrably {@link Void} if no result will be obtained).
 */
public abstract class AsyncAction<Result>
{
    /**
     * Starts this action in a separate thread. The callback will be invoked on completion.
     *
     * @param callback The callback.
     */
    public void run(@Nullable final Callback<? super Result> callback)
    {
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Result result = execute();
                if (callback != null) {
                    callback.done(result);
                }
            }
        }).start();
    }

    /**
     * Execute this action in the current thread.
     *
     * @return The result.
     */
    protected abstract Result execute();
}
