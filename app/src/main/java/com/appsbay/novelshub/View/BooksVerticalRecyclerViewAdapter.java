package com.appsbay.novelshub.View;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Controller.BookChapterActivity;
import com.appsbay.novelshub.Controller.BooksListActivity;
import com.appsbay.novelshub.Model.Book;
import com.appsbay.novelshub.Model.BookCategory;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.MyColor;

import java.util.ArrayList;

public class BooksVerticalRecyclerViewAdapter extends RecyclerView.Adapter<BooksVerticalRecyclerViewAdapter.BooksVerticalRecyclerViewViewHolder> {

    Context context;
    ArrayList<BookCategory> bookCategories;

    public BooksVerticalRecyclerViewAdapter(Context context, ArrayList<BookCategory> bookCategories) {
        this.context = context;
        this.bookCategories = bookCategories;
    }

    @NonNull
    @Override
    public BooksVerticalRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book_category, parent, false);
        return new BooksVerticalRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksVerticalRecyclerViewViewHolder holder, int position) {
        BookCategory bookCategory = bookCategories.get(position);

        String title = bookCategory.getCategoryName();
        ArrayList<Book> books = bookCategory.getBooks();

        holder.chapterTitle.setText(title);

        holder.chapterTitle.setTextColor(MyColor.getTitleTextColor(context));
        holder.buttonMore.setTextColor(MyColor.getButtonTintColor(context));
        holder.separator.setBackgroundColor(MyColor.getSeparatorColor(context));

        holder.buttonMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BooksListActivity.class);
                intent.putParcelableArrayListExtra("books", books);
                context.startActivity(intent);
            }
        });

        holder.booksRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        BooksHorizontalRecyclerViewAdapter booksHorizontalRecyclerViewAdapter = new BooksHorizontalRecyclerViewAdapter(context, books);
        holder.booksRecyclerView.setAdapter(booksHorizontalRecyclerViewAdapter);
    }


    @Override
    public int getItemCount() {
        return bookCategories.size();
    }

    public class BooksVerticalRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView chapterTitle;
        RecyclerView booksRecyclerView;
        Button buttonMore;
        View separator;

        public BooksVerticalRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            chapterTitle = itemView.findViewById(R.id.book_category_title);
            booksRecyclerView = itemView.findViewById(R.id.book_category_recycler_view);
            buttonMore = itemView.findViewById(R.id.book_more);
            separator = itemView.findViewById(R.id.row_book_separator);
        }
    }

}
