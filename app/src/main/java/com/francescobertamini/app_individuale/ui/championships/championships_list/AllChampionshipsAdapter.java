package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.francescobertamini.app_individuale.R;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

public class AllChampionshipsAdapter extends RecyclerView.Adapter<AllChampionshipsAdapter.ViewHolder> {
    JsonArray championships;
    Context context;

    public AllChampionshipsAdapter(Context context) throws IOException {
        this.championships = new JsonExtractorChampionships(context).getJsonArray();
        this.context = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView _champListItemName;
        public TextView _champListItemNumber;
        public ImageView _champListItemLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            _champListItemName = itemView.findViewById(R.id.champListItemName);
            _champListItemNumber = itemView.findViewById(R.id.champListItemNumber);
            _champListItemLogo = itemView.findViewById(R.id.champListItemLogo);
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
        TextView nameTextView = holder._champListItemName;
        TextView numberListTextView = holder._champListItemNumber;
        ImageView logo = holder._champListItemLogo;
        nameTextView.setText(campionato.get("nome").getAsString());
        numberListTextView.setText(campionato.get("id").getAsString());
        String wrong_res_name = campionato.get("logo").getAsString();
        wrong_res_name = wrong_res_name.replaceAll("-", "_");
        String logo_res = wrong_res_name.substring(0, wrong_res_name.indexOf("."));
        int logo_drawable_id = context.getResources().getIdentifier(logo_res, "drawable", context.getPackageName());
        logo.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), logo_drawable_id, null));
    }

    @Override
    public int getItemCount() {
        if (championships.size() == 0)
            return 0;
        else return championships.size();
    }

    public JsonObject getItem(int position) {
        return championships.get(position).getAsJsonObject();
    }
}
