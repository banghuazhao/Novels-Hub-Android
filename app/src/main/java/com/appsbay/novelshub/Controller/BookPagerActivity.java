package com.appsbay.novelshub.Controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.appsbay.novelshub.Model.Book;
import com.appsbay.novelshub.Model.BookChapter;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;
import com.appsbay.novelshub.Tools.PageBookmarkHelper;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;

import java.util.HashMap;
import java.util.Locale;

public class BookPagerActivity extends AppCompatActivity {
    Book book;
    BookChapter bookChapter;

    ScrollView scrollView;
    TextView titleTextView;
    TextView textView;

    Context mContext;

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_pager);

        mAdView = findViewById(R.id.adViewBanner);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        Intent intent = getIntent();
        bookChapter = intent.getExtras().getParcelable("bookChapter");
        book = intent.getExtras().getParcelable("book");

        setTitle(bookChapter.getChapterNumberName());

        mContext = this;

        scrollView = findViewById(R.id.book_pager_scroll);

        titleTextView = findViewById(R.id.book_pager_title);
        textView = findViewById(R.id.book_pager_text);

        titleTextView.setText(bookChapter.getChapterName());
        textView.setText(bookChapter.getText());
        titleTextView.setTextColor(MyColor.getTitleTextColor(mContext));
        textView.setTextColor(MyColor.getTitleTextColor(mContext));

        SharedPreferences preferences = this.getSharedPreferences("Font Preference", Context.MODE_PRIVATE);
        int textSize = preferences.getInt("textSize", 18);

        textView.setTextSize(textSize);

        configColor();

        loadInterstitialAd();

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                float gotoPostion = PageBookmarkHelper.getBookMark(mContext, book.getName(), bookChapter.getChapterNumberName(), bookChapter.getChapterName());
                int y = (int) (gotoPostion * scrollView.getChildAt(0).getHeight());
                scrollView.scrollTo(0, y);
            }
        });

    }

    private void loadInterstitialAd() {
        SharedPreferences preferences = this.getSharedPreferences("Ads Preference", Context.MODE_PRIVATE);
        int requestTime = preferences.getInt("interstitialAdRequestTime", 1);

        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt("interstitialAdRequestTime", requestTime + 1);
        editor.apply();

        if (requestTime >= 5) {

            AdRequest adRequestInterstitialAd = new AdRequest.Builder().build();

            InterstitialAd.load(this, getResources().getString(R.string.adInterstitialID), adRequestInterstitialAd, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    Log.i("Admob", "onAdLoaded");
                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(com.appsbay.novelshub.Controller.BookPagerActivity.this);
                        editor.putInt("interstitialAdRequestTime", 1);
                        editor.apply();
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

    private void configColor() {
        SharedPreferences preferences = this.getSharedPreferences("Color Preference", Context.MODE_PRIVATE);
        String backgroundColorName = preferences.getString("background", "default");

        scrollView.setBackgroundColor(MyColor.getBackgroundColor(this));
        MyImage.setBackgroundImage(this, scrollView);

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
            case R.id.nav_book_pager_more:
                String speakerString = getResources().getString(R.string.Begin_Reading);
                if (tts != null && tts.isSpeaking()) {
                    speakerString = getResources().getString(R.string.Stop_Reading);
                }

                float currentPosition = (float) scrollView.getScrollY() / scrollView.getChildAt(0).getHeight();
                String currentPositionPercent = String.format("%.0f%%", currentPosition * 100);
                float bookMarkPosition = PageBookmarkHelper.getBookMark(this, book.getName(), bookChapter.getChapterNumberName(), bookChapter.getChapterName());
                String bookMarkPositionPercent = String.format("%.0f%%", bookMarkPosition * 100);
                new XPopup.Builder(this)
                        .atView(findViewById(R.id.nav_book_pager_more))
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
                        .asAttachList(new String[]{getResources().getString(R.string.Font_Size),
                                        getResources().getString(R.string.ShareArticle),
                                        speakerString,
                                        getResources().getString(R.string.Add_book_markat) + ": " + currentPositionPercent,
                                        getResources().getString(R.string.Goto_Bookmark_at) + ": " + bookMarkPositionPercent},
                                new int[]{R.drawable.nav_text_format, R.drawable.nav_share, R.drawable.nav_speaker, R.drawable.nav_bookmark, R.drawable.nav_bookmark_circle},
                                new OnSelectListener() {
                                    @Override
                                    public void onSelect(int position, String text) {
                                        if (position == 0) {
                                            initPopWindow(findViewById(R.id.nav_book_pager_more));
                                        } else if (position == 1) {
                                            Intent sendIntent = new Intent();
                                            sendIntent.setAction(Intent.ACTION_SEND);

                                            String str = "";

                                            str += bookChapter.getChapterNumberName() + "\n\n";
                                            str += bookChapter.getChapterName() + "\n\n";
                                            str += bookChapter.getText();

                                            sendIntent.putExtra(Intent.EXTRA_TEXT, str);

                                            sendIntent.setType("text/plain");

                                            Intent shareIntent = Intent.createChooser(sendIntent, null);
                                            startActivity(shareIntent);
                                        } else if (position == 2) {
                                            if (tts != null && tts.isSpeaking()) {
                                                tts.stop();
                                                tts.shutdown();
                                            } else {
                                                tts = new TextToSpeech(mContext, new TextToSpeech.OnInitListener() {
                                                    @Override
                                                    public void onInit(int status) {
                                                        // TODO Auto-generated method stub
                                                        if (status == TextToSpeech.SUCCESS) {
                                                            int result = tts.setLanguage(Locale.ENGLISH);
                                                            if (result == TextToSpeech.LANG_MISSING_DATA ||
                                                                    result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                                                Log.e("error", "This Language is not supported");
                                                            } else {
                                                                speech(bookChapter.getText());
//                                                                tts.speak(bookChapter.getText(), TextToSpeech.QUEUE_FLUSH, null);
                                                            }
                                                        }
                                                    }
                                                });
                                            }
                                        } else if (position == 3) {
                                            float currentPosition = (float) scrollView.getScrollY() / scrollView.getChildAt(0).getHeight();
                                            String currentPositionPercent = String.format("%.0f%%", currentPosition * 100);
                                            PageBookmarkHelper.setBookMark(mContext, book.getName(), bookChapter.getChapterNumberName(), bookChapter.getChapterName(), currentPosition);
                                            String toastString = getResources().getString(R.string.Bookmark_added_at) + ": " + currentPositionPercent;
                                            Toast.makeText(mContext, toastString, Toast.LENGTH_SHORT).show();
                                        } else if (position == 4) {
                                            float gotoPostion = PageBookmarkHelper.getBookMark(mContext, book.getName(), bookChapter.getChapterNumberName(), bookChapter.getChapterName());
                                            int y = (int) (gotoPostion * scrollView.getChildAt(0).getHeight());
                                            scrollView.scrollTo(0, y);
                                        }
                                    }
                                })
                        .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void speech(String charSequence) {

        int position = 0;

        int sizeOfChar = charSequence.length();
        String testStri = charSequence.substring(position, sizeOfChar);

        int next = 20;
        int pos = 0;
        while (true) {
            String temp = "";
            Log.e("in loop", "" + pos);

            try {
                temp = testStri.substring(pos, next);
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, temp);
                tts.speak(temp, TextToSpeech.QUEUE_ADD, params);

                pos = pos + 20;
                next = next + 20;

            } catch (Exception e) {
                temp = testStri.substring(pos, testStri.length());
                tts.speak(temp, TextToSpeech.QUEUE_ADD, null);
                break;

            }
        }
    }

    private void initPopWindow(View v) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_popup, null, false);

        view.setBackgroundColor(MyColor.getBackgroundColor(this));

        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.anim.anim_popup);  //设置加载动画


        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        TextView sizeTextView = view.findViewById(R.id.popup_text_size_text_view);
        sizeTextView.setTextColor(MyColor.getTitleTextColor(this));

        SeekBar seekBar = view.findViewById(R.id.popup_seekBar);

        SharedPreferences preferences = this.getSharedPreferences("Font Preference", Context.MODE_PRIVATE);
        int textSize = preferences.getInt("textSize", 18);
        sizeTextView.setText(String.valueOf(textSize));
        seekBar.setProgress(textSize);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                sizeTextView.setText(String.valueOf(progress));
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("textSize", progress);
                editor.apply();
                textView.setTextSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 50, 0);
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.book_pager_menu, menu);

        menu.getItem(0).setIcon(MyImage.changeDrawableColor(this, R.drawable.ic_tab_more, MyColor.getButtonTintColor(this)));

        return super.onCreateOptionsMenu(menu);
    }
}
