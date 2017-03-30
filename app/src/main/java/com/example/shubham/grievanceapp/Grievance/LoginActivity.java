package com.example.shubham.grievanceapp.Grievance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shubham.grievanceapp.R;
import com.example.shubham.grievanceapp.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by shubham on 28/3/17.
 */

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.username)
    EditText userName;

    @BindView(R.id.password)
    EditText mPasswordView;

    @BindView(R.id.login_progress)
    View mProgressView;
    private DatabaseReference mUsersReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isSignedIn()) {
            showListActivity();
        }

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mUsersReference = FirebaseDatabase.getInstance().getReference().child("users");
    }

    private void showListActivity() {
        Intent intent = new Intent(this, ListActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.email_sign_in_button)
    public void signIn(View view) {

        String username = userName.getText().toString();
        final String password = mPasswordView.getText().toString();

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, R.string.incomplete_form, Toast.LENGTH_LONG).show();
        } else {
            mProgressView.setVisibility(View.VISIBLE);

            mUsersReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getPassword() != null && password.compareTo(user.getPassword()) == 0) {
                        // It's a match, Store username and open up Home Activity
                        saveUserName(username);
                        showListActivity();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.auth_failed, Toast.LENGTH_LONG).show();
                    }
                    mProgressView.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(LoginActivity.this,databaseError.toString(), Toast.LENGTH_LONG).show();
                    mProgressView.setVisibility(View.GONE);
                }
            });

        }
    }

    private boolean isSignedIn() {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
        if (sharedPreferences.contains(getString(R.string.username))) {
            return true;
        }
        return false;
    }

    private void saveUserName(String username) {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(getString(R.string.username), username);
        editor.commit();
    }

}
