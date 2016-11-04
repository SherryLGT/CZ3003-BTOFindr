package com.btofindr.controller;

import android.content.Context;

import com.btofindr.model.Block;
import com.btofindr.model.Unit;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contain methods that are commonly used in the program.
 *
 * @author Sherry Lau Geok Teng
 * @version 1.0
 * @since 31/08/2016
 */

public class Utility {
    // Web service url
    final static String API_URL = "http://btofindr.cczy.io/api/";

    // Convert dp to pixel
    public static int getPixels(int dp, float scale) {
        return ((int) (dp * scale + 0.5f));
    }

    // Format ethnic variable for parsing
    public static char setEthic(String ethic) {
        char character = '-';

        switch (ethic) {
            case "Chinese" :
                character =  'C';
                break;
            case "Malay" :
                character = 'M';
                break;
            case "Indian/Others" :
                character = 'O';
                break;
            default :
                break;
        }

        return character;
    }

    // Convert double into string with "thousands" (,) delimiter
    public static String formatPrice(Double value) {
        return String.format("%,d", value.intValue());
    }

    // Get request from api (getURL), returns json result
    public static String getRequest(String getURL) {
        try {
            URL url = new URL(API_URL + getURL.replace(" ","%20"));
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

    // Post request with json parameter (para)to api (postURL), returns json result
    public static String postRequest(String postURL, String para) {
        try {
            URL url = new URL(API_URL + postURL);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            try {
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Content-Type", "application/json");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setChunkedStreamingMode(0);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                DataOutputStream dStream = new DataOutputStream(out);
                dStream.writeBytes(para);
                dStream.flush();
                dStream.close();

                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
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

    // write to specific json data file in local storage
    public static boolean writeToFile(String filename, String data, Context mContext) {
        if (!data.equals("")) {
            try {
                FileOutputStream fos = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
                fos.write(data.getBytes());
                fos.close();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    // get specified json data file in local storage, return json result
    public static String readFromFile(String filename, Context mContext) {
        String json = "";
        try {
            InputStream in = mContext.openFileInput(filename);

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder out = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                out.append(line);
            }
            json = out.toString();
            reader.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }
}
