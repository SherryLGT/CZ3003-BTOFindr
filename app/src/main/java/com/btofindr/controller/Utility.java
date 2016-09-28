package com.btofindr.controller;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utility {
    static String API_URL = "http://btofindr.cczy.io/api/";

    public static String getRequest(String getURL) {
        try {
            URL url = new URL(API_URL + getURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    response.append(line);
                }
                bufferedReader.close();
                return response.toString();
            } finally {
                urlConnection.disconnect();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

//    public ArrayList<Block> postRequest(String postURL) {
//        try {
//            URL url = new URL(API_URL + postURL);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            try {
//                urlConnection.setRequestMethod("POST");
//                urlConnection.setRequestProperty("Content-Type", "application/json");
//                urlConnection.setDoInput(true);
//                urlConnection.setDoOutput(true);
//                urlConnection.setChunkedStreamingMode(0);
//
//                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
//                DataOutputStream dStream = new DataOutputStream(out);
//                JsonObject object = new JsonObject();
//                object.add("townNames", new Gson().toJsonTree(townNames));
//                object.add("ethnicGroup", new Gson().toJsonTree(ethic));
//                object.add("unitTypes", new Gson().toJsonTree(unitTypes));
//                object.add("maxPrice", new Gson().toJsonTree(maxPrice));
//                object.add("minPrice", new Gson().toJsonTree(minPrice));
//                object.add("orderBy", new Gson().toJsonTree("P"));
//                object.add("postalCode", new Gson().toJsonTree(""));
//                dStream.writeBytes(object.toString());
//
//                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
//                String line;
//                StringBuffer response = new StringBuffer();
//                while ((line = bufferedReader.readLine()) != null) {
//                    response.append(line);
//                }
//                bufferedReader.close();
//                TypeToken<List<Block>> token = new TypeToken<List<Block>>() {};
//                blockList = gson.fromJson(response.toString(), token.getType());
//
//                return blockList;
//            } finally {
//                urlConnection.disconnect();
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
}
