package com.francescobertamini.app_individuale.ui.championships.championship_standings;

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

public class TeamsStandingAdapter extends RecyclerView.Adapter<TeamsStandingAdapter.ViewHolder> {
    JsonArray teamsStandings;
    Context context;

    public TeamsStandingAdapter(JsonArray teamsStandings, Context context) {
        this.teamsStandings = teamsStandings;
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _standing;
        public TextView _name;
        public TextView _car;
        public TextView _score;

        public ViewHolder(View itemView) {
            super(itemView);
            _standing = itemView.findViewById(R.id.champStandTeamListItemStanding);
            _name = itemView.findViewById(R.id.champStandTeamListItemName);
            _car = itemView.findViewById(R.id.champStandTeamListItemCar);
            _score = itemView.findViewById(R.id.champStandTeamListItemScore);
        }
    }

    @NonNull
    @Override
    public TeamsStandingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_standings_teams, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeamsStandingAdapter.ViewHolder holder, int position) {
        TextView _standing = holder._standing;
        TextView _name = holder._name;
        TextView _car = holder._car;
        TextView _score = holder._score;
        JsonObject event = teamsStandings.get(position).getAsJsonObject();
        _standing.setText(Integer.toString(position + 1));
        _name.setText(event.get("team").getAsString());
        _car.setText(event.get("auto").getAsString());
        _score.setText(event.get("punti").getAsString());
    }

    @Override
    public int getItemCount() {
        if (teamsStandings.size() == 0)
            return 0;
        else return teamsStandings.size();
    }

    public JsonObject getItem(int position) {
        return teamsStandings.get(position).getAsJsonObject();
    }
}

