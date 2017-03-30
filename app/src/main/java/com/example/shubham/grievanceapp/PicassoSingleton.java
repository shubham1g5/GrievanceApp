package com.example.shubham.grievanceapp;

import android.content.Context;

import com.squareup.picasso.Picasso;

public class PicassoSingleton {

    private static Picasso picasso;

    public static Picasso getPicasso(Context context) {
        if (picasso == null) {
            picasso = Picasso.with(context);
        }
        return picasso;
    }
}
