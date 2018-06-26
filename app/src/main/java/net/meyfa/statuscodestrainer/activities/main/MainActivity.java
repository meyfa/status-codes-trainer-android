package net.meyfa.statuscodestrainer.activities.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.activities.reference.ReferenceActivity;
import net.meyfa.statuscodestrainer.activities.test.TestActivity;
import net.meyfa.statuscodestrainer.activities.training.TrainingActivity;
import net.meyfa.statuscodestrainer.logic.progress.ProgressTracker;
import net.meyfa.statuscodestrainer.logic.progress.database.ProgressItem;
import net.meyfa.statuscodestrainer.util.async.ActivityActionRunner;
import net.meyfa.statuscodestrainer.util.async.AsyncAction;
import net.meyfa.statuscodestrainer.util.async.Callback;

import org.joda.time.DateTimeZone;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The main activity.
 */
public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        // fetch progress items and display them when ready
        fetchProgress();
    }

    /**
     * Click handler for the "Reference" button.
     *
     * @param view The clicked view.
     */
    public void clickReference(@SuppressWarnings("unused") View view)
    {
        Intent launch = new Intent(this, ReferenceActivity.class);
        startActivity(launch);
    }

    /**
     * Click handler for the "Training" button.
     *
     * @param view The clicked view.
     */
    public void clickTraining(@SuppressWarnings("unused") View view)
    {
        Intent launch = new Intent(this, TrainingActivity.class);
        startActivity(launch);
    }

    /**
     * Click handler for the "Test" button.
     *
     * @param view The clicked view.
     */
    public void clickTest(@SuppressWarnings("unused") View view)
    {
        Intent launch = new Intent(this, TestActivity.class);
        startActivity(launch);
    }

    /**
     * Asynchronously fetch the progress items and show them afterwards.
     */
    private void fetchProgress()
    {
        final Context appContext = getApplicationContext();

        new ActivityActionRunner<>(this, new AsyncAction<List<ProgressItem>>()
        {
            @Override
            protected List<ProgressItem> execute()
            {
                // get the items
                ProgressTracker tracker = ProgressTracker.getInstance(appContext);
                return tracker.getProgressItems();
            }
        }).run(new Callback<List<ProgressItem>>()
        {
            @Override
            public void done(List<ProgressItem> result)
            {
                // display the items
                showProgress(result);
            }
        });
    }

    /**
     * Shows the given list of progress items.
     *
     * @param items The items to show.
     */
    private void showProgress(@NonNull List<ProgressItem> items)
    {
        // sort by timestamp in descending order
        Collections.sort(items, new Comparator<ProgressItem>()
        {
            @Override
            public int compare(ProgressItem o1, ProgressItem o2)
            {
                return o2.getDateTime().compareTo(o1.getDateTime());
            }
        });

        LinearLayout container = findViewById(R.id.progress_container);
        container.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();

        // view empty? show label
        if (items.isEmpty()) {
            View emptyView = inflater.inflate(R.layout.progress_empty_item, container, false);
            container.addView(emptyView);
            return;
        }

        // view not empty? add progress item views
        for (ProgressItem item : items) {
            View itemView = inflater.inflate(R.layout.progress_item, container, false);
            setProgressItemData(itemView, item);
            container.addView(itemView);
        }
    }

    /**
     * Initialize the given {@code progress_item} view with the given data object.
     *
     * @param itemView The view.
     * @param data     The data.
     */
    private void setProgressItemData(View itemView, ProgressItem data)
    {
        // display date and time
        TextView dateView = itemView.findViewById(R.id.label_date);
        dateView.setText(data.getDateTime().toDateTime(DateTimeZone.getDefault()).toString("yyyy-MM-dd HH:mm"));

        // display result total
        TextView totalView = itemView.findViewById(R.id.label_total);
        String totalTemplate = getResources().getString(R.string.main_progress_total);
        totalView.setText(String.format(totalTemplate, data.getCorrectCount(), data.getQuestionCount()));

        // display result total as progress bar
        ProgressBar totalBar = itemView.findViewById(R.id.bar_total);
        totalBar.setMax(data.getQuestionCount());
        totalBar.setProgress(data.getCorrectCount());
    }
}
