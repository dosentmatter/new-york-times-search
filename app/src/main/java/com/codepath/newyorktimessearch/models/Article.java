package com.codepath.newyorktimessearch.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by kevinlau on 10/23/16.
 */

public class Article implements Serializable {

    String webUrl;
    String headLine;
    String thumbnail;

    public Article(JSONObject jsonObject) throws JSONException {
        this.webUrl = jsonObject.getString("web_url");
        this.headLine = jsonObject.getJSONObject("headline").getString("main");

        JSONArray multimedia = jsonObject.getJSONArray("multimedia");
        if (multimedia.length() > 0) {
            JSONObject multimediaJson = multimedia.getJSONObject(0);
            final String MULTIMEDIA_PREFIX = "http://www.nytimes.com/";
            this.thumbnail = String.format("%s%s", MULTIMEDIA_PREFIX,
                                           multimediaJson.getString("url"));
        } else {
            this.thumbnail = "";
        }
    }

    public static ArrayList<Article> fromJSONArray(JSONArray array) {
        ArrayList<Article> results = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Article(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public String getHeadLine() {
        return headLine;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}
