package com.example.shubham.grievanceapp.Grievance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shubham.grievanceapp.R;
import com.example.shubham.grievanceapp.common.Utils;
import com.example.shubham.grievanceapp.model.Customer;
import com.example.shubham.grievanceapp.model.Grievance;
import com.example.shubham.grievanceapp.model.Manager;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class NewGrievanceActivity extends AppCompatActivity {

    public static final String EXTRA_GRIEVANCE_KEY = "com.example.shubham.grievanceapp.extra_greivance";

    @BindView(R.id.name)
    EditText mNameEt;

    @BindView(R.id.age)
    EditText mAgeEt;

    @BindView(R.id.sex)
    Spinner mSexSp;

    @BindView(R.id.manager)
    Spinner mManagerSp;

    private FirebaseDatabase db;
    private String[] sexArray;
    private ArrayList<String> managers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_grievance);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        db = FirebaseDatabase.getInstance();
        setUpViews();
    }

    private void setUpViews() {

        sexArray = getResources().getStringArray(R.array.sex_array);
        ArrayAdapter<CharSequence> sexAdapter = ArrayAdapter.createFromResource(this,
                R.array.sex_array, android.R.layout.simple_spinner_item);
        sexAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSexSp.setAdapter(sexAdapter);


        DatabaseReference managersReference = db.getReference("managers");
        managersReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                managers = new ArrayList<>();
                for (DataSnapshot managerSnapshot : dataSnapshot.getChildren()) {
                    managers.add(managerSnapshot.getValue(Manager.class).getName());
                }

                ArrayAdapter<String> managerAdapter = new ArrayAdapter<>(NewGrievanceActivity.this,
                        android.R.layout.simple_list_item_1, managers);
                managerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mManagerSp.setAdapter(managerAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(NewGrievanceActivity.this, databaseError.toString(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @OnClick(R.id.submit_fab)
    void Submit() {
        int selectedSexPos = mSexSp.getSelectedItemPosition();
        String custName = mNameEt.getText().toString();
        String age = mAgeEt.getText().toString();
        int selectedManagerPos = mManagerSp.getSelectedItemPosition();
        if (selectedSexPos < 0 || custName.isEmpty() || age.isEmpty() || selectedManagerPos < 0) {
            Toast.makeText(NewGrievanceActivity.this, R.string.incomplete_form, Toast.LENGTH_LONG).show();
        } else {
            Customer customer = new Customer(custName, age, sexArray[selectedSexPos]);
            Manager manager = new Manager(managers.get(selectedManagerPos));
            Grievance grievance = new Grievance(customer, manager, Utils.getUserName(this));
            Intent intent = new Intent(this, ImagesActivity.class);
            intent.putExtra(EXTRA_GRIEVANCE_KEY, grievance);
            startActivity(intent);
            finish();
        }
    }

}

