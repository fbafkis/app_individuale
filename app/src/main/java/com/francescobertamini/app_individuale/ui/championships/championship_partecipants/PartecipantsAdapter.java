package com.francescobertamini.app_individuale.ui.championships.championship_partecipants;

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

public class PartecipantsAdapter extends RecyclerView.Adapter<PartecipantsAdapter.ViewHolder> {
    JsonArray partecipants;
    Context context;

    public PartecipantsAdapter(JsonArray partecipants, Context context) {
        this.partecipants = partecipants;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _name;
        public TextView _initials;
        public TextView _team;
        public TextView _car;

        public ViewHolder(View itemView) {
            super(itemView);
            _initials = itemView.findViewById(R.id.champPartListItemInitials);
            _name = itemView.findViewById(R.id.champPartListItemName);
            _team = itemView.findViewById(R.id.champPartListItemTeam);
            _car = itemView.findViewById(R.id.champPartListItemCar);
        }
    }

    @NonNull
    @Override
    public PartecipantsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_partecipants, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PartecipantsAdapter.ViewHolder holder, int position) {
        TextView _initials = holder._initials;
        TextView _name = holder._name;
        TextView _team = holder._team;
        TextView _car = holder._car;
        JsonObject partecipant = partecipants.get(position).getAsJsonObject();
        String nameStr = partecipant.get("nome").getAsString();
        String initialsStr = "";
        int counter = 0;
        for (int i = 0; i < nameStr.length(); i++) {
            char letter = nameStr.charAt(i);
            if (i == 0)
                initialsStr += nameStr.charAt(i);
            if (letter == ' ' && counter < 1) {
                initialsStr += nameStr.charAt(i + 1);
                counter++;
            }
            _initials.setText(initialsStr);
            _name.setText(nameStr);
            _team.setText(partecipants.get(position).getAsJsonObject().get("team").getAsString());
            _car.setText(partecipant.get("auto").getAsString());
        }
    }

    @Override
    public int getItemCount() {
        if (partecipants.size() == 0)
            return 0;
        else return partecipants.size();
    }

    public JsonObject getItem(int position) {
        return partecipants.get(position).getAsJsonObject();
    }
}

