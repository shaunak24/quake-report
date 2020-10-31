package com.a24.shaunak.quakereport;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.LinkedList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import static com.a24.shaunak.quakereport.EarthquakeActivity.LOG_TAG;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        List earthquake = extractFromJSON(jsonResponse);

        // Return the {@link Event}
        return earthquake;
    }

    public static List<Earthquake> extractFromJSON(String jsonResponse) {

        // Create an empty ArrayList that we can start adding earthquakes to
        List<Earthquake> earthquakes = new LinkedList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            JSONObject jsonObject = new JSONObject(jsonResponse);

            JSONArray features = jsonObject.optJSONArray("features");

            for (int i = 0 ; i< 10 ; i++) {

                JSONObject jsonObject1 = features.optJSONObject(i);
                JSONObject properties = jsonObject1.optJSONObject("properties");
                double magnitude = properties.optDouble("mag");
                String place = properties.optString("place");
                Long time = properties.optLong("time");
                String url = properties.optString("url");

                earthquakes.add(new Earthquake(magnitude , place , time , url));

            }

        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {

        String jsonResponse = "";
        if (url == null)
            return jsonResponse;

        HttpsURLConnection urlconnection = null;
        InputStream inputStream = null;
        try {
            urlconnection = (HttpsURLConnection) url.openConnection();
            urlconnection.setReadTimeout(10000);
            urlconnection.setConnectTimeout(15000);
            urlconnection.setRequestMethod("GET");
            urlconnection.connect();

            if(urlconnection.getResponseCode() == 200) {
                inputStream = urlconnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }
            else
                Log.e(LOG_TAG , "Error response code : " + urlconnection.getResponseCode());

        }catch (IOException e) {
            Log.e(LOG_TAG , "Problem retrieving the earthquake JSON results");
        }
        try {
            if (urlconnection != null)
                urlconnection.disconnect();
            if (inputStream != null)
                inputStream.close();
        }
        catch (IOException e) {
            Log.e(LOG_TAG , "IOException");
        }
         return jsonResponse;

    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

}