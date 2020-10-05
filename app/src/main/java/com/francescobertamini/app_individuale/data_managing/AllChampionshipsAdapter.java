package com.francescobertamini.app_individuale.data_managing;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class AllChampionshipsAdapter extends RecyclerView.Adapter<AllChampionshipsAdapter.ViewHolder> {

    JsonArray championships;
    Context context;


    public AllChampionshipsAdapter(JsonArray championships, Context context) {
        this.championships = championships;
        this.context = context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView numberListTextView;
        public ImageView logo;

        public ViewHolder(View itemView) {

            super(itemView);

            nameTextView = itemView.findViewById(R.id.champListItemName);
            numberListTextView = itemView.findViewById(R.id.champListItemNumber);
            logo = itemView.findViewById(R.id.champListItemLogo);

        }
    }


    @NonNull

    @Override
    public AllChampionshipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_championships, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AllChampionshipsAdapter.ViewHolder holder, int position) {

        JsonObject campionato = championships.get(position).getAsJsonObject();

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

        if (championships.size() == 0)
            return 0;
        else return championships.size();

    }

    public JsonObject getItem (int position){
        return championships.get(position).getAsJsonObject();
    }


}
