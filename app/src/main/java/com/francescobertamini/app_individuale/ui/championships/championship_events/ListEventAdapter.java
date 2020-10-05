package com.francescobertamini.app_individuale.ui.championships.championship_events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

    JsonArray events;
    Context context;


    public ListEventAdapter(JsonArray event, Context context) {
        this.events = event;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView seq;
        public TextView track_name;
        public TextView date;



        public ViewHolder(View itemView) {

            super(itemView);

            seq = itemView.findViewById(R.id.champEventListListItemSeqNum);
            track_name = itemView.findViewById(R.id.champEventListListItemName);
            date = itemView.findViewById(R.id.champEventListListItemDate);

        }
    }


    @NonNull

    @Override
    public ListEventAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_events_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ListEventAdapter.ViewHolder holder, int position) {


        TextView seq = holder.seq;
        TextView track_name = holder.track_name;
        TextView date = holder.date;


        JsonObject event = events.get(position).getAsJsonObject();

        seq.setText(event.get("seq").getAsString());
        track_name.setText(event.get("circuito").getAsString());
        date.setText(event.get("data").getAsString());


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

