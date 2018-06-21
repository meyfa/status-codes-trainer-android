package net.meyfa.statuscodestrainer.activities.reference;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.meyfa.statuscodestrainer.R;
import net.meyfa.statuscodestrainer.data.HTTPStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for recyclers that display {@link HTTPStatus} instances.
 */
public class ReferenceAdapter extends RecyclerView.Adapter<ReferenceAdapter.ViewHolder>
{
    private List<HTTPStatus> data;

    /**
     * @param statuses The statuses to display.
     */
    public ReferenceAdapter(List<HTTPStatus> statuses)
    {
        data = Collections.unmodifiableList(new ArrayList<>(statuses));
    }

    @NonNull
    @Override
    public ReferenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.reference_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReferenceAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.update(data.get(i));
    }

    @Override
    public int getItemCount()
    {
        return data.size();
    }

    /**
     * View holder class for adapter items.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView codeView;
        private TextView messageView;

        /**
         * @param itemView The reference_item view.
         */
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);

            this.codeView = itemView.findViewById(R.id.label_code);
            this.messageView = itemView.findViewById(R.id.label_message);
        }

        /**
         * Replaces the view's data with data from the given object.
         *
         * @param statusCode The data object.
         */
        public void update(HTTPStatus statusCode)
        {
            codeView.setText(String.format(Locale.US, "%d", statusCode.getCode()));
            messageView.setText(statusCode.getMessage());
        }
    }
}
