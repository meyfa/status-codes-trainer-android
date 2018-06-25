package net.meyfa.statuscodestrainer.activities.testresults;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.logic.test.TestResults;
import net.meyfa.statuscodestrainer.logic.test.TestResultsItem;

import java.util.List;
import java.util.Locale;

public class TestResultsActivity extends AppCompatActivity
{
    /**
     * The intent data key with which to pass the results.
     */
    public static final String INTENT_EXTRA_RESULTS = TestResultsActivity.class.getName() + ":results";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_results);

        // configure toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        TestResults results = (TestResults) getIntent().getSerializableExtra(INTENT_EXTRA_RESULTS);
        List<TestResultsItem> items = results.getItems();

        // show total
        int count = items.size(), correctCount = 0;
        for (TestResultsItem item : items) {
            if (item.isAccepted()) {
                ++correctCount;
            }
        }
        showResultTotal(count, correctCount);

        // show details
        showResultDetails(items);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showResultTotal(int count, int correctCount)
    {
        // label
        TextView totalLabel = findViewById(R.id.label_result_total);
        String totalTemplate = getResources().getString(R.string.test_results_total);
        totalLabel.setText(String.format(totalTemplate, correctCount, count));

        // progress bar
        ProgressBar totalBar = findViewById(R.id.bar_result_total);
        totalBar.setMax(count);
        totalBar.setProgress(correctCount);
    }

    private void showResultDetails(List<TestResultsItem> items)
    {
        LinearLayout container = findViewById(R.id.results_container);

        container.removeAllViews();

        LayoutInflater inflater = getLayoutInflater();
        for (TestResultsItem item : items) {
            int layout = item.isAccepted() ? R.layout.test_results_item_correct : R.layout.test_results_item_incorrect;
            View itemView = inflater.inflate(layout, container, false);
            setItemData(itemView, item);
            container.addView(itemView);
        }
    }

    private void setItemData(View itemView, TestResultsItem data)
    {
        // set code
        TextView codeView = itemView.findViewById(R.id.label_code);
        codeView.setText(String.format(Locale.US, "%d", data.getCode()));

        // set correct message
        TextView messageView = itemView.findViewById(R.id.label_message);
        messageView.setText(data.getCorrectAnswer());

        if (!data.isAccepted()) {
            // set incorrect message
            TextView incorrectMessageView = itemView.findViewById(R.id.label_message_incorrect);
            String template = getResources().getString(R.string.test_results_incorrect);
            incorrectMessageView.setText(String.format(template, data.getGivenAnswer()));
        }
    }
}
