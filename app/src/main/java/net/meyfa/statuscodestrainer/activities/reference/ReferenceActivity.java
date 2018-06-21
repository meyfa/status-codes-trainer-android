package net.meyfa.statuscodestrainer.activities.reference;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        // setup recycler
        recyclerView = findViewById(R.id.reference_recycler);
        recyclerView.setHasFixedSize(true);

        // use linear layout
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // set adapter
        HTTPStatuses codes = HTTPStatuses.load(getResources(), R.xml.statuses);
        List<HTTPStatus> allStatuses = new ArrayList<>();
        for (HTTPStatusClass statusClass : codes.getClasses()) {
            allStatuses.addAll(statusClass.getStatuses());
        }
        adapter = new ReferenceAdapter(allStatuses);
        recyclerView.setAdapter(adapter);
    }
}
