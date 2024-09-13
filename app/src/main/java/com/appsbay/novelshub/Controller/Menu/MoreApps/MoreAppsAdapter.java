package com.appsbay.novelshub.Controller.Menu.MoreApps;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appsbay.novelshub.R;

import java.util.List;

public class MoreAppsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MoreApp> models;

    public MoreAppsAdapter(List<MoreApp> mData) {
        models = mData;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.row_more_apps,
                parent,
                false);
        MoreAppsViewHolder holder = new MoreAppsViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MoreApp moreApp = (MoreApp) models.get(position);
        MoreAppsViewHolder moreAppsViewHolder = (MoreAppsViewHolder) holder;
        moreAppsViewHolder.bind(moreApp);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
