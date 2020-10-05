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

public class RacersStandingAdapter extends RecyclerView.Adapter<RacersStandingAdapter.ViewHolder> {

    JsonArray racerStandings;
    Context context;


    public RacersStandingAdapter(JsonArray racerStandings, Context context) {
        this.racerStandings = racerStandings;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView standing;
        public TextView name;
        public TextView team;
        public TextView car;
        public TextView score;




        public ViewHolder(View itemView) {

            super(itemView);

            standing = itemView.findViewById(R.id.champStandRacerListItemStanding);
            name = itemView.findViewById(R.id.champStandRacerListItemName);
            team = itemView.findViewById(R.id.champStandRacerListItemTeam);
            car = itemView.findViewById(R.id.champStandRacerListItemCar);
            score = itemView.findViewById(R.id.champStandRacerListItemScore);

        }
    }


    @NonNull

    @Override
    public RacersStandingAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_standings_racers, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RacersStandingAdapter.ViewHolder holder, int position) {


        TextView standing = holder.standing;
        TextView name = holder.name;
        TextView team = holder.team;
        TextView car = holder.car;
        TextView score = holder.score;


        JsonObject event = racerStandings.get(position).getAsJsonObject();

        standing.setText(Integer.toString(position+1));
        name.setText(event.get("nome").getAsString());
        team.setText(event.get("team").getAsString());
        car.setText(event.get("auto").getAsString());
        score.setText(event.get("punti").getAsString());



    }

    @Override
    public int getItemCount() {

        if (racerStandings.size() == 0)
            return 0;
        else return racerStandings.size();

    }

    public JsonObject getItem(int position) {
        return racerStandings.get(position).getAsJsonObject();
    }


}

