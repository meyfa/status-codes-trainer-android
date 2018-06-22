package net.meyfa.statuscodestrainer.activities.training;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.data.HTTPStatusClass;
import net.meyfa.statuscodestrainer.data.HTTPStatuses;
import net.meyfa.statuscodestrainer.logic.TrainingLogic;
import net.meyfa.statuscodestrainer.logic.TrainingQuestion;

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
    private TrainingQuestion currentQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training);

        // configure action bar
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

    private void chooseAnswer(int index)
    {
        int correctIndex = currentQuestion.getCorrectAnswerIndex();
        if (correctIndex != index) {
            Button view = findViewById(ANSWER_BUTTONS.get(index));
            view.setBackgroundResource(R.drawable.button_incorrect);
        }
        Button correctView = findViewById(ANSWER_BUTTONS.get(correctIndex));
        correctView.setBackgroundResource(R.drawable.button_correct);

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
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

    private void showNextQuestion()
    {
        if (!logic.hasNextQuestion()) {
            // TODO handle case of no more questions
            return;
        }

        currentQuestion = logic.nextQuestion();

        // update question
        TextView codeView = findViewById(R.id.label_code);
        codeView.setText(String.format(Locale.US, "%d", currentQuestion.getCode()));

        // update answers
        List<String> answers = currentQuestion.getAnswers();
        for (int i = 0; i < answers.size(); ++i) {
            Button btn = findViewById(ANSWER_BUTTONS.get(i));
            btn.setText(answers.get(i));
            btn.setBackgroundResource(R.drawable.button_default);
        }
    }
}
