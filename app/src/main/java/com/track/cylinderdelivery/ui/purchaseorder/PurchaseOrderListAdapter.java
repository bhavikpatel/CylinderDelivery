package com.track.cylinderdelivery.ui.purchaseorder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.track.cylinderdelivery.R;

import java.util.ArrayList;
import java.util.HashMap;

public class PurchaseOrderListAdapter extends RecyclerView.Adapter<PurchaseOrderListAdapter.ViewHolder>{

    ArrayList<HashMap<String, String>> podetailList;
    Activity context;
    public PurchaseOrderListAdapter(ArrayList<HashMap<String, String>> dataList, Activity activity) {
        podetailList=dataList;
        context=activity;
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPonumber;
        TextView txtUserName;
        ImageView imgArrow;
        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View
            txtPonumber = (TextView) view.findViewById(R.id.txtPonumber);
            txtUserName=(TextView)view.findViewById(R.id.txtUserName);
            imgArrow=(ImageView)view.findViewById(R.id.imgArrow);
            imgArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos= (int) imgArrow.getTag ();
                    /*Intent intent=new Intent(context, CylinderDetailActivity.class);
                    intent.putExtra("editData",cylinderList.get(pos));
                    context.startActivity(intent);
                    context.overridePendingTransition(R.anim.enter_from_bottom, R.anim.hold_top);*/
                    /*AlertDialog.Builder adb = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
                    //adb.setView(alertDialogView);
                    adb.setTitle("You are sure won't be Delete this PO Detail!");
                    adb.setIcon(R.drawable.ic_baseline_delete_24);
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(isNetworkConnected()){
                                context.callChangeCompanyStatus(podetailList.get(pos).get("podetailid"));
                            }else {
                                Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    adb.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //finish();
                        }
                    });
                    adb.show();*/
                }
            });
        }
    }



    @NonNull
    @Override
    public PurchaseOrderListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_purchase_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchaseOrderListAdapter.ViewHolder holder, int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        holder.imgArrow.setTag(position);
        holder.txtPonumber.setText(podetailList.get(position).get("poNumber"));
        holder.txtUserName.setText(podetailList.get(position).get("username"));
        if(podetailList.get(position).get("status").equals("Pending")){
                holder.imgArrow.setImageResource(R.drawable.ic_baseline_pending_24);
        }else if(podetailList.get(position).get("status").equals("Draft")){
            holder.imgArrow.setImageResource(R.drawable.ic_baseline_edit_24);
        }
    }

    @Override
    public int getItemCount() {
        //return localDataSet.length;
        return podetailList.size();
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
