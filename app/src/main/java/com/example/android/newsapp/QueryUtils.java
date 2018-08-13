package com.example.android.newsapp;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Helper methods related to requesting and receiving news data from The Guardian.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(Context context, String webUrl) {

        Log.i(LOG_TAG, "TEST: extractNews() has been called");

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.
            JSONObject baseJsonObject = new JSONObject(fetchNewsData(webUrl));
            JSONObject responseObject = baseJsonObject.getJSONObject("response");
            JSONArray newsArray = responseObject.getJSONArray("results");
            if (newsArray != null && newsArray.length() > 0) {
                for (int i = 0; i < newsArray.length(); i++) {
                    JSONObject newsObject = newsArray.getJSONObject(i);

                    JSONObject fields = newsObject.getJSONObject("fields");
                    if (fields != null && fields.length() > 0) {
                        String headline = fields.getString("headline");
                        String section = newsObject.getString("sectionName");


                        // Set a default story body in case there isn't a link in the JSON response
                        String body = "";
                        try {
                            body = fromHtml(fields.getString("body")).toString();
                        } catch (Exception e) {
                            Log.e("QueryUtils", "Problem getting body in the news JSON results", e);
                        }

                        // Set a default byline in case there isn't a link in the JSON response
                        String byline = "No Byline";
                        try {
                            byline = fields.getString("byline");
                        } catch (Exception e) {
                            Log.e("QueryUtils", "Problem getting byline in the news JSON results", e);
                        }

                        // Set a default publication date in case there isn't a link in the JSON response
                        String publicationDate = "";
                        try {
                            publicationDate = newsObject.getString("webPublicationDate");
                        } catch (Exception e) {
                            Log.e("QueryUtils", "Problem getting publication date in the news JSON results", e);
                        }

                        // Set a default drawable for the thumbnail in case there isn't a link in the JSON response
                        Drawable thumbnail = ResourcesCompat.getDrawable(context.getResources(),R.drawable.placeholder,null);

                        try {
                            String thumbnailUrl = fields.getString("thumbnail");
                            thumbnail = LoadImageFromWebOperations(thumbnailUrl);
                        } catch (Exception e) {
                            Log.e("QueryUtils", "Problem getting thumbnail in the news JSON results", e);
                        }

                        String url = newsObject.getString("webUrl");

                        news.add(new News(headline, body, section, byline, publicationDate, thumbnail, url));
                    }
                }
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the news JSON results", e);
        }

        // Return the list of news
        return news;
    }

    /**
     * Query the The Guardian dataset and return an {@link News} object to represent a single news.
     */
    public static String fetchNewsData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return jsonResponse;
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

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
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

    /**
     * get an image from the submitted URL (@param url)
     *
     * @return a Drawable
     */
    private static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

    @SuppressWarnings("deprecation")
    private static Spanned fromHtml(String html){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(html);
        }
    }

}