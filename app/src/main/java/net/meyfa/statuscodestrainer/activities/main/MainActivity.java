package net.meyfa.statuscodestrainer.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.activities.reference.ReferenceActivity;
import net.meyfa.statuscodestrainer.activities.training.TrainingActivity;

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
    }

    /**
     * Click handler for the "Reference" button.
     *
     * @param view The clicked view.
     */
    public void clickReference(View view)
    {
        Intent launch = new Intent(this, ReferenceActivity.class);
        startActivity(launch);
    }

    /**
     * Click handler for the "Training" button.
     *
     * @param view The clicked view.
     */
    public void clickTraining(View view)
    {
        Intent launch = new Intent(this, TrainingActivity.class);
        startActivity(launch);
    }

    /**
     * Click handler for the "Test" button.
     *
     * @param view The clicked view.
     */
    public void clickTest(View view)
    {

    }
}
