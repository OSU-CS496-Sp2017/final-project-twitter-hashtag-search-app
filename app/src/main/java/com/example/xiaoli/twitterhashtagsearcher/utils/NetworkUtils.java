package com.example.xiaoli.twitterhashtagsearcher.utils;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;


public class NetworkUtils {
    private static final String CONSUMER_KEY = "VI64zamyjWBEPFzbzZYFN9H4B";
    private static final String CONSUMER_SECRET = "tjLkVH6NeFZCtwpjMC6pDuzzN9T4paf9q9tZbdIpGsF9UWxAHo";
    private String TAG = "Auth";


    public static String doHTTPGet(String input_url) throws IOException{
        HttpsURLConnection connection = null;

        try{
            URL url = new URL(input_url);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("HOST", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Twitter hashtag searcher");
            connection.setRequestProperty("Authorization", "Bearer "+ requestBearerToken());
            connection.connect();
            return readResponse(connection);
        }catch (MalformedURLException e){
            throw new IOException("Invalid URL", e);
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
    }

    private static String encodeKeys(String CONSUMER_KEY, String CONSUMER_SECRET){
        try{
            String encodedConsumerKey = URLEncoder.encode(CONSUMER_KEY, "UTF-8");
            String encodedConsumerSecret = URLEncoder.encode(CONSUMER_SECRET, "UTF-8");

            String fullkey = encodedConsumerKey + ":" + encodedConsumerSecret;
            String encodedBytes = Base64.encodeToString(fullkey.getBytes(), Base64.NO_WRAP);
            Log.d("Auth", "Encoded Token: " + encodedBytes);
            return encodedBytes;
        }catch (UnsupportedEncodingException e){
            Log.e("encodeKeys","Failed to encode", e);
        }
        return null;
    }

    private static String requestBearerToken() throws IOException{
        HttpsURLConnection connection = null;
        String encodedCredentials = encodeKeys(CONSUMER_KEY,CONSUMER_SECRET);
        String token = "";
        Log.d("Auth2", "Encoded Credentials " + encodedCredentials);

        try {
            URL url = new URL("https://api.twitter.com/oauth2/token");
            connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Host", "api.twitter.com");
            connection.setRequestProperty("User-Agent", "Twitter hashtag searcher");
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            connection.setRequestProperty("Content-Length", "29");

            writeRequest(connection, "grant_type=client_credentials");

            String JsonString = readResponse(connection);

            Log.d("Read Response 2", "is:" + JsonString);

            JSONObject jsonObject = new JSONObject(JsonString);
            String tokenType = jsonObject.getString("token_type");
            token = jsonObject.getString("access_token");

            if(tokenType.equals("bearer") && token != null){
                return token;
            }

        }catch(MalformedURLException e){
            throw new IOException("Invalid URL", e);
        }catch(JSONException e){
            Log.e("Parsing", "Failed", e);
        }finally {
            if(connection != null){
                connection.disconnect();
            }
        }
        return null;
    }

    private static boolean writeRequest(HttpsURLConnection connection, String textbody){
        try{
            BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            wr.write(textbody);
            wr.flush();
            wr.close();

            return true;
        }catch(IOException e){
            return false;
        }
    }

    private static String readResponse(HttpsURLConnection connection){
        try{
            StringBuilder str = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            while((line = br.readLine())!= null){
                str.append(line + System.getProperty("line.separator"));
            }
            return str.toString();
        }catch (IOException e){
            Log.e("Read", "Failed");
            return new String();
        }
    }
}