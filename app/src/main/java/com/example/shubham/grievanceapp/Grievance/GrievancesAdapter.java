package com.example.shubham.grievanceapp.Grievance;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shubham.grievanceapp.R;
import com.example.shubham.grievanceapp.model.Grievance;

import java.util.ArrayList;

import static com.example.shubham.grievanceapp.Grievance.NewGrievanceActivity.EXTRA_GRIEVANCE_KEY;

public class GrievancesAdapter extends RecyclerView.Adapter<GrievancesHolder> {


    private final Context mContext;
    private ArrayList<Grievance> mGrievances;
    private final View mEmptyView;


    public GrievancesAdapter(Context context, ArrayList<Grievance> mGrievances, View emptyView) {
        this.mGrievances = mGrievances;
        mEmptyView = emptyView;
        mContext = context;
    }


    public void setGrievances(ArrayList<Grievance> mGrievances) {
        this.mGrievances = mGrievances;
        notifyDataSetChanged();
    }

    @Override
    public GrievancesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grievances_list_item, null);
        return new GrievancesHolder(view);
    }

    @Override
    public void onBindViewHolder(GrievancesHolder holder, int position) {
        Grievance grievance = mGrievances.get(position);
        holder.setCustomerName(grievance.getCustomer().getName());
        holder.setCustomerAge(mContext.getString(R.string.age_format, grievance.getCustomer().getAge()));
        holder.setManagerName(mContext.getString(R.string.manager_format, grievance.getManager()));

        holder.getView().setOnClickListener(v -> {
            Intent intent = new Intent(mContext, ImagesActivity.class);
            intent.putExtra(EXTRA_GRIEVANCE_KEY, grievance);
            mContext.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        mEmptyView.setVisibility(mGrievances.size() > 0 ? View.GONE : View.VISIBLE);
        return mGrievances.size();
    }
}
