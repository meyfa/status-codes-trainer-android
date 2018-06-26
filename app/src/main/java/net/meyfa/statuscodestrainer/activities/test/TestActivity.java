package net.meyfa.statuscodestrainer.activities.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.activities.testresults.TestResultsActivity;
import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.data.HTTPStatusClass;
import net.meyfa.statuscodestrainer.data.HTTPStatuses;
import net.meyfa.statuscodestrainer.logic.progress.ProgressTracker;
import net.meyfa.statuscodestrainer.logic.test.TestLogic;
import net.meyfa.statuscodestrainer.logic.test.TestQuestion;
import net.meyfa.statuscodestrainer.logic.test.TestResults;
import net.meyfa.statuscodestrainer.util.async.ActivityActionRunner;
import net.meyfa.statuscodestrainer.util.async.AsyncAction;
import net.meyfa.statuscodestrainer.util.async.Callback;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * "Test" activity.
 */
public class TestActivity extends AppCompatActivity
{
    private static final int QUESTION_COUNT = 20;

    private TestLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // configure action bar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // setup logic
        HTTPStatuses codes = HTTPStatuses.load(getResources(), R.xml.statuses);
        List<HTTPStatus> allStatuses = new ArrayList<>();
        for (HTTPStatusClass statusClass : codes.getClasses()) {
            allStatuses.addAll(statusClass.getStatuses());
        }
        logic = new TestLogic(allStatuses, QUESTION_COUNT);

        // setup input IME action
        EditText input = findViewById(R.id.input_answer);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView view, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    finishCurrentQuestion();
                    return true;
                }
                return false;
            }
        });

        // show first question
        showNextQuestion();
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

    /**
     * Click handler for the accept button.
     *
     * @param view The clicked view.
     */
    public void clickAccept(@SuppressWarnings("unused") View view)
    {
        finishCurrentQuestion();
    }

    /**
     * Enter the current answer, and either show the next question or go to the results.
     */
    private void finishCurrentQuestion()
    {
        EditText input = findViewById(R.id.input_answer);
        logic.enterAnswer(input.getText().toString());

        if (!logic.hasNextQuestion()) {
            showResults();
            return;
        }

        showNextQuestion();
    }

    /**
     * Fetch another question and display it.
     */
    private void showNextQuestion()
    {
        TestQuestion question = logic.nextQuestion();

        // update question
        TextView codeView = findViewById(R.id.label_code);
        codeView.setText(String.format(Locale.US, "%d", question.getCode()));

        // reset input
        EditText input = findViewById(R.id.input_answer);
        input.getText().clear();

        // update state display
        String template = getResources().getString(R.string.test_question_number);
        int questionNumber = logic.getQuestionIndex() + 1;
        int questionCount = logic.getQuestionCount();
        TextView stateDisplay = findViewById(R.id.label_question_number);
        stateDisplay.setText(String.format(template, questionNumber, questionCount));
    }

    /**
     * End this questionnaire. The answers are evaluated, stored, and then shown to the user.
     */
    private void showResults()
    {
        // evaluate the results
        final TestResults results = logic.evaluate();

        // persist the results
        persistResults(results, new Callback<Void>()
        {
            @Override
            public void done(Void result)
            {
                // show the results activity
                Intent launch = new Intent(TestActivity.this, TestResultsActivity.class);
                launch.putExtra(TestResultsActivity.INTENT_EXTRA_RESULTS, results);
                startActivity(launch);

                finish();
            }
        });
    }

    /**
     * Store the results. After this is done, the callback is invoked.
     *
     * @param results    The results to persist.
     * @param thenAction The callback to invoke after completion.
     */
    private void persistResults(@NonNull final TestResults results, @Nullable Callback<Void> thenAction)
    {
        new ActivityActionRunner<>(this, new AsyncAction<Void>()
        {
            @Override
            protected Void execute()
            {
                ProgressTracker tracker = ProgressTracker.getInstance(getApplicationContext());

                final DateTime dateTime = DateTime.now();
                final int questionCount = results.getQuestionCount();
                final int correctCount = results.getCorrectAnswerCount();
                tracker.createProgressItem(dateTime, questionCount, correctCount);

                return null;
            }
        }).run(thenAction);
    }
}
