package com.example.android.newsapp;

import android.graphics.drawable.Drawable;

public class News {
    private String mHeadline;
    private String mBody;
    private String mSection;
    private String mByline;
    private String mPublicationDate;
    private Drawable mThumbnail;
    private String mUrl;


    public News(String headline, String body, String section, String byline, String publicationDate, Drawable thumbnail, String url) {
        mHeadline = headline;
        mBody = body;
        mSection = section;
        mByline = byline;
        mPublicationDate = publicationDate;
        mThumbnail = thumbnail;
        mUrl = url;
    }

    public String getHeadline() {
        return mHeadline;
    }

    public String getBody() {
        return mBody;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getSection() {
        return mSection;
    }

    public String getByline() {
        return mByline;
    }

    public Drawable getThumbnail() {
        return mThumbnail;
    }

    public String getUrl() {
        return mUrl;
    }

    @Override
    public String toString() {
        return "News{" +
                "mHeadline = " + mHeadline +
                ", mBody = "+ mBody +
                ", mSection = " + mSection +
                ", mByline = " + mByline +
                ", mPublicationDate = " + mPublicationDate +
                ", mThumbnail = "+ mThumbnail +
                ", mUrl = "+ mUrl +
                "}";
    }
}
