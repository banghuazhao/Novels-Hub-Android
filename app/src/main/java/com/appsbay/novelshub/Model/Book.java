package com.appsbay.novelshub.Model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

enum BookType {
    romanceAndHistoricalFiction,
    realisticAndSocialFiction,
    fantasyAdventureFiction,
    gothicPsychologicalFiction,
    crimeAndDetectiveFiction
}

public class Book implements Parcelable {
    String name;
    String author;
    String fileName;
    String bookCover;
    BookType bookType;
    boolean isOnline;

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    boolean isCollection;
    String parent;


    public Book(JsonObject json) {
        this.name = json.get("name").getAsString();
        this.author = json.get("author").getAsString();
        String bookType = json.get("bookType").getAsString();
        if (bookType.equals("romanceAndHistoricalFiction")) {
            this.bookType = BookType.romanceAndHistoricalFiction;
        } else if (bookType.equals("realisticAndSocialFiction")) {
            this.bookType = BookType.realisticAndSocialFiction;
        } else if (bookType.equals("fantasyAdventureFiction")) {
            this.bookType = BookType.fantasyAdventureFiction;
        } else if (bookType.equals("gothicPsychologicalFiction")) {
            this.bookType = BookType.gothicPsychologicalFiction;
        } else {
            this.bookType = BookType.crimeAndDetectiveFiction;
        }
        this.bookCover = json.get("bookCover").getAsString();
        this.fileName = json.get("fileName").getAsString();
        if (json.get("isOnline") != null) {
            this.isOnline = json.get("isOnline").getAsBoolean();
            Log.d("myTag", fileName);
            Log.d("myTag", String.valueOf(isOnline));
        }
        if (json.get("isCollection") != null) {
            this.isCollection = json.get("isCollection").getAsBoolean();
        }
        if (json.get("parent") != null) {
            this.parent = json.get("parent").getAsString();
        }

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getBookCover() {
        return bookCover;
    }

    public void setBookCover(String bookCover) {
        this.bookCover = bookCover;
    }

    public BookType getBookType() {
        return bookType;
    }

    public void setBookType(BookType bookType) {
        this.bookType = bookType;
    }


    protected Book(Parcel in) {
        name = in.readString();
        author = in.readString();
        fileName = in.readString();
        bookCover = in.readString();
        bookType = (BookType) in.readValue(BookType.class.getClassLoader());
        isOnline = in.readByte() != 0;
        isCollection = in.readByte() != 0;
        parent = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(fileName);
        dest.writeString(bookCover);
        dest.writeValue(bookType);
        dest.writeByte((byte) (isOnline ? 1 : 0));
        dest.writeByte((byte) (isCollection ? 1 : 0));
        dest.writeString(parent);
    }

}

