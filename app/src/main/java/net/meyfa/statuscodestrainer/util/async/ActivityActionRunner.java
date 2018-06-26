package net.meyfa.statuscodestrainer.util.async;

import android.app.Activity;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * This is a wrapper around {@link AsyncAction}.
 * It allows attaching a callback that will be run in the UI thread of an activity.
 *
 * @param <Result> The type of result (preferrably {@link Void} if no result will be obtained).
 */
public class ActivityActionRunner<Result>
{
    private final WeakReference<Activity> activityReference;
    private final AsyncAction<? extends Result> action;

    /**
     * @param activity The activity.
     * @param action   The action to run.
     */
    public ActivityActionRunner(Activity activity, AsyncAction<? extends Result> action)
    {
        this.activityReference = new WeakReference<>(activity);
        this.action = action;
    }

    /**
     * Runs the action. When it completes, the given callback is scheduled to be run on the activity's UI thread,
     * receiving the action's result.
     *
     * @param callback The callback.
     */
    public void run(@Nullable final Callback<Result> callback)
    {
        // start the action as normal
        action.run(new Callback<Result>()
        {
            @Override
            public void done(final Result result)
            {
                Activity activity = activityReference.get();
                // activity and callback must exist for it to be invoked
                if (activity == null || callback == null) {
                    return;
                }

                activity.runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.done(result);
                    }
                });
            }
        });
    }
}
