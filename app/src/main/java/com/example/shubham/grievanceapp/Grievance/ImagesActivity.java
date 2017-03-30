package com.example.shubham.grievanceapp.Grievance;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.shubham.grievanceapp.Grievance.NewGrievanceActivity.EXTRA_DISABLE_FAB;
import static com.example.shubham.grievanceapp.Grievance.NewGrievanceActivity.EXTRA_GRIEVANCE_KEY;

/**
 * Created by shubham on 28/3/17.
 */

public class ImagesActivity extends AppCompatActivity implements ChoosePictureMethodInterface {

    private static final int REQUEST_IMAGE_CAPTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private static final int GALLERY_INDEX = 0;
    private static final int CAMERA_INDEX = 1;

    private static final String CHOOSE_PIC_METHOD = "choose_pic_method";

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

    private boolean disableFab = false;

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

        disableFab = getIntent().getBooleanExtra(EXTRA_DISABLE_FAB,false);
        if(disableFab){
            findViewById(R.id.add_fab).setVisibility(View.GONE);
        }

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
        DialogFragment dialogFragment = new ChoosePictureMethodDialogFragment();
        dialogFragment.show(getSupportFragmentManager(), CHOOSE_PIC_METHOD);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if(resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_IMAGE_CAPTURE:
                    Bundle extras = intent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
                    uploadBitmap(imageBitmap);
                    break;
                case REQUEST_PICK_IMAGE:
                    Uri imageUri = intent.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        uploadBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    break;
                default:
                    break;
            }
        }
    }

    private void uploadBitmap(Bitmap imageBitmap) {
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

    private void saveGrievance() {
        mGrievance.setUrls(urls);
        if (mGrievanceKey == null) {
            mGrievanceKey = mDatabase.push().getKey();
        }
        mDatabase.child(mGrievanceKey).setValue(mGrievance);
    }

    @Override
    public void selectMethod(int which) {
        switch (which) {
            case GALLERY_INDEX:
                dispatchChoosePictureIntent();
                break;
            case CAMERA_INDEX:
                dispatchTakePictureIntent();
                break;
            default:
                break;
        }
    }

    private void dispatchChoosePictureIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PICK_IMAGE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

}
