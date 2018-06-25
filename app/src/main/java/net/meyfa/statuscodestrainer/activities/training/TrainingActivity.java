package net.meyfa.statuscodestrainer.activities.training;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.data.HTTPStatusClass;
import net.meyfa.statuscodestrainer.data.HTTPStatuses;
import net.meyfa.statuscodestrainer.logic.training.TrainingLogic;
import net.meyfa.statuscodestrainer.logic.training.TrainingQuestion;
import net.meyfa.statuscodestrainer.logic.training.TrainingResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * "Training" activity.
 */
public class TrainingActivity extends AppCompatActivity
{
    /**
     * Read-only list of the answer button ids, in order of appearance.
     */
    private static final List<Integer> ANSWER_BUTTONS = Collections
            .unmodifiableList(Arrays.asList(R.id.btn_answer_0, R.id.btn_answer_1, R.id.btn_answer_2));

    private TrainingLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

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
        logic = new TrainingLogic(allStatuses);

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
     * Click handler for the answer buttons.
     *
     * @param view The clicked view.
     */
    public void clickAnswerButton(View view)
    {
        for (int i = 0; i < ANSWER_BUTTONS.size(); ++i) {
            if (view.getId() == ANSWER_BUTTONS.get(i)) {
                chooseAnswer(i);
            }
        }
    }

    /**
     * Handle the given answer choice.
     *
     * @param index The index of the answer that was chosen.
     */
    private void chooseAnswer(int index)
    {
        final TrainingResponse response = logic.chooseAnswer(index);
        if (response == null) {
            return;
        }

        // update button styles
        setAnswerButtonState(response.getChosenAnswerIndex(), AnswerButtonState.INCORRECT);
        setAnswerButtonState(response.getCorrectAnswerIndex(), AnswerButtonState.CORRECT);

        // display result label
        int labelId = response.isCorrect() ? R.id.label_correct : R.id.label_incorrect;
        findViewById(labelId).setVisibility(View.VISIBLE);

        // continue after some time
        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(response.isCorrect() ? 1200 : 2400);
                } catch (InterruptedException ignored) {
                }

                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        showNextQuestion();
                    }
                });
            }
        }).start();
    }

    /**
     * Set the display state of an answer button.
     *
     * @param index The button index (0, 1, 2).
     * @param state The state (default, correct, incorrect).
     */
    private void setAnswerButtonState(int index, @NonNull AnswerButtonState state)
    {
        Button view = findViewById(ANSWER_BUTTONS.get(index));

        switch (state) {
            case DEFAULT:
                view.setBackgroundResource(R.drawable.button_default);
                break;
            case CORRECT:
                view.setBackgroundResource(R.drawable.button_correct);
                break;
            case INCORRECT:
                view.setBackgroundResource(R.drawable.button_incorrect);
                break;
        }
    }

    /**
     * Fetch another question and display it.
     */
    private void showNextQuestion()
    {
        TrainingQuestion question = logic.nextQuestion();

        // update question
        TextView codeView = findViewById(R.id.label_code);
        codeView.setText(String.format(Locale.US, "%d", question.getCode()));

        // update answers
        List<String> answers = question.getAnswers();
        for (int i = 0; i < answers.size(); ++i) {
            Button btn = findViewById(ANSWER_BUTTONS.get(i));
            btn.setText(answers.get(i));
            setAnswerButtonState(i, AnswerButtonState.DEFAULT);
        }

        // hide result labels
        findViewById(R.id.label_correct).setVisibility(View.GONE);
        findViewById(R.id.label_incorrect).setVisibility(View.GONE);
    }

    /**
     * State of an answer button.
     */
    private enum AnswerButtonState
    {
        DEFAULT,
        CORRECT,
        INCORRECT
    }
}
