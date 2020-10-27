package com.francescobertamini.app_individuale.broadcast_receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.francescobertamini.app_individuale.data_managing.JsonExtractorChampionships;
import com.francescobertamini.app_individuale.data_managing.JsonExtractorStandings;
import com.google.gson.JsonObject;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ResetReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            new JsonExtractorChampionships(context).copyFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
