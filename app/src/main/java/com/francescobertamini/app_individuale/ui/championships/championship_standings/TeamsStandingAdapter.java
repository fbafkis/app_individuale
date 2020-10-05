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

        public TextView standing;
        public TextView name;
        public TextView car;

        public TextView score;






        public ViewHolder(View itemView) {

            super(itemView);

            standing = itemView.findViewById(R.id.champStandTeamListItemStanding);
            name = itemView.findViewById(R.id.champStandTeamListItemName);
            car = itemView.findViewById(R.id.champStandTeamListItemCar);
            score = itemView.findViewById(R.id.champStandTeamListItemScore);

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


        TextView standing = holder.standing;
        TextView name = holder.name;
        TextView car = holder.car;
        TextView score = holder.score;


        JsonObject event = teamsStandings.get(position).getAsJsonObject();

        standing.setText(Integer.toString(position+1));
        name.setText(event.get("team").getAsString());
        car.setText(event.get("auto").getAsString());
        score.setText(event.get("punti").getAsString());



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

