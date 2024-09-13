package com.appsbay.novelshub.View;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Controller.BookChapterActivity;
import com.appsbay.novelshub.Controller.BooksListActivity;
import com.appsbay.novelshub.Model.Book;
import com.appsbay.novelshub.Model.BookLibrary;
import com.appsbay.novelshub.Model.BookStore;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.Constants;
import com.appsbay.novelshub.Tools.MyColor;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BooksLibraryListRecyclerViewAdapter extends RecyclerView.Adapter<BooksLibraryListRecyclerViewAdapter.BooksListRecyclerViewAdapterViewHolder> implements Filterable {

    Context context;
    public ArrayList<Book> books;
    public List<Book> booksFull;
    String searchString;
    public Toast toast;

    public BooksLibraryListRecyclerViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
        booksFull = new ArrayList<>(books);
    }

    @NonNull
    @Override
    public BooksListRecyclerViewAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_book, parent, false);
        return new BooksListRecyclerViewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksListRecyclerViewAdapterViewHolder holder, int position) {
        Book book = books.get(position);

        String bookNameFull = book.getName();
        String bookAuthorFull = book.getAuthor();
        holder.bookName.setTextColor(MyColor.getTitleTextColor(context));
        holder.bookAuthor.setTextColor(MyColor.getDetailTextColor(context));
        holder.separator.setBackgroundColor(MyColor.getSeparatorColor(context));

        // highlight search text
        if (searchString != null && !searchString.isEmpty()) {
            int startPos = bookNameFull.toLowerCase(Locale.US).indexOf(searchString.toLowerCase(Locale.US));
            int endPos = startPos + searchString.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(bookNameFull);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.rgb(252,147,0)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.bookName.setText(spannable);
            } else {
                holder.bookName.setText(bookNameFull);
            }
        } else {
            holder.bookName.setText(bookNameFull);
        }

        // highlight search text
        if (searchString != null && !searchString.isEmpty()) {
            int startPos = bookAuthorFull.toLowerCase(Locale.US).indexOf(searchString.toLowerCase(Locale.US));
            int endPos = startPos + searchString.length();

            if (startPos != -1) {
                Spannable spannable = new SpannableString(bookAuthorFull);
                ColorStateList blueColor = new ColorStateList(new int[][]{new int[]{}}, new int[]{Color.rgb(252,147,0)});
                TextAppearanceSpan highlightSpan = new TextAppearanceSpan(null, Typeface.BOLD, -1, blueColor, null);
                spannable.setSpan(highlightSpan, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.bookAuthor.setText(spannable);
            } else {
                holder.bookAuthor.setText(bookAuthorFull);
            }
        } else {
            holder.bookAuthor.setText(bookAuthorFull);
        }

        try {
            // get input stream
            InputStream ims = context.getAssets().open("covers/" + book.getBookCover() + ".png");
            // load image as Drawable
            Drawable d = Drawable.createFromStream(ims, null);
            // set image to ImageView
            holder.bookImage.setImageDrawable(d);
            ims.close();
        } catch (IOException ex) {
            Picasso.get()
                    .load("https://dtv96jzsdft4e.cloudfront.net/NoverlsHubImages/" + book.getBookCover().replace(" ", "+") + ".png")
                    .placeholder(R.drawable.bg4)
                    .into(holder.bookImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (book.isCollection()) {
                    Intent intent = new Intent(context, BooksListActivity.class);
                    intent.putExtra("books", BookStore.shared.getBooksForCollection(book));
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, BookChapterActivity.class);
                    intent.putExtra("book", book);
                    context.startActivity(intent);
                }
            }
        });

        if(BookLibrary.shared.have(holder.itemView.getContext(), book)) {
            holder.shineButton.setChecked(true);
        } else {
            holder.shineButton.setChecked(false);
        }

        final View shineButtonParent = (View) holder.shineButton.getParent();  // button: the view you want to enlarge hit area
        shineButtonParent.post( new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                holder.shineButton.getHitRect(rect);
                rect.top -= 16;
                rect.bottom += 16;    // increase top hit area
                rect.left -= 16;   // increase left hit area
                rect.right += 16;  // increase right hit area
                shineButtonParent.setTouchDelegate( new TouchDelegate( rect , holder.shineButton));
            }
        });

        holder.shineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.shineButton.isChecked()) {
                    BookLibrary.shared.save(holder.itemView.getContext(), book);

                    String addedString = holder.itemView.getContext().getResources().getString(R.string.Library_added);
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(holder.itemView.getContext(),
                            addedString + ": " + book.getName(),Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent1 = new Intent(Constants.NotificationHomeBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent1);
                    Intent intent2 = new Intent(Constants.NotificationHomeListBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent2);
                    Intent intent3 = new Intent(Constants.NotificationLibraryBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent3);

                } else {
                    BookLibrary.shared.remove(holder.itemView.getContext(), book);

                    // Show the removed item label`enter code here`

                    String removedString = holder.itemView.getContext().getResources().getString(R.string.Library_removed);
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(holder.itemView.getContext(),
                            removedString + ": " + book.getName(),Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent1 = new Intent(Constants.NotificationHomeBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent1);
                    Intent intent2 = new Intent(Constants.NotificationHomeListBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent2);
                    Intent intent3 = new Intent(Constants.NotificationLibraryBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent3);

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    @Override
    public Filter getFilter() {
        return booksFilter;
    }

    private Filter booksFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Book> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(booksFull);
                searchString = "";
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                searchString = filterPattern;

                for (Book book : booksFull) {
                    if (book.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                        continue;
                    }
                    if (book.getAuthor().toLowerCase().contains(filterPattern)) {
                        filteredList.add(book);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            books.clear();
            books.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    public class BooksListRecyclerViewAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView bookAuthor;
        ImageView bookImage;
        View separator;
        ShineButton shineButton;


        public BooksListRecyclerViewAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.row_book_name);
            bookAuthor = itemView.findViewById(R.id.row_book_author);
            bookImage = itemView.findViewById(R.id.row_book_image_view);
            separator = itemView.findViewById(R.id.books_list_separator);
            shineButton = itemView.findViewById(R.id.like);
        }
    }
}