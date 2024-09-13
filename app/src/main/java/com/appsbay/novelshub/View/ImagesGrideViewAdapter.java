package com.appsbay.novelshub.View;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.R;

import java.util.ArrayList;

public class ImagesGrideViewAdapter extends BaseAdapter {

    Context context;
    ArrayList<String> images;


    public ImagesGrideViewAdapter(Context context, ArrayList<String> images) {
        super();
        this.context = context;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        ImageView backgroundImage = view.findViewById(R.id.background_imageView);
        TextView imageText = view.findViewById(R.id.background_image_name);
        String imageName = images.get(position);
//        Log.d("debug", imageName);
        imageText.setText("");
        switch (imageName) {
            case "default":
                backgroundImage.setImageDrawable(null);
                backgroundImage.setBackgroundColor(Color.parseColor("#F6EFDE"));
                imageText.setText("Default");
                imageText.setTextColor(Color.BLACK);
                break;
            case "white":
                backgroundImage.setImageDrawable(null);
                backgroundImage.setBackgroundColor(Color.parseColor("#FFFEFF"));
                break;
            case "green":
                backgroundImage.setImageDrawable(null);
                backgroundImage.setBackgroundColor(Color.parseColor("#CCEFCF"));
                break;
            case "dark":
                backgroundImage.setImageDrawable(null);
                backgroundImage.setBackgroundColor(Color.parseColor("#131316"));
                imageText.setText("Dark Mode");
                imageText.setTextColor(Color.WHITE);
                break;
            case "bg":
                backgroundImage.setImageResource(R.drawable.bg);
                break;
            case "bg1":
                backgroundImage.setImageResource(R.drawable.bg1);
                break;
            case "bg2":
                backgroundImage.setImageResource(R.drawable.bg2);
                break;
            case "bg3":
                backgroundImage.setImageResource(R.drawable.bg3);
                break;
            case "bg4":
                backgroundImage.setImageResource(R.drawable.bg4);
                break;
            case "bg5":
                backgroundImage.setImageResource(R.drawable.bg5);
                break;
            default:
                backgroundImage.setImageDrawable(null);
                backgroundImage.setBackgroundColor(Color.parseColor("#F6EFDE"));
        }
        return view;
    }

}