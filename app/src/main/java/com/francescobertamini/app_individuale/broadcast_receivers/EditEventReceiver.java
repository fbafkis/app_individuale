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

public class EditEventReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String seq = intent.getStringExtra("seq");
        String track = intent.getStringExtra("track");
        String date = intent.getStringExtra("date");
        int champId = intent.getIntExtra("champId", 1);
        JsonObject newEvent = new JsonObject();
        newEvent.addProperty("seq", seq);
        newEvent.addProperty("circuito", track);
        newEvent.addProperty("data", date);
        JsonObject championships = null;
        try {
            championships = new JsonExtractorChampionships(context).getJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < championships.get("campionati").getAsJsonArray().size(); i++) {
            if (championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString().equals(Integer.toString(champId))) {
                for (int e = 0; e < championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("calendario").getAsJsonArray().size(); e++) {
                    if (championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("calendario").getAsJsonArray().get(e).getAsJsonObject().get("seq").getAsString().equals(seq)) {
                        championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("calendario").getAsJsonArray().get(e).getAsJsonObject().addProperty("data", date);
                    }
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
