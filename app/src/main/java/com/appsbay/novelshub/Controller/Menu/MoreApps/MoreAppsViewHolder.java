package com.appsbay.novelshub.Controller.Menu.MoreApps;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.R;
import com.appsbay.novelshub.Tools.MyColor;
import com.appsbay.novelshub.Tools.StoreHelper;


public class MoreAppsViewHolder extends RecyclerView.ViewHolder {
    public MoreAppsViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public TextView name;
    public TextView description;
    public ImageView appIcon;

    public void bind(final MoreApp moreApp) {

        Context context = itemView.getContext();

        name = itemView.findViewById(R.id.textView_appName);
        name.setText(moreApp.getName());
        name.setTextColor(MyColor.getTitleTextColor(context));

        description = itemView.findViewById(R.id.textView_appDescription);
        description.setText(moreApp.getDescription());
        description.setTextColor(MyColor.getDetailTextColor(context));

        appIcon = itemView.findViewById(R.id.imageView_appIcon);
        appIcon.setImageDrawable(moreApp.getIconDrawable());

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StoreHelper.goToMarket(itemView.getContext(), moreApp.getPackageName());
            }
        });
    }
}
