package com.track.cylinderdelivery.ui.company;

import androidx.appcompat.app.AppCompatActivity;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.R;
import com.track.cylinderdelivery.utils.TransparentProgressDialog;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddCompany extends AppCompatActivity {

    Context context;
    private static final String BASE_URL = "http://test.hdvivah.in";
    Button btnSubmit,btnCancel;
    private EditText edtName;
    private EditText edtPersonName;
    private EditText edtAddress1;
    private EditText edtAddress2;
    private EditText editCity;
    private EditText edtCountry;
    private EditText edtZipCode;
    private EditText edtTexNumber;
    private EditText edtEmail;
    private EditText edtSecondaryEmail;
    private EditText edtMobile;
    private EditText edtSecondaryMobile,edtEmailPassword;
    private SharedPreferences settings;
    private SharedPreferences CompanyUpdate;
    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    ArrayList<HashMap<String,String>> companyTypeList;
    NiceSpinner spCompanyType;
    int companytypepos=-1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_company);
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Add Company");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Add Company</font>"));

        btnSubmit=(Button)findViewById(R.id.btnSubmit);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        spCompanyType=findViewById(R.id.spCompanyType);
        edtName=findViewById(R.id.edtName);
        edtPersonName=findViewById(R.id.edtPersonName);
        edtAddress1=findViewById(R.id.edtAddress1);
        edtAddress2=findViewById(R.id.edtAddress2);
        editCity=findViewById(R.id.editCity);
        edtCountry=findViewById(R.id.edtCountry);
        edtZipCode=findViewById(R.id.edtZipCode);
        edtTexNumber=findViewById(R.id.edtPinNumber);
        edtEmail=findViewById(R.id.edtEmail);
        edtSecondaryEmail=findViewById(R.id.edtSecondaryEmail);
        edtMobile=findViewById(R.id.edtMobile);
        edtSecondaryMobile=findViewById(R.id.edtSecondaryMobile);
        edtEmailPassword=findViewById(R.id.edtEmailPassword);
        settings=context.getSharedPreferences("setting",MODE_PRIVATE);
        CompanyUpdate=context.getSharedPreferences("companyUpdate",MODE_PRIVATE);
        if(isNetworkConnected()){
            getCompanyTypeList();
        }else {
            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(edtName.getText().toString().trim(),
                        edtPersonName.getText().toString().trim(),
                        edtAddress1.getText().toString().trim(),
                        editCity.getText().toString(),
                        edtCountry.getText().toString().trim(),
                        edtZipCode.getText().toString().trim(),
                        edtTexNumber.getText().toString().trim(),
                        edtMobile.getText().toString().trim(),
                        edtEmail.getText().toString().trim(),
                        edtSecondaryEmail.getText().toString().trim(),
                        edtSecondaryMobile.getText().toString().trim(),
                        edtAddress2.getText().toString().trim())){
                    try {
                        if(isNetworkConnected()){
                            callEditCompanyApi();
                        }else {
                            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        spCompanyType.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                Log.d("checkedId==>",position+"");
                hideSoftKeyboard(view);
                companytypepos=position;
            }
        });
        spCompanyType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });
    }

    private void callEditCompanyApi() throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"/api/MobCompany/AddEdit";
        final JSONObject jsonBody=new JSONObject();
        SharedPreferences setting= getSharedPreferences("setting",MODE_PRIVATE);
        jsonBody.put("CompanyId",null);
        jsonBody.put("CompanyName",edtName.getText().toString().trim()+"");
        jsonBody.put("AdminName",edtPersonName.getText().toString().trim()+"");
        jsonBody.put("Address1",edtAddress1.getText().toString().trim()+"");
        jsonBody.put("Address2",edtAddress2.getText().toString().trim()+"");
        jsonBody.put("City",editCity.getText().toString().trim()+"");
        jsonBody.put("County",edtCountry.getText().toString().trim()+"");
        jsonBody.put("ZipCode",edtZipCode.getText().toString().trim()+"");
        jsonBody.put("Phone",edtMobile.getText().toString().trim()+"");
        jsonBody.put("SecondaryPhone",edtSecondaryMobile.getText().toString().trim()+"");
        jsonBody.put("Email",edtEmail.getText().toString().trim()+"");
        jsonBody.put("SecondaryEmail",edtSecondaryEmail.getText().toString().trim()+"");
        jsonBody.put("EmailPassword","");
        jsonBody.put("CreatedBy",Integer.parseInt(settings.getString("userId","1")));
        jsonBody.put("ModifiedBy",Integer.parseInt(settings.getString("userId","1")));
        jsonBody.put("CompanyType",companyTypeList.get(companytypepos-1).get("value")+"");
        jsonBody.put("TaxNumber",edtTexNumber.getText().toString().trim()+"");

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
                                SharedPreferences.Editor editor = CompanyUpdate.edit();
                                editor.putBoolean("refresh",true);
                                editor.commit();
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

    public boolean validate(String fullName,String CopmanyName,String Address1,String City,String County,
                            String ZipCode,String TaxNumber, String Phone, String Email,
                            String SecondaryEmail,String SecondaryPhome,String Address2) {
        boolean valid = true;

        if (Address2.isEmpty()) {
            edtAddress2.setError("Field is Required.");
            valid = false;
        } else {
            edtAddress2.setError(null);
        }
        if (SecondaryPhome.isEmpty()) {
            edtSecondaryMobile.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryMobile.setError(null);
        }

        if (fullName.isEmpty()) {
            edtName.setError("Field is Required.");
            valid = false;
        } else {
            edtName.setError(null);
        }
        if (CopmanyName.isEmpty()) {
            edtPersonName.setError("Field is Required.");
            valid = false;
        } else {
            edtPersonName.setError(null);
        }
        if (companytypepos<=0) {
            spCompanyType.setError("Field is Required.");
            valid = false;
        } else {
            spCompanyType.setError(null);
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
        if(TaxNumber.isEmpty()){
            edtTexNumber.setError("Field is Required.");
            valid=false;
        }else{
            edtTexNumber.setError(null);
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
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (edtEmail.getText().toString().matches(emailPattern)) {
            edtEmail.setError(null);
        }else{
            edtEmail.setError("valid email address.");
            valid=false;
        }
        if (SecondaryEmail.isEmpty()) {
            edtSecondaryEmail.setError("Field is Required.");
            valid = false;
        } else {
            edtSecondaryEmail.setError(null);
        }
        if (edtSecondaryEmail.getText().toString().matches(emailPattern)) {
            edtSecondaryEmail.setError(null);
        }else{
            edtSecondaryEmail.setError("valid email address.");
            valid=false;
        }

        return valid;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getCompanyTypeList() {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobCompany/CompanyTypeList";
        Log.d("request==>",url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                url,new Response.Listener<String>() {
            @Override
            public void onResponse(String Response) {
                progressDialog.dismiss();
                Log.d("resonse ==>",Response+"");
                JSONObject j;
                try {
                    j = new JSONObject(Response);
                    if(j.getBoolean("status")){
                        JSONArray jsonArray=j.getJSONArray("data");
                        companyTypeList = new ArrayList<>();
                        List<String> imtes=new ArrayList<>();
                        imtes.add("Select");
                        for(int i=0;i<jsonArray.length();i++) {
                            HashMap<String, String> map = new HashMap<>();
                            map.put("value", jsonArray.getJSONObject(i).getString("value")+"");
                            map.put("text", jsonArray.getJSONObject(i).getString("text") + "");
                            map.put("disabled",jsonArray.getJSONObject(i).getBoolean("disabled")+"");
                            map.put("group",jsonArray.getJSONObject(i).getString("group")+"");
                            map.put("selected",jsonArray.getJSONObject(i).getBoolean("selected")+"");

                            imtes.add(jsonArray.getJSONObject(i).getString("value") + "");
                            companyTypeList.add(map);
                        }
                        spCompanyType.attachDataSource(imtes);
                    }else {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //progressDialog.dismiss();
                String message = null;
                if (error instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                } else if (error instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                } else if (error instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                } else if (error instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                }
                Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                Log.d("error==>",message+"");
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
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(stringRequest);
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}