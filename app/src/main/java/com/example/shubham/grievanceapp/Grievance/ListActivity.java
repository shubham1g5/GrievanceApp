package com.example.shubham.grievanceapp.Grievance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.grievanceapp.R;
import com.example.shubham.grievanceapp.common.Utils;
import com.example.shubham.grievanceapp.model.Grievance;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListActivity extends AppCompatActivity {

    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @BindView(R.id.empty_tv)
    TextView emptyView;

    private ArrayList<Grievance> grievances;
    private DatabaseReference db;
    private GrievancesAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.activity_list_title);


        db = FirebaseDatabase.getInstance().getReference();
        grievances = new ArrayList<>();
        setUpListView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchGrievances();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logOut() {
        removeUserData();
        showLoginActivity();
        finish();
    }

    private void removeUserData() {
        SharedPreferences sharedPreferences = getSharedPreferences(
                getString(R.string.preference_file_name), MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(getString(R.string.username));
        editor.commit();
    }

    private void showLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void fetchGrievances() {
        String username = Utils.getUserName(this);
        Query myGrievancesQuery = db.child("grievances")
                .orderByChild("username")
                .equalTo(username);
        myGrievancesQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                grievances = new ArrayList<>();
                for (DataSnapshot grivanceSnapshot : dataSnapshot.getChildren()) {
                    grievances.add(grivanceSnapshot.getValue(Grievance.class));
                }
                mAdapter.setGrievances(grievances);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ListActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setUpListView() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GrievancesAdapter(this, grievances, emptyView);
        mRecyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.add_fab)
    void addGrievance() {
        Intent intent = new Intent(this, NewGrievanceActivity.class);
        startActivity(intent);
    }

}
