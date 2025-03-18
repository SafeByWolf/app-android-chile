package com.safebywolf.safebywolf.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagenGenerica implements Parcelable{
    private String mUrl;
    private String mTitle;
    ImagenGenerica[] getSpacePhotos;

    public ImagenGenerica() {
    }

    public ImagenGenerica(String url, String title) {
        mUrl = url;
        mTitle = title;
    }

    protected ImagenGenerica(Parcel in) {
        mUrl = in.readString();
        mTitle = in.readString();
    }

    public ImagenGenerica(String mUrl, String mTitle, ImagenGenerica[] getSpacePhotos) {
        this.mUrl = mUrl;
        this.mTitle = mTitle;
        this.getSpacePhotos = getSpacePhotos;
    }

    public String getmUrl() {
        return mUrl;
    }

    public void setmUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public ImagenGenerica[] getGetSpacePhotos() {
        return getSpacePhotos;
    }

    public void setGetSpacePhotos(ImagenGenerica[] getSpacePhotos) {
        this.getSpacePhotos = getSpacePhotos;
    }

    public static Parcelable.Creator<ImagenGenerica> getCREATOR() {
        return CREATOR;
    }

    public static final Parcelable.Creator<ImagenGenerica> CREATOR = new Parcelable.Creator<ImagenGenerica>() {
        @Override
        public ImagenGenerica createFromParcel(Parcel in) {
            return new ImagenGenerica(in);
        }

        @Override
        public ImagenGenerica[] newArray(int size) {
            return new ImagenGenerica[size];
        }
    };

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mUrl);
        parcel.writeString(mTitle);
    }
}
