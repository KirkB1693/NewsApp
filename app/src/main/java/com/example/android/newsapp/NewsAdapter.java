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

    // Implement a ViewHolder class to store views and promote efficiency in the ListView when recycling views
    static class ViewHolder {
        TextView headline;
        ImageView thumbnail;
        TextView author;
        TextView body;
        TextView section;
        TextView publicationDate;
    }


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
        ViewHolder holder;

        // Check if the existing view is being reused, otherwise inflate the view
        // also use a ViewHolder to optimize the ListView
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list, parent, false);
            holder = new ViewHolder();
            holder.headline = listItemView.findViewById(R.id.news_headline);
            holder.thumbnail = listItemView.findViewById(R.id.news_thumbnail);
            holder.author = listItemView.findViewById(R.id.news_byline);
            holder.body = listItemView.findViewById(R.id.news_blurb);
            holder.section = listItemView.findViewById(R.id.news_section);
            holder.publicationDate = listItemView.findViewById(R.id.news_publication_date);
            listItemView.setTag(holder);
        }
        else {
            holder = (ViewHolder) listItemView.getTag();
        }

        // Get the {@link News} object located at this position in the list
        News currentNews = getItem(position);

        // Get the headline from the current News object and
        // set this text on the news_headline TextView
        holder.headline.setText(currentNews.getHeadline());


        // Get the drawable from the current news object and
        // set this image on the news_thumbnail ImageView
        holder.thumbnail.setImageDrawable(currentNews.getThumbnail());


        // Get the byline from the current News object
        // set this text on the news_byline TextView
        holder.author.setText(currentNews.getAuthor());


        // Get the body of the article from the current News object
        // set this text on the news_blurb TextView
        holder.body.setText(currentNews.getBody());

        // Get the news section from the current News object and
        // set this text on the news_section TextView
        holder.section.setText(currentNews.getSection());


        // Get the publication date from the current news object
        String rawPublicationDate = currentNews.getPublicationDate();
        // Transform the publication date from the DateTime format received in JSON to a String with the desired date format
        Date date = StringToDate(rawPublicationDate);
        SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        // Set the formatted date string on the news_publication_date TextView
        holder.publicationDate.setText(formatter.format(date));



        /*
        // Return the whole list item layout (containing 5 TextViews and 1 ImageView)
        // so that it can be shown in the ListView
        */
        return listItemView;
    }

    public Date StringToDate(String rawStringDate) {

        Date date = new Date();
        // (1) create a SimpleDateFormat object with the desired format.
        // this is the format/pattern we're expecting to receive. 2018-07-30T23:54:39Z
        String expectedPattern = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        SimpleDateFormat formatter = new SimpleDateFormat(expectedPattern, Locale.US);
        try {
            // (2) give the formatter a String that matches the SimpleDateFormat pattern
            date = formatter.parse(rawStringDate);

            // returns the date
            return date;
        } catch (ParseException e) {
            // execution will come here if the String that is given
            // does not match the expected format.
            e.printStackTrace();
        }
        return date;
    }


}

