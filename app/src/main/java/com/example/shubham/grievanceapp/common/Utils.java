package com.example.shubham.grievanceapp.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.shubham.grievanceapp.R;

import static android.content.Context.MODE_PRIVATE;

public class Utils {

    @Nullable
    public static String getUserName(@NonNull Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.preference_file_name), MODE_PRIVATE);
        return sharedPreferences.getString(context.getString(R.string.username), null);
    }
}
