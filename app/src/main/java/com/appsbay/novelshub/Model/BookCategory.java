package com.appsbay.novelshub.Model;

import java.util.ArrayList;

public class BookCategory {
    String categoryName;
    ArrayList<Book> books;

    public BookCategory(String categoryName, ArrayList<Book> books) {
        this.categoryName = categoryName;
        this.books = books;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Book> getBooks() {
        return books;
    }

    public void setBooks(ArrayList<Book> books) {
        this.books = books;
    }
}
