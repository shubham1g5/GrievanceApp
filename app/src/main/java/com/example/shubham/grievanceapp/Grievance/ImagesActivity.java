package com.example.shubham.grievanceapp.Grievance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shubham.grievanceapp.R;
import com.example.shubham.grievanceapp.model.Grievance;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.shubham.grievanceapp.Grievance.NewGrievanceActivity.EXTRA_GRIEVANCE_KEY;

/**
 * Created by shubham on 28/3/17.
 */

public class ImagesActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 0;

    private StorageReference storageRef;
    private ArrayList<String> urls;
    private Grievance mGrievance;
    private DatabaseReference mDatabase;

    @BindView(R.id.images_recyclerview)
    RecyclerView recyclerView;

    @BindView(R.id.empty_tv)
    TextView emptyView;

    @BindView(R.id.upload_progress)
    ProgressBar uploadProgress;

    private ImagesAdapter mAdapter;
    private String mGrievanceKey;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference("grievances");

        mGrievance = getIntent().getParcelableExtra(EXTRA_GRIEVANCE_KEY);
        urls = mGrievance.getUrls();
        if (urls == null) {
            urls = new ArrayList<>();
        }

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, getResources().getInteger(R.integer.col_count)));
        mAdapter = new ImagesAdapter(this, urls, emptyView);
        recyclerView.setAdapter(mAdapter);
    }

    @OnClick(R.id.add_fab)
    void showTakeImageDialogue() {
        dispatchTakePictureIntent();
    }

    void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = intent.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            uploadProgress.setVisibility(View.VISIBLE);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            UploadTask uploadTask = storageRef.child(new Date().getTime() + ".jpg").putBytes(data);

            uploadTask.addOnFailureListener(exception -> {
                Toast.makeText(this, R.string.upload_failed, Toast.LENGTH_LONG).show();
                uploadProgress.setVisibility(View.GONE);
            }).addOnSuccessListener(taskSnapshot -> {
                urls.add(taskSnapshot.getMetadata().getDownloadUrl().toString());
                saveGrievance();
                mAdapter.setUrls(urls);
                uploadProgress.setVisibility(View.GONE);
            });
        }
    }

    private void saveGrievance() {
        mGrievance.setUrls(urls);
        if (mGrievanceKey == null) {
            mGrievanceKey = mDatabase.push().getKey();
        }
        mDatabase.child(mGrievanceKey).setValue(mGrievance);
    }
}
