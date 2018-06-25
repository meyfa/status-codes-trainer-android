package net.meyfa.statuscodestrainer.activities.reference;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.data.HTTPStatus;
import net.meyfa.statuscodestrainer.data.HTTPStatusClass;
import net.meyfa.statuscodestrainer.data.HTTPStatuses;

import java.util.ArrayList;
import java.util.List;

/**
 * "Status Code Reference" activity.
 */
public class ReferenceActivity extends AppCompatActivity
{
    private HTTPStatuses codes;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        codes = HTTPStatuses.load(getResources(), R.xml.statuses);

        // setup recycler
        recyclerView = findViewById(R.id.reference_recycler);
        recyclerView.setHasFixedSize(true);

        // configure toolbar
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // configure tab bar
        TabLayout tabs = findViewById(R.id.reference_tabs);
        tabs.addOnTabSelectedListener(new TabSelectionListener());
        populateTabBar(tabs, codes.getClasses());
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

    private void populateTabBar(TabLayout tabs, List<HTTPStatusClass> classes)
    {
        tabs.addTab(tabs.newTab().setText("All").setTag(null));
        for (HTTPStatusClass statusClass : classes) {
            tabs.addTab(tabs.newTab().setText(statusClass.getDigit() + "XX").setTag(statusClass));
        }
    }

    private void showStatuses(@Nullable HTTPStatusClass statusClass)
    {
        List<HTTPStatus> display = (statusClass == null) ? getAllStatuses() : statusClass.getStatuses();

        ReferenceAdapter adapter = new ReferenceAdapter(display);
        recyclerView.setAdapter(adapter);
    }

    private List<HTTPStatus> getAllStatuses()
    {
        List<HTTPStatus> allStatuses = new ArrayList<>();
        for (HTTPStatusClass statusClass : codes.getClasses()) {
            allStatuses.addAll(statusClass.getStatuses());
        }
        return allStatuses;
    }

    /**
     * Manages the tab selection.
     */
    private class TabSelectionListener implements TabLayout.OnTabSelectedListener
    {
        @Override
        public void onTabSelected(TabLayout.Tab tab)
        {
            showStatuses((HTTPStatusClass) tab.getTag());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab)
        {
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab)
        {
        }
    }
}
