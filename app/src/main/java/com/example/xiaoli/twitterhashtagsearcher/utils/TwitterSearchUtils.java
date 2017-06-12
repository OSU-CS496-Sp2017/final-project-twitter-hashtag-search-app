package com.example.xiaoli.twitterhashtagsearcher.utils;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hessro on 4/25/17.
 */

public class TwitterSearchUtils {

    private final static String TWITTER_SEARCH_BASE_URL = "https://api.twitter.com/1.1/search/tweets.json";
    private final static String TWITTER_SEARCH_QUERY_PARAM = "q";
    private final static String TWITTER_SEARCH_RESULT_TYPE = "result_type";
    private final static String TWITTER_SEARCH_LANGUAGE_PARAM = "lang";
    private final static String TWITTER_SEARCH_COUNT = "count";
    private final static String TWITTER_SEARCH_LOCALE = "locale";


    public static class SearchResult implements Serializable {
        public static final String EXTRA_SEARCH_RESULT = "TwitterSearchUtils.SearchResult";
        public String Text;
        public String User_name;
        public String Time;
        public String Profile_image;
        public String Media_photo;
    }

    public static String buildGitHubSearchURL(String searchQuery) {

        if(!searchQuery.contains("#"))
            searchQuery = "#" + searchQuery;

        return Uri.parse(TWITTER_SEARCH_BASE_URL).buildUpon().
                appendQueryParameter(TWITTER_SEARCH_QUERY_PARAM, searchQuery).
                //appendQueryParameter(TWITTER_SEARCH_RESULT_TYPE, resultType).
                //appendQueryParameter(TWITTER_SEARCH_LANGUAGE_PARAM, language).
                //appendQueryParameter(TWITTER_SEARCH_COUNT, String.valueOf(count)).
                //appendQueryParameter(TWITTER_SEARCH_LOCALE, locale).
                        build().
                        toString();
    }

    public static ArrayList<SearchResult> parseGitHubSearchResultsJSON(String searchResultsJSON) {
        String photo_url;
        JSONObject entitiesObj;

        try {
            JSONObject searchResultsObj = new JSONObject(searchResultsJSON);
            JSONArray searchResultsItems = searchResultsObj.getJSONArray("statuses");

            ArrayList<SearchResult> searchResultsList = new ArrayList<SearchResult>();
            for (int i = 0; i < searchResultsItems.length(); i++) {
                SearchResult searchResult = new SearchResult();
                JSONObject searchResultItem = searchResultsItems.getJSONObject(i);
                searchResult.Time = searchResultItem.getString("created_at");
                searchResult.Text = searchResultItem.getString("text");
                searchResult.User_name = searchResultItem.getJSONObject("user").getString("screen_name");
                searchResult.Profile_image = searchResultItem.getJSONObject("user").getString("profile_image_url_https");
                entitiesObj = searchResultItem.getJSONObject("entities");
                if(entitiesObj.has("media"))
                    searchResult.Media_photo = entitiesObj.getJSONArray("media").getJSONObject(0).getString("media_url_https");
                searchResultsList.add(searchResult);
            }
            return searchResultsList;
        } catch (JSONException e) {
            return null;
        }
    }
}
