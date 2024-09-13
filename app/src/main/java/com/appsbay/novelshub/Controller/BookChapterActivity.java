package com.appsbay.novelshub.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Model.Book;
import com.appsbay.novelshub.Model.BookChapter;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.ACache;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;
import com.appsbay.novelshub.View.BookChapterRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class BookChapterActivity extends AppCompatActivity {
    Book book;
    ArrayList<BookChapter> bookChapters = new ArrayList<>();

    RecyclerView recyclerView;
    BookChapterRecyclerViewAdapter adapter;

    Context mContext;

    ProgressBar pBar;
    TextView textView_network_error;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_chapter);

        mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        pBar = findViewById(R.id.pBar);
        pBar.setVisibility(View.VISIBLE);
        textView_network_error = findViewById(R.id.textView_network_error);
        textView_network_error.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        book = intent.getExtras().getParcelable("book");

        setTitle(book.getName());

        mContext = this;

        adapter = new BookChapterRecyclerViewAdapter(this, bookChapters, book);

        recyclerView = findViewById(R.id.book_chapter_recycler_view);

        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        fetchBookChapters();

        configColor();
    }

    private void fetchBookChapters() {

        if (book.isOnline()) {
            String requestString = "https://dtv96jzsdft4e.cloudfront.net/NoverlsHub/" + book.getFileName().replace(" ", "+") + ".json";
            Log.d("myTag", requestString);
            ACache mCache = ACache.get(this);
            String data = mCache.getAsString(requestString);
            if (data != null) {
                setChapterData(data);
            } else {
                Request request = new Request.Builder()
                        .url(requestString)
                        .build();
                OkHttpClient client = new OkHttpClient();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        e.printStackTrace();
                        if (this == null) return;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                pBar.setVisibility(View.INVISIBLE);
                                textView_network_error.setVisibility(View.VISIBLE);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (!response.isSuccessful()) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    pBar.setVisibility(View.INVISIBLE);
                                    textView_network_error.setVisibility(View.VISIBLE);
                                }
                            });
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String responseString = response.body().string();
                            mCache.put(requestString, responseString, 365 * 24 * 60 * 60);
                            setChapterData(responseString);
                        }
                    }
                });
            }
        } else {
            setChapterData(loadJSONFromAsset());
        }
    }

    private void setChapterData(String jsonString) {
        try {
            JSONObject obj = new JSONObject(jsonString);

            JSONArray m_jArry;
            if (obj.has(book.getName())) {
                m_jArry = obj.getJSONArray(book.getName());
            } else {
                String key = obj.keys().next();
                m_jArry = obj.getJSONArray(key);
            }

            for (int i = 0; i < m_jArry.length(); i++) {
                JSONObject jo_inside = m_jArry.getJSONObject(i);
                String chapterNumberName = jo_inside.getString("章节");
                String chapterName = jo_inside.getString("章节名称");
                String chapterText = jo_inside.getString("章节内容");

                BookChapter bookChapter = new BookChapter(chapterNumberName, chapterName, chapterText);
                bookChapters.add(bookChapter);
            }


            if (this == null) return;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    pBar.setVisibility(View.INVISIBLE);
                    adapter.notifyDataSetChanged();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("files/" + book.getFileName() + ".json");
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

    private void configColor() {
        SharedPreferences preferences = this.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        recyclerView.setBackgroundColor(MyColor.getBackgroundColor(this));
        MyImage.setBackgroundImage(this, recyclerView);

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MyColor.getActionBarColor(this));

        View decorView = window.getDecorView();
        if (backgroundColorName.equals("dark")) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(MyColor.getActionBarColor(this)));

        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(MyColor.getTitleTextColor(this)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(new PorterDuffColorFilter(MyColor.getButtonTintColor(this), PorterDuff.Mode.SRC_ATOP));
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.nav_book_chapter_bookmark:
                SharedPreferences preferences = this.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
                int bookmarkNum = preferences.getInt(book.getName(), 0);
                recyclerView.smoothScrollToPosition(bookmarkNum);
        }
        return super.onOptionsItemSelected(item);
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.bookmark_menu, menu);

        menu.getItem(0).setIcon(MyImage.changeDrawableColor(this, R.drawable.nav_bookmark, MyColor.getButtonTintColor(this)));


        return super.onCreateOptionsMenu(menu);
    }
}
