package com.francescobertamini.app_individuale.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveRacerReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        int champId = intent.getIntExtra("champId", 0);

        JsonObject championships = null;
        try {
            championships = new JsonExtractorChampionships(context).getJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < championships.get("campionati").getAsJsonArray().size(); i++) {
            if (championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString().equals(Integer.toString(champId))) {
                for (int e = 0; e < championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("piloti-iscritti").getAsJsonArray().size(); e++)
                    if (championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("piloti-iscritti").getAsJsonArray().get(e).getAsJsonObject().get("nome").getAsString().equals(name)) {
                        championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("piloti-iscritti").getAsJsonArray().remove(e);
                    }
            }
        }

        File file = new File(context.getFilesDir(), "campionati.json");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        try {
            bufferedWriter.write(championships.toString());
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
