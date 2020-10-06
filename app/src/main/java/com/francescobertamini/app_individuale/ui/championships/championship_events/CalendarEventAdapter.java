package com.francescobertamini.app_individuale.ui.championships.championship_events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.francescobertamini.app_individuale.R;

public class CalendarEventAdapter extends RecyclerView.Adapter<CalendarEventAdapter.ViewHolder> {
    JsonArray events;
    Context context;

    public CalendarEventAdapter(JsonArray events, Context context) {
        this.events = events;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _seq;
        public TextView _trackName;

        public ViewHolder(View itemView) {
            super(itemView);
            _seq = itemView.findViewById(R.id.champEventCalListItemSeqNum);
            _trackName = itemView.findViewById(R.id.champEventCalListItemName);
        }
    }

    @NonNull
    @Override
    public CalendarEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_events_calendar, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CalendarEventAdapter.ViewHolder holder, int position) {
        TextView _seq = holder._seq;
        TextView _trackName = holder._trackName;
        JsonObject event = events.get(position).getAsJsonObject();
        _seq.setText(event.get("seq").getAsString());
        _trackName.setText(event.get("circuito").getAsString());
    }

    @Override
    public int getItemCount() {
        if (events.size() == 0)
            return 0;
        else return events.size();
    }

    public JsonObject getItem(int position) {
        return events.get(position).getAsJsonObject();
    }
}

