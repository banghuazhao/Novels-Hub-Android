package com.appsbay.novelshub.Model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookStore {
    public static BookStore shared = new BookStore();
    public ArrayList<Book> books = new ArrayList<>();
    public ArrayList<Book> allBooks = new ArrayList<>();

    public ArrayList<Book> getBooks(Context context) {
        ArrayList<Book> books = BookStore.shared.books;
        return books;
    }

    public ArrayList<Book> getAllBooks(Context context) {
        ArrayList<Book> books = BookStore.shared.allBooks;
        return books;
    }

    public ArrayList<Book> getBooksForCollection(Book book) {
        ArrayList<Book> books = BookStore.shared.allBooks;
        ArrayList<Book> childBooks = new ArrayList<>();
        for (Book temp : books) {
            String parent = temp.parent;
            if (parent != null) {
                if (parent.equals(book.name)) {
                    childBooks.add(temp);
                }
            }
        }
        return childBooks;
    }

    public void fetchFromLocal(Context context) {
        String jsonString = loadJSONFromAsset(context);
        JsonArray jsonArray = JsonParser.parseString(jsonString).getAsJsonArray();
        for (JsonElement jsonObject: jsonArray) {
            Book book = new Book(jsonObject.getAsJsonObject());
            allBooks.add(book);
            if (book.parent == null) {
                books.add(book);
            }
        }
    }

    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("bookInfo.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
