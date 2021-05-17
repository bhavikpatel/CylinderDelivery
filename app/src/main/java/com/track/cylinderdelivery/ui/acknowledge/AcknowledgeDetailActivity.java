package com.track.cylinderdelivery.ui.acknowledge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.track.cylinderdelivery.R;

import java.util.HashMap;

public class AcknowledgeDetailActivity extends AppCompatActivity {

    RelativeLayout rvBlank;
    Context context;
    ImageView btnCancel;
    private HashMap<String, String> mapdata;
    TextView txtUserName,txtAdminName,txtRemark,txtStatus;
    LinearLayout lvHoldingCapacity,lvAcknowledgeReamark;
    TextView txtHoldCap,txtAcknowledgeReamrk,txtCreatedbylbl;
    TextView txtCreatedDate,txtAckby,txtAckDate;
    LinearLayout lvAckBy,lvAckDate;
    ImageView btnAction;
    private SharedPreferences shpreRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acknowledge_detail);
        context=this;
        mapdata= (HashMap<String, String>) getIntent().getSerializableExtra("editData");
        rvBlank=findViewById(R.id.rvBlank);
        btnCancel=findViewById(R.id.btnCancel);
        txtUserName=findViewById(R.id.txtUserName);
        txtAdminName=findViewById(R.id.txtAdminName);
        txtRemark=findViewById(R.id.txtRemark);
        txtStatus=findViewById(R.id.txtStatus);
        lvHoldingCapacity=findViewById(R.id.lvHoldingCapacity);
        txtHoldCap=findViewById(R.id.txtHoldCap);
        lvAcknowledgeReamark=findViewById(R.id.lvAcknowledgeReamark);
        txtAcknowledgeReamrk=findViewById(R.id.txtAcknowledgeReamrk);
        txtCreatedbylbl=findViewById(R.id.txtCreatedbylbl);
        txtCreatedDate=findViewById(R.id.txtCreatedDate);
        lvAckBy=findViewById(R.id.lvAckBy);
        txtAckby=findViewById(R.id.txtAckby);
        lvAckDate=findViewById(R.id.lvAckDate);
        txtAckDate=findViewById(R.id.txtAckDate);
        btnAction=findViewById(R.id.btnAction);
        shpreRefresh=context.getSharedPreferences("ackRefresh",MODE_PRIVATE);

        if(shpreRefresh.getInt("fromlist",0)==2){
            btnAction.setVisibility(View.GONE);
        }
        if(shpreRefresh.getInt("fromlist",0)==3){
            btnAction.setVisibility(View.GONE);
        }
        if(shpreRefresh.getInt("fromlist",0)==4){
            btnAction.setVisibility(View.VISIBLE);
        }

        txtUserName.setText(mapdata.get("userName"));
        txtAdminName.setText(mapdata.get("companyName"));
        txtRemark.setText(mapdata.get("remark"));
        txtStatus.setText(mapdata.get("status"));
        if(mapdata.get("holdingCapacity").equals("null") || mapdata.get("holdingCapacity").equals("")){
            lvHoldingCapacity.setVisibility(View.GONE);
            txtHoldCap.setVisibility(View.GONE);
        }else {
            lvHoldingCapacity.setVisibility(View.VISIBLE);
            txtHoldCap.setText(mapdata.get("holdingCapacity"));
        }
        if(mapdata.get("achnowledgeRemark").equals("null") || mapdata.get("achnowledgeRemark").equals("")){
            lvAcknowledgeReamark.setVisibility(View.GONE);
            txtAcknowledgeReamrk.setVisibility(View.GONE);
        }else {
            lvAcknowledgeReamark.setVisibility(View.VISIBLE);
            txtAcknowledgeReamrk.setText(mapdata.get("achnowledgeRemark"));
        }
        txtCreatedbylbl.setText(mapdata.get("createdByName"));
        txtCreatedDate.setText(mapdata.get("createdDateStr"));
        if(mapdata.get("acknowledgeByName").equals("null") || mapdata.get("acknowledgeByName").equals("")){
            lvAckBy.setVisibility(View.GONE);
            txtAckby.setVisibility(View.GONE);
        }else {
            lvAckBy.setVisibility(View.VISIBLE);
            txtAckby.setText(mapdata.get("acknowledgeByName"));
        }

        if(mapdata.get("acknowledgeDateStr").equals("null")||mapdata.get("acknowledgeDateStr").equals("")){
            lvAckDate.setVisibility(View.GONE);
            txtAckDate.setVisibility(View.GONE);
        }else {
            lvAckDate.setVisibility(View.VISIBLE);
            txtAckDate.setText(mapdata.get("acknowledgeDateStr"));
        }

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ApproveAcknowledgeActivity.class);
                intent.putExtra("dataMap",mapdata);
                startActivity(intent);
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        rvBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}