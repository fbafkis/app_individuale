package com.example.app_individuale.data_managing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ChampionshipsAdapter extends RecyclerView.Adapter<ChampionshipsAdapter.ViewHolder> {

    JsonArray campionati;
    Context context;


    public ChampionshipsAdapter(JsonArray campionati, Context context) {
        this.campionati = campionati;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView numberListTextView;
        public ImageView logo;

        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.championship_list_name);
            numberListTextView = itemView.findViewById(R.id.championship_list_number);
            logo = itemView.findViewById(R.id.championship_list_logo);

        }
    }


    @NonNull

    @Override
    public ChampionshipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ChampionshipsAdapter.ViewHolder holder, int position) {

        JsonObject campionato = campionati.get(position).getAsJsonObject();

        TextView nameTextView = holder.nameTextView;
        TextView numberListTextView = holder.numberListTextView;
        ImageView logo = holder.logo;

        nameTextView.setText(campionato.get("nome").getAsString());
        numberListTextView.setText(campionato.get("id").getAsString());

        String wrong_res_name = campionato.get("logo").getAsString();
        wrong_res_name =  wrong_res_name.replaceAll("-","_");

        String logo_res = wrong_res_name.substring(0,wrong_res_name.indexOf("."));

        int logo_drawable_id = context.getResources().getIdentifier( logo_res, "drawable", context.getPackageName());


        logo.setImageDrawable(context.getResources().getDrawable(logo_drawable_id));

    }

    @Override
    public int getItemCount() {

        if (campionati.size() == 0)
            return 0;
        else return campionati.size();

    }


}
