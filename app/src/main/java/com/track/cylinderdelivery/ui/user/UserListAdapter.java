package com.track.cylinderdelivery.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.ui.company.CompanyListAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ViewHolder>{

    ArrayList<HashMap<String, String>> listData=new ArrayList<HashMap<String, String>>();
    Activity context;
    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public UserListAdapter(ArrayList<HashMap<String, String>> dataSet, Activity context) {
        listData = dataSet;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_users, parent, false);
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //holder.getTvName().setText(listData.get(position).get("fullName")+"");
        holder.imgActioinUser.setTag(position);
        String fullName=listData.get(position).get("fullName");
        holder.tvName.setText(fullName);
        /*String mobno=listData.get(position).get("phone");//+" "+listData.get(position).get("secondaryPhone");
        holder.tvMobileNo.setText(mobno);*/
        /*String email=listData.get(position).get("email");//+" "+listData.get(position).get("secondaryEmail");
        holder.tvEmail.setText(email);*/
        String address=listData.get(position).get("address1")+","+listData.get(position).get("address2");
        holder.tvAddress.setText(address.trim());
        /*String cratedby="Created by: "+listData.get(position).get("createdBy");
        holder.tvCreatedby.setText(cratedby);
        String createddate="Created Date: "+listData.get(position).get("createdDate");
        holder.tvCreated.setText(createddate);*/
    }

    @Override
    public int getItemCount() {
        //return localDataSet.length;
        return listData.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        private RelativeLayout relativeLayout;
        private TextView tvName,tvAddress;
        private int backgroundIndex = 0;
        private ImageView imgActioinUser;
        public ViewHolder(View view,int backgroundIndex) {
            super(view);
            this.backgroundIndex = backgroundIndex;
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rv_background);
            // Define click listener for the ViewHolder's View
            tvName = (TextView) view.findViewById(R.id.tvName);
            //tvMobileNo=(TextView)view.findViewById(R.id.tvMobileNo);
            //tvEmail=(TextView)view.findViewById(R.id.tvEmail);
            tvAddress=(TextView)view.findViewById(R.id.tvAddress);
            //tvCreated=(TextView)view.findViewById(R.id.tvCreated);
           // tvCreatedby=(TextView)view.findViewById(R.id.tvCreatedby);
            imgActioinUser=view.findViewById(R.id.imgActioinUser);
            imgActioinUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) imgActioinUser.getTag();
                    Intent intent=new Intent(context,UserDetailActivity.class);
                    intent.putExtra("editData",listData.get(pos));
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.enter_from_bottom, R.anim.hold_top);
                }
            });
        }
    }
}
