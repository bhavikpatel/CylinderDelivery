package com.track.cylinderdelivery.ui.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.track.cylinderdelivery.R;

import java.util.HashMap;

public class UserDetailActivity extends AppCompatActivity {

    RelativeLayout rvBlank;
    TextView txtAdminName,txtAddress,txtPinNumber,txtPhoneNo;
    TextView txtEmail,txtCreatedby,txtCreatedDate,txtCompanyName;
    private HashMap<String, String> mapdata;
    Context context;
    ImageView btnCancel,btnAckno,btnEdit;
    LinearLayout lvCompanyName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_user_detail);
        mapdata= (HashMap<String, String>) getIntent().getSerializableExtra("editData");
        context=this;
        rvBlank=findViewById(R.id.rvBlank);
        txtCompanyName=findViewById(R.id.txtCompanyName);
        txtAdminName=findViewById(R.id.txtAdminName);
        txtAddress=findViewById(R.id.txtAddress);
        txtPinNumber=findViewById(R.id.txtPinNumber);
        txtPhoneNo=findViewById(R.id.txtPhoneNo);
        txtEmail=findViewById(R.id.txtEmail);
        txtCreatedby=findViewById(R.id.txtCreatedby);
        txtCreatedDate=findViewById(R.id.txtCreatedDate);
        btnCancel=findViewById(R.id.btnCancel);
        btnAckno=findViewById(R.id.btnAckno);
        btnEdit=findViewById(R.id.btnEdit);
        lvCompanyName=findViewById(R.id.lvCompanyName);


        txtCompanyName.setText(mapdata.get("fullName")+"");
        if(mapdata.get("companyName").equals("null") || mapdata.get("companyName").equals("")){
            txtAdminName.setVisibility(View.GONE);
            lvCompanyName.setVisibility(View.GONE);
        }
        txtAdminName.setText(mapdata.get("companyName")+"");
        String address=mapdata.get("address1")+","+mapdata.get("address2")+
                ","+mapdata.get("city")+","+mapdata.get("county");
        txtAddress.setText(address);
        txtPinNumber.setText(mapdata.get("zipCode")+"");
        txtPhoneNo.setText(mapdata.get("phone")+"");
        txtEmail.setText(mapdata.get("email")+"");
        txtCreatedby.setText(mapdata.get("createdByName")+"");
        txtCreatedDate.setText(mapdata.get("createdDateStr")+"");
        if(mapdata.get("accNo").equals("0") && (!mapdata.get("userType").equals("Employee"))){
            btnAckno.setVisibility(View.VISIBLE);
        }else {
            btnAckno.setVisibility(View.GONE);
        }

        btnAckno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,UserAcknoActivity.class);
                intent.putExtra("editData",mapdata);
                startActivity(intent);
                finish();
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,EditUserActivity.class);
                intent.putExtra("editData",mapdata);
                startActivity(intent);
                finish();
            }
        });
        rvBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
  //      getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.hold_top, R.anim.exit_in_bottom);
    }
}