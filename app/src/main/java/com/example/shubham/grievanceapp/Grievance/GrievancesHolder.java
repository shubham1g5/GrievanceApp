package com.example.shubham.grievanceapp.Grievance;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.shubham.grievanceapp.R;

public class GrievancesHolder extends RecyclerView.ViewHolder {

    private final TextView mCustomerNameTv;
    private final TextView mCustomerAgeTv;
    private final TextView mManagerNameTv;

    private final View view;

    public GrievancesHolder(View itemView) {
        super(itemView);
        view = itemView;
        mCustomerNameTv = (TextView) itemView.findViewById(R.id.customer_name);
        mCustomerAgeTv = (TextView) itemView.findViewById(R.id.customer_age);
        mManagerNameTv = (TextView) itemView.findViewById(R.id.manager_name);

    }

    public void setCustomerName(String customerName) {
        mCustomerNameTv.setText(customerName);
    }

    public void setManagerName(String managerName) {
        mManagerNameTv.setText(managerName);
    }

    public void setCustomerAge(String customerAge) {
        mCustomerAgeTv.setText(customerAge);
    }

    public View getView() {
        return view;
    }
}
