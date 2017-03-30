package com.example.shubham.grievanceapp.Grievance;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.shubham.grievanceapp.R;

public class GrievanceImagesHolder extends RecyclerView.ViewHolder{
    private final ImageView mImageView;

    public GrievanceImagesHolder(Context context, View itemView) {
        super(itemView);
        mImageView = (ImageView) itemView.findViewById(R.id.grievance_doc);
    }

    public ImageView getImageView() {
        return mImageView;
    }
}
