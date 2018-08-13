package com.example.android.newsapp;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NewsAdapter extends ArrayAdapter<News> {


    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context The current context. Used to inflate the layout file.
     * @param news    A List of News objects to display in a list
     */
    public NewsAdapter(Activity context, ArrayList<News> news) {
        super(context, 0, news);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The position in the list of data that should be displayed in the
     *                    list item view.
     * @param convertView The recycled view to populate.
     * @param parent      The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Find the TextView in the news_list.xml layout with the ID version_name
        TextView news_headline = (TextView) listItemView.findViewById(R.id.news_headline);
        // Get the headline from the current News object and
        // set this text on the news_headline TextView
        String headline = currentNews.getHeadline();
        news_headline.setText(headline);

        // Find the ImageView in the news_list.xml layout with the ID version_name
        ImageView thumbnail = (ImageView) listItemView.findViewById((R.id.news_thumbnail));
        // Get the drawable from the current news object and
        // set this image on the news_thumbnail ImageView
        thumbnail.setImageDrawable(currentNews.getThumbnail());

        // Find the TextView in the news_list.xml layout with the ID version_number
        TextView news_byline = (TextView) listItemView.findViewById(R.id.news_byline);
        // Get the byline from the current News object
        // set this text on the news_byline TextView
        news_byline.setText(currentNews.getByline());

        // Find the TextView in the news_list.xml layout with the ID version_number
        TextView news_blurb = (TextView) listItemView.findViewById(R.id.news_blurb);
        // Get the body of the article from the current News object
        // set this text on the news_blurb TextView
        news_blurb.setText(currentNews.getBody());

        // Find the TextView in the news_list.xml layout with the ID version_number
        TextView news_section = (TextView) listItemView.findViewById(R.id.news_section);
        // Get the news section from the current News object and
        // set this text on the news_section TextView
        news_section.setText(currentNews.getSection());

        // Find the TextView in the news_list.xml layout with the ID version_number
        TextView news_publication_date = (TextView) listItemView.findViewById(R.id.news_publication_date);
        // Get the publication date from the current news object
        String rawPublicationDate = currentNews.getPublicationDate();
        // Transform the publication date from the DateTime format received in JSON to a String with the desired date format
        Date date = StringToDate(rawPublicationDate);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        // Set the formatted date string on the news_publication_date TextView
        news_publication_date.setText(formatter.format(date));



        /*
        // Return the whole list item layout (containing 5 TextViews and 1 ImageView)
        // so that it can be shown in the ListView
        */
        return listItemView;
    }

    public Date StringToDate(String rawStringDate)
    {

        Date date = new Date();
        // (1) create a SimpleDateFormat object with the desired format.
        // this is the format/pattern we're expecting to receive. 2018-07-30T23:54:39Z
        String expectedPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.US);
        try
        {
            // (2) give the formatter a String that matches the SimpleDateFormat pattern
            date = formatter.parse(rawStringDate);

            // returns the date
            return date;
        }
        catch (ParseException e)
        {
            // execution will come here if the String that is given
            // does not match the expected format.
            e.printStackTrace();
        }
        return date;
    }


}