package com.appsbay.novelshub.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class BookChapter implements Parcelable {
    int chapterNumber;
    String chapterNumberName;
    String chapterName;
    String text;

    public BookChapter(String chapterNumberName, String chapterName, String chapterText) {
        this.chapterNumberName = chapterNumberName;
        this.chapterName = chapterName;
        this.text = chapterText;
    }

    public int getChapterNumber() {
        return chapterNumber;
    }

    public void setChapterNumber(int chapterNumber) {
        this.chapterNumber = chapterNumber;
    }

    public String getChapterNumberName() {
        return chapterNumberName;
    }

    public void setChapterNumberName(String chapterNumberName) {
        this.chapterNumberName = chapterNumberName;
    }

    public String getChapterName() {
        return chapterName;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    protected BookChapter(Parcel in) {
        chapterNumber = in.readInt();
        chapterNumberName = in.readString();
        chapterName = in.readString();
        text = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(chapterNumber);
        dest.writeString(chapterNumberName);
        dest.writeString(chapterName);
        dest.writeString(text);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<BookChapter> CREATOR = new Parcelable.Creator<BookChapter>() {
        @Override
        public BookChapter createFromParcel(Parcel in) {
            return new BookChapter(in);
        }

        @Override
        public BookChapter[] newArray(int size) {
            return new BookChapter[size];
        }
    };
}