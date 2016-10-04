package com.btofindr.controller;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

/**
 * Created by Sherry on 31/08/2016.
 */

public class Utility {
    static String API_URL = "http://btofindr.cczy.io/api/";
    static String lala = "haha";
    // dp to pixel
    public static int getPixels(int dp, float scale) {
        return ((int) (dp * scale + 0.5f));
    }

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

    public static String formatPrice(Double value) {
        return String.format("%,d", value.intValue());
    }

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
