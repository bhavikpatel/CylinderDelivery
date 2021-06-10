package com.track.cylinderdelivery.ui.company;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.ui.user.UserDetailActivity;

import java.util.ArrayList;
import java.util.HashMap;

public class CompanyListAdapter extends RecyclerView.Adapter<CompanyListAdapter.ViewHolder>{

    ArrayList<HashMap<String, String>> companyList;
    Activity context;
    public CompanyListAdapter(ArrayList<HashMap<String, String>> dataList, Activity activity) {
        companyList=dataList;
        context=activity;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName;
        TextView txtAddress;
        ImageView imgArrow;
        TextView txtAdminName,txtStatus;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtName = (TextView) view.findViewById(R.id.txtName);
            txtAddress=(TextView)view.findViewById(R.id.txtAddress);
            imgArrow=(ImageView)view.findViewById(R.id.imgArrow);
            txtAdminName=(TextView)view.findViewById(R.id.txtAdminName);
            txtStatus=view.findViewById(R.id.txtStatus);
            imgArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) imgArrow.getTag ();
                    Intent intent=new Intent(context, CompanyDetail.class);
                    intent.putExtra("editData",companyList.get(pos));
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.enter_from_bottom, R.anim.hold_top);
                }
            });
        }
    }

    @NonNull
    @Override
    public CompanyListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_company, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyListAdapter.ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
       // holder.getTextView().setText(localDataSet[position]);
        holder.imgArrow.setTag(position);
        holder.txtName.setText(companyList.get(position).get("companyName"));
        holder.txtAdminName.setText("Admin Name: "+companyList.get(position).get("adminName"));
        holder.txtStatus.setText(companyList.get(position).get("status"));
        holder.txtAddress.setText(companyList.get(position).get("address1")+
                ","+companyList.get(position).get("address2")+
                ","+companyList.get(position).get("city")+
                ","+companyList.get(position).get("county")+
                ","+companyList.get(position).get("zipCode"));
    }

    @Override
    public int getItemCount() {
        //return localDataSet.length;
        return companyList.size();
    }
}
