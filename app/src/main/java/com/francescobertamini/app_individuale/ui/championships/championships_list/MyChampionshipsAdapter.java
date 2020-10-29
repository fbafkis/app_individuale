package com.francescobertamini.app_individuale.ui.championships.championships_list;

import android.content.Context;
import android.util.Log;
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
import java.util.ArrayList;
import java.util.List;

public class MyChampionshipsAdapter extends RecyclerView.Adapter<MyChampionshipsAdapter.ViewHolder> {
    JsonArray championships;
    Context context;
    String nameLastname;
    List<JsonObject> relevantChampionships = new ArrayList<>();

    public MyChampionshipsAdapter(Context context, String nameLastname) throws IOException {
        this.championships = new JsonExtractorChampionships(context).getJsonArray();
        this.context = context;
        this.nameLastname = nameLastname;
        extractChampionships(championships);
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
    public MyChampionshipsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.list_item_championships, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    List<JsonObject> extractChampionships(JsonArray championships) {
        for (int e = 0; e < championships.size(); e++) {
            JsonObject championship = championships.get(e).getAsJsonObject();
            JsonArray racers = championship.getAsJsonArray("piloti-iscritti");
            for (int i = 0; i < racers.size(); i++) {
                JsonObject iscritto = racers.get(i).getAsJsonObject();
                if (iscritto.get("nome").getAsString().equals(nameLastname)) {
                    relevantChampionships.add(championship);
                }
            }
        }
        return relevantChampionships;
    }

    @Override
    public void onBindViewHolder(@NonNull MyChampionshipsAdapter.ViewHolder holder, int position) {
        TextView _champListItemName = holder._champListItemName;
        TextView _champListItemNumber = holder._champListItemNumber;
        ImageView _champListItemLogo = holder._champListItemLogo;
        _champListItemName.setText(relevantChampionships.get(position).get("nome").getAsString());
        _champListItemNumber.setText(relevantChampionships.get(position).get("id").getAsString());
        String wrongPictureFilName = relevantChampionships.get(position).get("logo").getAsString();
        wrongPictureFilName = wrongPictureFilName.replaceAll("-", "_");
        String _champListItemLogo_res = wrongPictureFilName.substring(0, wrongPictureFilName.indexOf("."));
        int _champListItemLogo_drawable_id = context.getResources().getIdentifier(_champListItemLogo_res, "drawable", context.getPackageName());
        _champListItemLogo.setImageDrawable(ResourcesCompat.getDrawable(context.getResources(), _champListItemLogo_drawable_id, null));
    }

    @Override
    public int getItemCount() {
        int itemCount = 0;
        for (int e = 0; e < championships.size(); e++) {
            JsonObject championship = championships.get(e).getAsJsonObject();
            JsonArray racers = championship.getAsJsonArray("piloti-iscritti");
            Boolean bool = false;
            for (int i = 0; i < racers.size(); i++) {
                JsonObject iscritto = racers.get(i).getAsJsonObject();
                if (iscritto.get("nome").getAsString().equals(nameLastname)) {
                    bool = true;
                }
            }
            if (bool)
                itemCount++;
        }
        return itemCount;
    }

    public JsonObject getItem(int position) {
        return relevantChampionships.get(position).getAsJsonObject();
    }
}