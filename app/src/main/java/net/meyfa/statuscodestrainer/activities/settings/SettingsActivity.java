package net.meyfa.statuscodestrainer.activities.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // load the fragment as the main content
        getFragmentManager().beginTransaction()//
                .replace(android.R.id.content, new SettingsFragment())//
                .commit();
    }
}
