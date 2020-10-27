package com.francescobertamini.app_individuale.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class RemoveChampReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int champId = intent.getIntExtra("champId", 1);
        JsonObject championships = null;
        try {
            championships = new JsonExtractorChampionships(context).getJsonObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < championships.get("campionati").getAsJsonArray().size(); i++) {
            if (championships.get("campionati").getAsJsonArray().get(i).getAsJsonObject().get("id").getAsString().equals(Integer.toString(champId))) {
                championships.get("campionati").getAsJsonArray().remove(i);
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
