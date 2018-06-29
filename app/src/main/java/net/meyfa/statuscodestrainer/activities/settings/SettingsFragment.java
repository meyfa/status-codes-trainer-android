package net.meyfa.statuscodestrainer.activities.settings;

import android.content.DialogInterface;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.logic.progress.ProgressTracker;
import net.meyfa.statuscodestrainer.util.async.ActivityActionRunner;
import net.meyfa.statuscodestrainer.util.async.AsyncAction;
import net.meyfa.statuscodestrainer.util.async.Callback;

public class SettingsFragment extends PreferenceFragment
{
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // clear progress
        Preference clearProgress = findPreference(getString(R.string.preference_clear_progress));
        clearProgress.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                showClearConfirmation();
                return true;
            }
        });
    }

    private void showClearConfirmation()
    {
        new AlertDialog.Builder(getActivity())//
                .setTitle(R.string.clear_progress_title)//
                .setMessage(R.string.clear_progress_message)//
                .setIcon(android.R.drawable.ic_dialog_alert)//
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        clearProgress();
                    }
                })//
                .setNegativeButton(android.R.string.no, null)//
                .show();
    }

    private void clearProgress()
    {
        new ActivityActionRunner<>(getActivity(), new AsyncAction<Void>()
        {
            @Override
            protected Void execute()
            {
                ProgressTracker tracker = ProgressTracker.getInstance(getActivity().getApplicationContext());
                tracker.clearItems();

                return null;
            }
        }).run(new Callback<Void>()
        {
            @Override
            public void done(Void aVoid)
            {
                Toast.makeText(getActivity(), R.string.clear_progress_toast, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
