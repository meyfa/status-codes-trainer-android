package net.meyfa.statuscodestrainer.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.activities.reference.ReferenceActivity;
import net.meyfa.statuscodestrainer.activities.test.TestActivity;
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

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
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
}
