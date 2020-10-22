package com.francescobertamini.app_individuale.data_managing;

import android.content.Context;
import android.os.Environment;

import com.francescobertamini.app_individuale.R;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public class JsonExtractorStandings {
    JsonObject jsonObjectFile;
    JsonArray championships;
    Context context;

    public JsonExtractorStandings(Context current) {
        this.context = current;
    }

    public JsonArray getJsonArray() throws IOException {
        championships = getJsonObject().get("campionati").getAsJsonArray();
        return championships;
    }

    public JsonObject getJsonObject() throws IOException {
        File file = new File(context.getFilesDir(), "classifiche.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null) {
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        String jsonString = stringBuilder.toString();
        jsonObjectFile = JsonParser.parseString(jsonString).getAsJsonObject();
        return jsonObjectFile;
    }

    public void copyFile() throws IOException {
        InputStream is = context.getAssets().open("classifiche.json");
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
        jsonObjectFile = JsonParser.parseString(jsonString).getAsJsonObject();
        File file = new File(context.getFilesDir(), "classifiche.json");
        FileWriter fileWriter = new FileWriter(file);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(jsonString);
        bufferedWriter.close();
    }
}
