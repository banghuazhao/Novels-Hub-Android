package com.appsbay.novelshub.Model;

import android.content.Context;

import com.appsbay.novelshub.Tools.TinyDB;

import java.util.ArrayList;

public class BookLibrary {

    public static final String BookLibrary =  "BookLibrary";

    public static com.appsbay.novelshub.Model.BookLibrary shared = new com.appsbay.novelshub.Model.BookLibrary();

    void init() {}

    public boolean have(Context context, Book book) {

        TinyDB tinydb = new TinyDB(context);

        ArrayList<String> books = tinydb.getListString(BookLibrary);

        if (books.contains(book.name)) {
            return true;
        } else {
            return false;
        }
    }

    public void save(Context context, Book book) {
        TinyDB tinydb = new TinyDB(context);
        ArrayList<String> books = tinydb.getListString(BookLibrary);
        books.add(book.name);
        tinydb.putListString(BookLibrary, books);
    }

    public void remove(Context context, Book book) {
        TinyDB tinydb = new TinyDB(context);
        ArrayList<String> books = tinydb.getListString(BookLibrary);
        if (have(context, book)) {
            books.remove(book.name);
            tinydb.putListString(BookLibrary, books);
        }
    }

    public ArrayList<Book> books(Context context) {
        ArrayList<Book> books = new ArrayList<>();
        TinyDB tinydb = new TinyDB(context);
        ArrayList<String> bookStrings = tinydb.getListString(BookLibrary);

        for (String bookString: bookStrings) {
            for (Book book : BookStore.shared.getAllBooks(context)) {
                if (book.name.equals(bookString)) {
                    books.add(book);
                    break;
                }
            }
        }

        return books;
    }
}
