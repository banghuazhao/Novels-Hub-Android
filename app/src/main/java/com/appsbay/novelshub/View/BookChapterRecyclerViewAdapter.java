package com.appsbay.novelshub.View;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Controller.BookChapterActivity;
import com.appsbay.novelshub.Controller.BookPagerActivity;
import com.appsbay.novelshub.Model.Book;
import com.appsbay.novelshub.Model.BookChapter;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BookChapterRecyclerViewAdapter extends RecyclerView.Adapter<BookChapterRecyclerViewAdapter.BookChapterRecyclerViewViewHolder> {

    Context context;
    ArrayList<BookChapter> bookChapters;
    Book book;

    public BookChapterRecyclerViewAdapter(Context context, ArrayList<BookChapter> bookChapters, Book book) {
        this.context = context;
        this.bookChapters = bookChapters;
        this.book = book;
    }

    @NonNull
    @Override
    public BookChapterRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book_chapter, parent, false);
        return new BookChapterRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookChapterRecyclerViewViewHolder holder, int position) {
        BookChapter bookChapter = bookChapters.get(position);

        if (bookChapter.getChapterNumberName().equals(bookChapter.getChapterName())) {
            int newChapterNumberName = position + 1;
            holder.bookNumber.setText(String.valueOf(newChapterNumberName));
        } else {
            holder.bookNumber.setText(bookChapter.getChapterNumberName());
        }
        holder.bookNumber.setTextColor(MyColor.getTitleTextColor(context));
        holder.bookName.setText(bookChapter.getChapterName());
        holder.bookName.setTextColor(MyColor.getTitleTextColor(context));

        SharedPreferences preferences = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        int bookmarkNumber = preferences.getInt(book.getName(), 0);
        if (position == bookmarkNumber) {
            holder.bookmark.setBackground(MyImage.changeDrawableColor(context, R.drawable.nav_bookmark, Color.RED));
        } else {
            holder.bookmark.setBackground(null);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt(book.getName(), position);
                editor.apply();
                notifyDataSetChanged();

                Intent intent = new Intent(context, BookPagerActivity.class);
                intent.putExtra("bookChapter", bookChapter);
                intent.putExtra("book", book);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookChapters.size();
    }

    public class BookChapterRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView bookNumber;
        TextView bookName;
        Button bookmark;

        public BookChapterRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            bookNumber = itemView.findViewById(R.id.book_chapter_number);
            bookName = itemView.findViewById(R.id.book_chapter_name);
            bookmark = itemView.findViewById(R.id.book_chapter_bookmark);
        }
    }

}