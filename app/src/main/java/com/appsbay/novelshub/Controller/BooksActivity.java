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
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Controller.Menu.MenuActivity;
import com.appsbay.novelshub.Model.BookCategoryStore;
import com.appsbay.novelshub.Model.BookStore;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;
import com.appsbay.novelshub.Tools.RateItDialogFragment;
import com.appsbay.novelshub.View.BooksListRecyclerViewAdapter;
import com.appsbay.novelshub.View.BooksVerticalRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class BooksActivity extends AppCompatActivity {

    ViewFlipper viewFlipper;
    RecyclerView booksVerticalRecyclerView;
    RecyclerView booksListRecyclerView;
    BooksVerticalRecyclerViewAdapter booksVerticalRecyclerViewAdapter;
    BooksListRecyclerViewAdapter booksListRecyclerViewAdapter;
    Integer viewFlipperChild;
    private Menu menu;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RateItDialogFragment.show(this, getSupportFragmentManager());

        mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getSupportActionBar().setHomeAsUpIndicator(changeDrawableColor(this, R.drawable.nav_more, MyColor.getButtonTintColor(this)));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewFlipper = findViewById(R.id.main_view_flipper);

        booksVerticalRecyclerView = findViewById(R.id.main_category_recycler_view);
        booksListRecyclerView = findViewById(R.id.main_list_recycler_view);

        booksVerticalRecyclerViewAdapter = new BooksVerticalRecyclerViewAdapter(this, BookCategoryStore.shared.getCategories());
        booksVerticalRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        booksVerticalRecyclerView.setAdapter(booksVerticalRecyclerViewAdapter);

        booksListRecyclerViewAdapter = new BooksListRecyclerViewAdapter(this, BookStore.shared.books);
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        booksListRecyclerView.setAdapter(booksListRecyclerViewAdapter);

        viewFlipperChild = 0;
        viewFlipper.setDisplayedChild(viewFlipperChild);

        configColor();
    }

    private void configColor() {
        SharedPreferences preferences = this.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        viewFlipper.setBackgroundColor(MyColor.getBackgroundColor(this));
        MyImage.setBackgroundImage(this, viewFlipper);

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


        if (menu != null) {
            MenuItem searchItem = menu.findItem(R.id.home_menu_action_search);
            SearchView searchView = (SearchView) searchItem.getActionView();

            int searchHintBtnId = searchView.getContext().getResources()
                    .getIdentifier("android:id/search_button", null, null);
            ImageView searchIcon = searchView.findViewById(searchHintBtnId);
            searchIcon.setImageDrawable(changeDrawableColor(this, R.drawable.nav_search, MyColor.getButtonTintColor(this)));

            int searchTextID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
            TextView textView = searchView.findViewById(searchTextID);
            textView.setTextColor(MyColor.getTitleTextColor(this));

            int searchCloseID = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
            ImageView searchClose = searchView.findViewById(searchCloseID);
            searchClose.setImageDrawable(changeDrawableColor(this, R.drawable.nav_close, MyColor.getButtonTintColor(this)));

            if (viewFlipper.getDisplayedChild() == 0) {
                menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(this)));
            } else {
                menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_grid22, MyColor.getButtonTintColor(this)));
            }
            menu.getItem(2).setIcon(changeDrawableColor(this, R.drawable.nav_sun, MyColor.getButtonTintColor(this)));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(this, MenuActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.home_menu_action_change:
                if (viewFlipper.getDisplayedChild() == 0) {
                    item.setIcon(changeDrawableColor(this, R.drawable.nav_grid22, MyColor.getButtonTintColor(this)));
                    viewFlipperChild = 1;
                } else {
                    item.setIcon(changeDrawableColor(this, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(this)));
                    viewFlipperChild = 0;
                }
                viewFlipper.showNext();
                return true;
            case R.id.home_menu_action_image:
                Intent i = new Intent(this, ImagesActivity.class);
                startActivityForResult(i, 1);
                return true;
            case R.id.home_menu_action_search:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                configColor();
                booksVerticalRecyclerViewAdapter.notifyDataSetChanged();
                booksListRecyclerViewAdapter.notifyDataSetChanged();

                AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();

                InterstitialAd.load(this, getResources().getString(R.string.adInterstitialID), adRequestInterstitialAd, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        Log.i("Admob", "onAdLoaded");
                        if (mInterstitialAd != null) {
                            mInterstitialAd.show(BooksActivity.this);
                        } else {
                            Log.d("Admob", "The interstitial ad wasn't ready yet.");
                        }
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("Admob", loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });
            }
        }
    }

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        getMenuInflater().inflate(R.menu.home_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.home_menu_action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        int searchHintBtnId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = searchView.findViewById(searchHintBtnId);
        searchIcon.setImageDrawable(changeDrawableColor(this, R.drawable.nav_search, MyColor.getButtonTintColor(this)));

        int searchTextID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(searchTextID);
        textView.setTextColor(MyColor.getTitleTextColor(this));

        int searchCloseID = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = searchView.findViewById(searchCloseID);
        searchClose.setImageDrawable(changeDrawableColor(this, R.drawable.nav_close, MyColor.getButtonTintColor(this)));

        if (viewFlipper.getDisplayedChild() == 0) {
            menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(this)));
        } else {
            menu.getItem(1).setIcon(changeDrawableColor(this, R.drawable.nav_grid22, MyColor.getButtonTintColor(this)));
        }
        menu.getItem(2).setIcon(changeDrawableColor(this, R.drawable.nav_sun, MyColor.getButtonTintColor(this)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                viewFlipper.setDisplayedChild(1);
                booksListRecyclerViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                viewFlipper.setDisplayedChild(viewFlipperChild);
                booksListRecyclerViewAdapter.getFilter().filter("");
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}