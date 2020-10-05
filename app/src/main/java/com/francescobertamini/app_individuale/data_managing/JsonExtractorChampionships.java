package com.francescobertamini.app_individuale.data_managing;

import android.content.Context;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JsonExtractorChampionships {

    JsonObject jsonObject;
    JsonArray championships;
    Context context;

    public JsonExtractorChampionships(Context current) {
        this.context = current;
    }

    public JsonArray readJson() throws IOException {
        InputStream is = context.getResources().openRawResource(R.raw.campionati);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            is.close();
        }
        String jsonString = writer.toString();
        jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
        championships = jsonObject.get("campionati").getAsJsonArray();
        return championships;
    }


    public JsonArray getCalendar(JsonObject campionato) {
        JsonArray calendar = campionato.get("calendario").getAsJsonArray();
        return calendar;
    }

    public JsonArray getSettings(JsonObject campionato) {
        JsonArray settings = campionato.get("impostazioni-gioco").getAsJsonArray();
        return settings;
    }

    public JsonArray getDrivers(JsonObject campionato) {
        JsonArray racers = campionato.get("piloti-iscritti").getAsJsonArray();
        return racers;
    }
}
