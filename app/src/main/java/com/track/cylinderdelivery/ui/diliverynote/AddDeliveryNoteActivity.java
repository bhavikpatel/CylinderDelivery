package com.track.cylinderdelivery.ui.diliverynote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.ui.cylinder.AddCylinderActivity;
import com.track.cylinderdelivery.ui.cylinder.CylinderQRActivity;

import java.util.ArrayList;

public class AddDeliveryNoteActivity extends AppCompatActivity {

    Context context;
    /////////////////////////////////////1 part

    /////////////////////////////////////2 part
    ImageView btnScanCylinders;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    TextView txtCylinderNos;
    ArrayList<String> qrcodeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_note);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Add Delivery Note");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Add Delivery Note</font>"));

        ///////////////////////////////////////2..part
        btnScanCylinders=findViewById(R.id.btnScanCylinders);
        txtCylinderNos=findViewById(R.id.txtCylinderNos);
        qrcodeList=new ArrayList<String>();

        txtCylinderNos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCylinderNos.setError(null);
            }
        });
        btnScanCylinders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(AddDeliveryNoteActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    openQrScan();
                }
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==201){
            try {
                qrcodeList = (ArrayList<String>) data.getSerializableExtra("scanlist");
                txtCylinderNos.setText(qrcodeList.toString()+"");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                Log.i("Camera", "G : " + grantResults[0]);
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    openQrScan();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    if (ActivityCompat.shouldShowRequestPermissionRationale
                            (this, Manifest.permission.CAMERA)) {
                        //showAlert();
                    } else {

                    }
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    private void openQrScan() {
        txtCylinderNos.setError(null);
        Intent intent=new Intent(context, CylinderQRActivity.class);
        intent.putExtra("scanlist",qrcodeList);
        startActivityForResult(intent,201);
    }
    public boolean validate() {
        boolean valid = true;
        if (qrcodeList.size()==0) {
            txtCylinderNos.setError("Field is Required.");
            valid = false;
        } else {
            txtCylinderNos.setError(null);
        }
  /*      if (wharehouspos<=0) {
            spWarehouse.setError("Field is Required.");
            valid = false;
        } else {
            spWarehouse.setError(null);
        }
        if (ManufacturingDate.isEmpty()) {
            edtManufacturingDate.setError("Field is Required.");
            valid = false;
        } else {
            edtManufacturingDate.setError(null);
        }
        if(Company.isEmpty()){
            edtCompanyName.setError("Field is Required.");
            valid=false;
        }else {
            edtCompanyName.setError(null);
        }
        if(Address1.isEmpty()){
            edtAddress1.setError("Field is Required.");
            valid=false;
        }else {
            edtAddress1.setError(null);
        }
        if(Address2.isEmpty()){
            edtAddress2.setError("Field is Required.");
            valid=false;
        }else {
            edtAddress2.setError(null);
        }
        if(City.isEmpty()){
            editCity.setError("Field is Required.");
            valid=false;
        }else {
            editCity.setError(null);
        }
        if(Country.isEmpty()){
            edtCountry.setError("Field is Required.");
            valid=false;
        }else{
            edtCountry.setError(null);
        }
        if(ZipCode.isEmpty()){
            edtZipCode.setError("Field is Required.");
            valid=false;
        }else{
            edtZipCode.setError(null);
        }
        if(ValueComanyName.isEmpty()){
            edtValveCompanyName.setError("Field is Required.");
            valid=false;
        }else{
            edtValveCompanyName.setError(null);
        }
        if(PurchaeDate.isEmpty()){
            edtPurchaesDate.setError("Field is Required.");
            valid=false;
        }else{
            edtPurchaesDate.setError(null);
        }
        if(ExpireDate.isEmpty()){
            edtExpireDate.setError("Field is Required.");
            valid=false;
        }else{
            edtExpireDate.setError(null);
        }
        if(PaintExpireDays.isEmpty()){
            edtPaintExpireMonth.setError("Field is Required.");
            valid=false;
        }else {
            edtExpireDate.setError(null);
        }
        if(TestingPeriodDays.isEmpty()){
            edtTestingPeriodMonth.setError("Field is Required.");
            valid=false;
        }else {
            edtTestingPeriodMonth.setError(null);
        }*/
        return valid;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}