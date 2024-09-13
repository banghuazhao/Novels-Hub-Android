package com.appsbay.novelshub.Model;

import java.util.ArrayList;

public class BookCategoryStore {
    public static BookCategoryStore shared = new BookCategoryStore();

    public ArrayList<BookCategory> getCategories() {

        ArrayList<BookCategory> categories = new ArrayList<>();

        ArrayList<Book> book_ROMANCE_HISTORYICAL = new ArrayList<>();
        ArrayList<Book> book_realisticAndSocialFiction = new ArrayList<>();
        ArrayList<Book> book_FANTASY_ADVENTURE = new ArrayList<>();
        ArrayList<Book> book_GOTHIC_PSYCHOLOGICAL = new ArrayList<>();
        ArrayList<Book> book_CRIME_DETECTIVE = new ArrayList<>();

        for (Book book : BookStore.shared.books) {
            if (book.getBookType() == BookType.romanceAndHistoricalFiction) {
                book_ROMANCE_HISTORYICAL.add(book);
            } else if (book.getBookType() == BookType.realisticAndSocialFiction) {
                book_realisticAndSocialFiction.add(book);
            } else if (book.getBookType() == BookType.fantasyAdventureFiction) {
                book_FANTASY_ADVENTURE.add(book);
            } else if (book.getBookType() == BookType.gothicPsychologicalFiction) {
                book_GOTHIC_PSYCHOLOGICAL.add(book);
            } else if (book.getBookType() == BookType.crimeAndDetectiveFiction) {
                book_CRIME_DETECTIVE.add(book);
            }
        }

        categories.add(new BookCategory("Romance & Historical Fiction", book_ROMANCE_HISTORYICAL));
        categories.add(new BookCategory("Realistic & Social Fiction", book_realisticAndSocialFiction));
        categories.add(new BookCategory("Fantasy & Adventure Fiction", book_FANTASY_ADVENTURE));
        categories.add(new BookCategory("Gothic & Psychological Fiction", book_GOTHIC_PSYCHOLOGICAL));
        categories.add(new BookCategory("Crime & Detective Fiction", book_CRIME_DETECTIVE));

        return categories;
    }

    public void setCategories(ArrayList<BookCategory> categories) {
        this.categories = categories;
    }

    public ArrayList<BookCategory> categories;

}
