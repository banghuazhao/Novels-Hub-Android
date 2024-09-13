package com.appsbay.novelshub.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class BooksHorizontalRecyclerViewAdapter extends RecyclerView.Adapter<BooksHorizontalRecyclerViewAdapter.BooksHorizontalRecyclerViewViewHolder> {

    Context context;
    ArrayList<Book> books;
    public Toast toast;

    public BooksHorizontalRecyclerViewAdapter(Context context, ArrayList<Book> books) {
        this.context = context;
        this.books = books;
    }

    @NonNull
    @Override
    public BooksHorizontalRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_book, parent, false);
        return new BooksHorizontalRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BooksHorizontalRecyclerViewViewHolder holder, int position) {
        Book book = books.get(position);
        holder.bookName.setText(book.getName());
        holder.bookName.setTextColor(MyColor.getTitleTextColor(context));
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookAuthor.setTextColor(MyColor.getDetailTextColor(context));

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

        holder.bookImage.setOnClickListener(new View.OnClickListener() {
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

                    Intent intent1 = new Intent(Constants.NotificationHomeListBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent1);
                    Intent intent2 = new Intent(Constants.NotificationLibraryBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent2);
                } else {
                    BookLibrary.shared.remove(holder.itemView.getContext(), book);
                    String removedString = holder.itemView.getContext().getResources().getString(R.string.Library_removed);
                    if (toast != null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(holder.itemView.getContext(),
                            removedString + ": " + book.getName(),Toast.LENGTH_SHORT);
                    toast.show();

                    Intent intent1 = new Intent(Constants.NotificationHomeListBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent1);
                    Intent intent2 = new Intent(Constants.NotificationLibraryBookChange);
                    LocalBroadcastManager.getInstance(holder.itemView.getContext()).sendBroadcast(intent2);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public class BooksHorizontalRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView bookName;
        TextView bookAuthor;
        ImageView bookImage;
        ShineButton shineButton;


        public BooksHorizontalRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.book_name);
            bookAuthor = itemView.findViewById(R.id.book_author_name);
            bookImage = itemView.findViewById(R.id.book_image);
            shineButton = itemView.findViewById(R.id.like);
        }
    }

}
