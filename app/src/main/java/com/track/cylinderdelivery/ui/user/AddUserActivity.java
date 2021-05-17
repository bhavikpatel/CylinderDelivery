package com.track.cylinderdelivery.ui.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.Dashboard;
import com.track.cylinderdelivery.LoginActivity;
import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.ui.BaseActivity;
import com.track.cylinderdelivery.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddUserActivity extends BaseActivity {

    AppCompatSpinner spinner_name;
    Context context;
    EditText edtName,edtAddress1,edtAddress2,editCity,edtCountry,edtZipCode;
    EditText edtMobile,edtSecondaryMobile,edtEmail,edtPassword,edtSecondaryEmail;
    Button btnSubmit,btnCancel;
    private SharedPreferences spUserFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        spUserFilter=getSharedPreferences("userFilter",MODE_PRIVATE);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Add User");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Add User </font>"));
        //RefreshUserList=true;

        spinner_name=(AppCompatSpinner)findViewById(R.id.spinner_name);
        edtName=(EditText)findViewById(R.id.edtName);
        edtAddress1=(EditText)findViewById(R.id.edtAddress1);
        edtAddress2=(EditText)findViewById(R.id.edtAddress2);
        editCity=(EditText)findViewById(R.id.editCity);
        edtCountry=(EditText)findViewById(R.id.edtCountry);
        edtZipCode=(EditText)findViewById(R.id.edtZipCode);
        edtMobile=(EditText)findViewById(R.id.edtMobile);
        edtSecondaryMobile=(EditText)findViewById(R.id.edtSecondaryMobile);
        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        edtSecondaryEmail=(EditText)findViewById(R.id.edtSecondaryEmail);
        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnCancel=(Button)findViewById(R.id.btnCancel);

        ArrayList list = new ArrayList<String>();
        list.add("Select");
        list.add("01");
        list.add("02");
        ArrayAdapter<String> adp = new ArrayAdapter<String>
                (this, android.R.layout.simple_spinner_item, list);
        spinner_name.setAdapter(adp);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String FullName=edtName.getText().toString().trim();
                int CompanyId=1;
                String Address1=edtAddress1.getText().toString().trim();
                String Address2=edtAddress2.getText().toString().trim();
                String City=editCity.getText().toString().trim();
                String County=edtCountry.getText().toString().trim();
                String ZipCode=edtZipCode.getText().toString().trim();
                String Phone=edtMobile.getText().toString().trim();
                String SecondaryPhone=edtSecondaryMobile.getText().toString().trim();
                String Email=edtEmail.getText().toString().trim();
                String SecondaryEmail=edtSecondaryEmail.getText().toString().trim();
                String EmailPassword=edtPassword.getText().toString().trim();
                String UserType="Client";
                int CreatedBy=1;
                int ModifiedBy=1;
                int HoldingCapacity=10;

                if(validate(FullName,Address1,City,County,ZipCode,Phone,Email,EmailPassword,
                        Address2,SecondaryPhone,SecondaryEmail)){
                    try {
                        if(isNetworkConnected()){
                            callAddUserApi(FullName,CompanyId,Address1,Address2,City,County,ZipCode,Phone,
                                    SecondaryPhone,HoldingCapacity,Email,SecondaryEmail,EmailPassword,
                                    UserType,CreatedBy,ModifiedBy);
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

    private void callAddUserApi(String FullName, int CompanyId,String Address1,
                                String Address2, String City,String County,
                                String ZipCode,String Phone,String SecondaryPhone,
                                int HoldingCapacity,String Email,String SecondaryEmail,
                                String EmailPassword,String UserType,int CreatedBy
                                ,int ModifiedBy) throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(AddUserActivity.this, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"MobUser/AddEdit";
        final JSONObject jsonBody=new JSONObject();
        jsonBody.put("UserId",null);
        jsonBody.put("FullName",FullName+"");
        jsonBody.put("CompanyId",CompanyId);
        jsonBody.put("Address1",Address1+"");
        jsonBody.put("Address2",Address2+"");
        jsonBody.put("City",City+"");
        jsonBody.put("County",County+"");
        jsonBody.put("ZipCode",ZipCode+"");
        jsonBody.put("Phone",Phone+"");
        jsonBody.put("SecondaryPhone",SecondaryPhone+"");
        jsonBody.put("HoldingCapacity",HoldingCapacity);
        jsonBody.put("Email",Email+"");
        jsonBody.put("SecondaryEmail",SecondaryEmail+"");
        jsonBody.put("EmailPassword",EmailPassword+"");
        //jsonBody.put("AccNo",AccNo);
        jsonBody.put("UserType","Client");
        jsonBody.put("CreatedBy",CreatedBy);
        jsonBody.put("ModifiedBy",ModifiedBy);

        final String v = jsonBody.toString();

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
                            String Address2,String SecondaryPhone,String SecondaryEmail) {
        boolean valid = true;

        if (SecondaryEmail.isEmpty()) {
            edtSecondaryEmail.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryEmail.setError(null);
        }
        if (SecondaryPhone.isEmpty()) {
            edtSecondaryMobile.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryMobile.setError(null);
        }
        if (Address2.isEmpty()) {
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