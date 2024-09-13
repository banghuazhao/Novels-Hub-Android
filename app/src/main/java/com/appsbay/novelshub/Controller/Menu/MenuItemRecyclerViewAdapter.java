package com.appsbay.novelshub.Controller.Menu;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.appsbay.novelshub.Controller.Menu.MoreApps.MoreAppsActivity;
import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.HelperFunctions;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.MyImage;
import com.appsbay.novelshub.Tools.StoreHelper;

import java.util.ArrayList;

public class MenuItemRecyclerViewAdapter extends RecyclerView.Adapter<MenuItemRecyclerViewAdapter.MenuItemRecyclerViewViewHolder> {

    Context context;
    ArrayList<MenuItem> menuItems;

    public MenuItemRecyclerViewAdapter(Context context, ArrayList<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    @NonNull
    @Override
    public MenuItemRecyclerViewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_menu_item, parent, false);
        return new MenuItemRecyclerViewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuItemRecyclerViewViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);

        holder.itemText.setText(menuItem.getItemName());
        holder.itemText.setTextColor(MyColor.getTitleTextColor(context));
        holder.menuIcon.setImageDrawable(menuItem.getIcon());
        holder.rightArrow.setImageDrawable(MyImage.changeDrawableColor(context, R.drawable.icon_right_arrow, MyColor.getButtonTintColor(context)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0) {
                    Intent email = new Intent(Intent.ACTION_SEND);
                    email.putExtra(Intent.EXTRA_EMAIL, new String[]{"appsbayarea@gmail.com"});
                    email.putExtra(Intent.EXTRA_SUBJECT, HelperFunctions.getApplicationName(context) + " - " + context.getResources().getString(R.string.Feedback));
                    email.putExtra(Intent.EXTRA_TEXT, "");

                    email.setType("message/rfc822");

                    context.startActivity(Intent.createChooser(email, "Choose an Email client:"));
                }

                if (position == 1) {
                    StoreHelper.goToGoogleMarket(context, context.getPackageName());
                }

                if (position == 2) {
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT, HelperFunctions.getApplicationName(context));
                        String shareMessage = "https://play.google.com/store/apps/details?id=" + ((Activity) context).getPackageName() + "\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        context.startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch (Exception e) {
                        //e.toString();
                    }
                }

                if (position == 3) {
                    Intent intent = new Intent(context, MoreAppsActivity.class);
                    context.startActivity(intent);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    public class MenuItemRecyclerViewViewHolder extends RecyclerView.ViewHolder {

        TextView itemText;
        ImageView menuIcon;
        ImageView rightArrow;

        public MenuItemRecyclerViewViewHolder(@NonNull View itemView) {
            super(itemView);
            itemText = itemView.findViewById(R.id.row_menu_textView);
            menuIcon = itemView.findViewById(R.id.row_menu_icon);
            rightArrow = itemView.findViewById(R.id.row_menu_arrow);
        }
    }

}