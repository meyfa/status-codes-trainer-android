package net.meyfa.statuscodestrainer.logic.progress;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.annotation.NonNull;

import net.meyfa.statuscodestrainer.logic.progress.database.ProgressDatabase;
import net.meyfa.statuscodestrainer.logic.progress.database.ProgressItem;

import org.joda.time.DateTime;

import java.util.List;

/**
 * This is the main progress tracker that operates the progress database.
 * <p>
 * Use {@link #getInstance(Context)} to obtain the singleton.
 */
public class ProgressTracker
{
    private static volatile ProgressTracker instance;

    private final Context appContext;
    private final ProgressDatabase db;

    private ProgressTracker(@NonNull Context appContext)
    {
        this.appContext = appContext;
        this.db = Room.databaseBuilder(appContext, ProgressDatabase.class, "progressdb").build();
    }

    /**
     * Obtain the singleton instance. This operation is thread-safe.
     *
     * @param appContext The application context.
     * @return The instance.
     */
    public static ProgressTracker getInstance(@NonNull Context appContext)
    {
        if (instance == null) {
            synchronized (ProgressTracker.class) {
                if (instance == null) {
                    instance = new ProgressTracker(appContext);
                }
            }
        }
        return instance;
    }

    /**
     * Obtain a list of all progress items (in no particular order).
     *
     * @return All progress items.
     */
    public List<ProgressItem> getProgressItems()
    {
        return db.progressItemDao().getAll();
    }

    /**
     * Create a progress item.
     *
     * @param dateTime      The time at which the test was taken.
     * @param questionCount The number of questions that were asked.
     * @param correctCount  The number of questions that were answered correctly.
     * @return The newly created item.
     */
    public ProgressItem createProgressItem(DateTime dateTime, int questionCount, int correctCount)
    {
        ProgressItem item = new ProgressItem();

        item.setDateTime(dateTime);
        item.setQuestionCount(questionCount);
        item.setCorrectCount(correctCount);

        db.progressItemDao().insertAll(item);

        return item;
    }
}
