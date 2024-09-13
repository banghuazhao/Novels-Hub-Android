package com.appsbay.novelshub.Controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.Controller.Menu.MenuActivity;
import com.appsbay.novelshub.Model.BookCategoryStore;
import com.appsbay.novelshub.Model.BookStore;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.Constants;
import com.appsbay.novelshub.Tools.HelperFunctions;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;
import com.appsbay.novelshub.View.BooksListRecyclerViewAdapter;
import com.appsbay.novelshub.View.BooksVerticalRecyclerViewAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

public class BooksFragment extends Fragment {

    ViewFlipper viewFlipper;
    RecyclerView booksVerticalRecyclerView;
    RecyclerView booksListRecyclerView;
    BooksVerticalRecyclerViewAdapter booksVerticalRecyclerViewAdapter;
    BooksListRecyclerViewAdapter booksListRecyclerViewAdapter;
    Integer viewFlipperChild;
    private Menu menu;
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;
    Context mContext;

    public BooksFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_books, container, false);
        mContext = getContext();

        mAdView = view.findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeAsUpIndicator(changeDrawableColor(mContext, R.drawable.nav_more, MyColor.getButtonTintColor(mContext)));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        viewFlipper = view.findViewById(R.id.main_view_flipper);

        booksVerticalRecyclerView = view.findViewById(R.id.main_category_recycler_view);
        booksVerticalRecyclerViewAdapter = new BooksVerticalRecyclerViewAdapter(mContext, BookCategoryStore.shared.getCategories());
        booksVerticalRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        booksVerticalRecyclerView.setAdapter(booksVerticalRecyclerViewAdapter);

        booksListRecyclerView = view.findViewById(R.id.main_list_recycler_view);
        booksListRecyclerViewAdapter = new BooksListRecyclerViewAdapter(mContext, BookStore.shared.getBooks(mContext));
        booksListRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        booksListRecyclerView.setAdapter(booksListRecyclerViewAdapter);

        viewFlipperChild = 0;
        viewFlipper.setDisplayedChild(viewFlipperChild);

        configColor();

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("NotificationBackgroundChange"));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(notificationHomeBookChange,
                new IntentFilter(Constants.NotificationHomeBookChange));
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(notificationHomeListBookChange,
                new IntentFilter(Constants.NotificationHomeListBookChange));
        return view;
    }

    @Override
    public void onDestroy() {
        // Unregister since the activity is about to be closed.
        // This is somewhat like [[NSNotificationCenter defaultCenter] removeObserver:name:object:]
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(notificationHomeBookChange);
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(notificationHomeListBookChange);
        super.onDestroy();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            viewFlipper.setBackgroundColor(MyColor.getBackgroundColor(mContext));
            MyImage.setBackgroundImage(mContext, viewFlipper);
        }
    };

    private BroadcastReceiver notificationHomeBookChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            booksVerticalRecyclerViewAdapter.notifyDataSetChanged();
        }
    };

    private BroadcastReceiver notificationHomeListBookChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            booksListRecyclerViewAdapter.notifyDataSetChanged();

        }
    };

    private void configColor() {
        SharedPreferences preferences = mContext.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        viewFlipper.setBackgroundColor(MyColor.getBackgroundColor(mContext));
        MyImage.setBackgroundImage(mContext, viewFlipper);

        Window window = ((AppCompatActivity) getActivity()).getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(MyColor.getActionBarColor(mContext));

        View decorView = window.getDecorView();
        if (backgroundColorName.equals("dark")) {
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(MyColor.getActionBarColor(mContext)));

        Spannable text = new SpannableString(actionBar.getTitle());
        text.setSpan(new ForegroundColorSpan(MyColor.getTitleTextColor(mContext)), 0, text.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        actionBar.setTitle(text);

        if (menu != null) {
            MenuItem searchItem = menu.findItem(R.id.home_menu_action_search);

            if (searchItem != null) {

                SearchView searchView = (SearchView) searchItem.getActionView();

                int searchHintBtnId = searchView.getContext().getResources()
                        .getIdentifier("android:id/search_button", null, null);
                ImageView searchIcon = searchView.findViewById(searchHintBtnId);
                searchIcon.setImageDrawable(changeDrawableColor(mContext, R.drawable.nav_search, MyColor.getButtonTintColor(mContext)));

                int searchTextID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
                TextView textView = searchView.findViewById(searchTextID);
                textView.setTextColor(MyColor.getTitleTextColor(mContext));

                int searchCloseID = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
                ImageView searchClose = searchView.findViewById(searchCloseID);
                searchClose.setImageDrawable(changeDrawableColor(mContext, R.drawable.nav_close, MyColor.getButtonTintColor(mContext)));
            }

            if (viewFlipper.getDisplayedChild() == 0) {
                menu.getItem(1).setIcon(changeDrawableColor(mContext, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(mContext)));
            } else {
                menu.getItem(1).setIcon(changeDrawableColor(mContext, R.drawable.nav_grid22, MyColor.getButtonTintColor(mContext)));
            }
            menu.getItem(2).setIcon(changeDrawableColor(mContext, R.drawable.nav_sun, MyColor.getButtonTintColor(mContext)));
        }

        BottomNavigationView navigation = (BottomNavigationView) getActivity().findViewById(R.id.bottom_navigation_main);
        navigation.setBackground(new ColorDrawable(MyColor.getBottomBarColor(mContext)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent(mContext, MenuActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.home_menu_action_change:
                if (viewFlipper.getDisplayedChild() == 0) {
                    item.setIcon(changeDrawableColor(mContext, R.drawable.nav_grid22, MyColor.getButtonTintColor(mContext)));
                    viewFlipperChild = 1;
                } else {
                    item.setIcon(changeDrawableColor(mContext, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(mContext)));
                    viewFlipperChild = 0;
                }
                viewFlipper.showNext();
                return true;
            case R.id.home_menu_action_image:
                Intent i = new Intent(mContext, ImagesActivity.class);
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
            configColor();
            booksVerticalRecyclerViewAdapter.notifyDataSetChanged();
            booksListRecyclerViewAdapter.notifyDataSetChanged();

            AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();

            InterstitialAd.load(mContext, getResources().getString(R.string.adInterstitialID), adRequestInterstitialAd, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i("Admob", "onAdLoaded");
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
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

    public static Drawable changeDrawableColor(Context context, int icon, int newColor) {
        Drawable mDrawable = ContextCompat.getDrawable(context, icon).mutate();
        mDrawable.setColorFilter(new PorterDuffColorFilter(newColor, PorterDuff.Mode.SRC_IN));
        return mDrawable;
    }

    SearchView searchView;

    // create an action bar button
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);

        this.menu = menu;

        MenuItem searchItem = menu.findItem(R.id.home_menu_action_search);
        searchView = (SearchView) searchItem.getActionView();

        int searchHintBtnId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_button", null, null);
        ImageView searchIcon = searchView.findViewById(searchHintBtnId);
        searchIcon.setImageDrawable(changeDrawableColor(mContext, R.drawable.nav_search, MyColor.getButtonTintColor(mContext)));

        int searchTextID = searchView.getContext().getResources().getIdentifier("android:id/search_src_text", null, null);
        TextView textView = searchView.findViewById(searchTextID);
        textView.setTextColor(MyColor.getTitleTextColor(mContext));

        int searchCloseID = searchView.getContext().getResources().getIdentifier("android:id/search_close_btn", null, null);
        ImageView searchClose = searchView.findViewById(searchCloseID);
        searchClose.setImageDrawable(changeDrawableColor(mContext, R.drawable.nav_close, MyColor.getButtonTintColor(mContext)));

        if (viewFlipper.getDisplayedChild() == 0) {
            menu.getItem(1).setIcon(changeDrawableColor(mContext, R.drawable.nav_list_bullet, MyColor.getButtonTintColor(mContext)));
        } else {
            menu.getItem(1).setIcon(changeDrawableColor(mContext, R.drawable.nav_grid22, MyColor.getButtonTintColor(mContext)));
        }

        menu.getItem(2).setIcon(changeDrawableColor(mContext, R.drawable.nav_sun, MyColor.getButtonTintColor(mContext)));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
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

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.setDisplayedChild(1);
            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
        searchView.setQuery("", true);
    }
}