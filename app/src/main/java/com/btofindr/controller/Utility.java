package com.btofindr.controller;

public interface Utility {
    String API_URL = "http://btofindr.cczy.io/api/";

//    public void postRequest(String postURL) {
//        try {
//            URL url = new URL(API_URL + postURL);
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//            try {
//                urlConnection.setDoOutput(true);
//                DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
//                outputStream.write();
//                outputStream.flush();
//                outputStream.close();
//            } finally {
//                urlConnection.disconnect();
//            }
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
