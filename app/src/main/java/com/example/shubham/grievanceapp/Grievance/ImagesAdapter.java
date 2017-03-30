package com.example.shubham.grievanceapp.Grievance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shubham.grievanceapp.PicassoSingleton;
import com.example.shubham.grievanceapp.R;

import java.util.ArrayList;

public class ImagesAdapter extends RecyclerView.Adapter<GrievanceImagesHolder> {

    private ArrayList<String> urls;
    private final Context mContext;
    private final View mEmptyView;

    public ImagesAdapter(Context context, ArrayList<String> urls, View emptyView) {
        this.urls = urls;
        mContext = context;
        mEmptyView = emptyView;
        mEmptyView.setVisibility(urls.size() > 0 ? View.GONE : View.VISIBLE);
    }

    public void setUrls(ArrayList<String> urls) {
        this.urls = urls;
        mEmptyView.setVisibility(urls.size() > 0 ? View.GONE : View.VISIBLE);
        notifyDataSetChanged();
    }

    @Override
    public GrievanceImagesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievance_images_list_item, null);
        return new GrievanceImagesHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(GrievanceImagesHolder holder, int position) {
        final String url = urls.get(position);
        PicassoSingleton.getPicasso(mContext)
                .load(url)
                .placeholder(R.drawable.loading_animation)
                .error(android.R.drawable.stat_notify_error)
                .into(holder.getImageView());
    }

    @Override
    public int getItemCount() {
        return urls.size();
    }
}
