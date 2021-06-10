package com.track.cylinderdelivery.ui.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.ui.BaseActivity;
import com.track.cylinderdelivery.utils.TransparentProgressDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditUserActivity extends BaseActivity {

    Context context;
    EditText edtName,edtAddress1,edtAddress2,editCity,edtCountry,edtZipCode;
    EditText edtMobile,edtSecondaryMobile,edtEmail,edtPassword,edtSecondaryEmail;
    EditText edtHoldingCapacity;
    Button btnSubmit,btnCancel;
    HashMap<String, String> mapdata;
    private SharedPreferences spUserFilter;
    int holdingCapacity=10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);
        context=this;
        mapdata= (HashMap<String, String>) getIntent().getSerializableExtra("editData");
        Log.d("mapdata==>",mapdata+"");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        spUserFilter=getSharedPreferences("userFilter",MODE_PRIVATE);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Edit User");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Edit User </font>"));
        //RefreshUserList=true;
        //spinner_name=(AppCompatSpinner)findViewById(R.id.spinner_name);
        edtName= findViewById(R.id.edtName);
        edtAddress1= findViewById(R.id.edtAddress1);
        edtAddress2= findViewById(R.id.edtAddress2);
        editCity= findViewById(R.id.editCity);
        edtCountry= findViewById(R.id.edtCountry);
        edtZipCode= findViewById(R.id.edtZipCode);
        edtMobile= findViewById(R.id.edtMobile);
        edtSecondaryMobile= findViewById(R.id.edtSecondaryMobile);
        edtEmail= findViewById(R.id.edtEmail);
        edtPassword= findViewById(R.id.edtPassword);
        edtSecondaryEmail= findViewById(R.id.edtSecondaryEmail);
        btnSubmit= findViewById(R.id.btnSubmit);
        btnCancel= findViewById(R.id.btnCancel);
        edtHoldingCapacity=findViewById(R.id.edtHoldingCapacity);

        edtName.setText(mapdata.get("fullName"));
        edtAddress1.setText(mapdata.get("address1"));
        edtAddress2.setText(mapdata.get("address2"));
        editCity.setText(mapdata.get("city"));
        edtCountry.setText(mapdata.get("county"));
        edtZipCode.setText(mapdata.get("zipCode"));
        edtMobile.setText(mapdata.get("phone"));
        edtSecondaryMobile.setText(mapdata.get("secondaryPhone"));
        edtEmail.setText(mapdata.get("email"));
        edtSecondaryEmail.setText(mapdata.get("secondaryEmail"));
        edtPassword.setText(mapdata.get("emailPassword"));
        edtHoldingCapacity.setText(mapdata.get("holdingCapacity"));

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(edtName.getText().toString().trim(),edtAddress1.getText().toString().trim(),
                        editCity.getText().toString(),edtCountry.getText().toString().trim(),
                        edtZipCode.getText().toString().trim(),edtMobile.getText().toString().trim(),
                        edtEmail.getText().toString().trim(),edtPassword.getText().toString().trim(),
                        edtAddress2.getText().toString().trim(),edtSecondaryMobile.getText().toString().trim(),
                        edtSecondaryEmail.getText().toString().trim())){
                    try {
                        if(isNetworkConnected()){
                            if(edtHoldingCapacity.getText().toString().trim().length()!=0){
                                holdingCapacity=Integer.parseInt(edtHoldingCapacity.getText().toString());
                                callEditUserApi();
                            }
                        }else {
                            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callEditUserApi() throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(EditUserActivity.this, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"MobUser/AddEdit";
        final JSONObject jsonBody=new JSONObject();
        SharedPreferences setting= getSharedPreferences("setting",MODE_PRIVATE);
        jsonBody.put("UserId",Integer.parseInt(mapdata.get("userId")));
        jsonBody.put("FullName",edtName.getText().toString().trim()+"");
        jsonBody.put("CompanyId",Integer.parseInt(mapdata.get("companyId")));
        jsonBody.put("Address1",edtAddress1.getText().toString().trim()+"");
        jsonBody.put("Address2",edtAddress2.getText().toString().trim()+"");
        jsonBody.put("City",editCity.getText().toString().trim()+"");
        jsonBody.put("County",editCity.getText().toString().trim()+"");
        jsonBody.put("ZipCode",edtZipCode.getText().toString().trim()+"");
        jsonBody.put("Phone",edtMobile.getText().toString().trim()+"");
        jsonBody.put("SecondaryPhone",edtSecondaryMobile.getText().toString().trim()+"");
        jsonBody.put("HoldingCapacity",holdingCapacity);
        jsonBody.put("Email",edtEmail.getText().toString().trim()+"");
        jsonBody.put("SecondaryEmail",edtSecondaryEmail.getText().toString().trim()+"");
        jsonBody.put("EmailPassword",edtPassword.getText().toString().trim()+"");
        jsonBody.put("accNo",Integer.parseInt(mapdata.get("accNo")));
        jsonBody.put("UserType",mapdata.get("userType"));
        jsonBody.put("CreatedBy",Integer.parseInt(setting.getString("userId","")));
        jsonBody.put("ModifiedBy",Integer.parseInt(setting.getString("userId","")));

        Log.d("jsonRequest==>",jsonBody.toString()+"");

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        Log.d("response==>",response.toString()+"");
                        try{
                            JSONObject jsonObject=response;
                            if(jsonObject.getBoolean("status")){
                                Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor userFilterEditor = spUserFilter.edit();
                                userFilterEditor.putBoolean("dofilter",true);
                                userFilterEditor.commit();
                                finish();
                            }else {
                                Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Log.d("response==>",error.toString()+"");
                    }
                }){
            @Override
            public String getBodyContentType() {
                return "application/json";
            }
            @Override
            public Map<String, String> getHeaders() {
                HashMap map=new HashMap();
                map.put("content-type","application/json");
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    public boolean validate(String fullName,String Address1,String City,String County,
                            String ZipCode, String Phone, String Email,String EmailPassword,
                            String address2,String SecondaryMobile,String SecondaryEmail) {
        boolean valid = true;

/*        if (SecondaryEmail.isEmpty()) {
            edtSecondaryEmail.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryEmail.setError(null);
        }*/
/*        if (SecondaryMobile.isEmpty()) {
            edtSecondaryMobile.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryMobile.setError(null);
        }*/
        if (address2.isEmpty()) {
            edtAddress2.setError("Field is Required.");
            valid = false;
        } else {
            edtAddress2.setError(null);
        }
        if (fullName.isEmpty()) {
            edtName.setError("Field is Required.");
            valid = false;
        } else {
            edtName.setError(null);
        }
        if (Address1.isEmpty()) {
            edtAddress1.setError("Field is Required.");
            valid = false;
        } else {
            edtAddress1.setError(null);
        }
        if(City.isEmpty()){
            editCity.setError("Field is Required.");
            valid=false;
        }else {
            editCity.setError(null);
        }
        if(County.isEmpty()){
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
        if(Phone.isEmpty()){
            edtMobile.setError("Field is Required.");
            valid=false;
        }else{
            edtMobile.setError(null);
        }
        if(Email.isEmpty()){
            edtEmail.setError("Field is Required.");
            valid=false;
        }else{
            edtEmail.setError(null);
        }
        if(EmailPassword.isEmpty()){
            edtPassword.setError("Field is Required.");
            valid=false;
        }else{
            edtPassword.setError(null);
        }
        return valid;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}