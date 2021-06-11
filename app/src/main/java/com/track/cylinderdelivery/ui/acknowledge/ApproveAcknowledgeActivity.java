package com.track.cylinderdelivery.ui.acknowledge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import com.track.cylinderdelivery.ui.BaseActivity;
import com.track.cylinderdelivery.ui.user.UserListAdapter;
import com.track.cylinderdelivery.utils.TransparentProgressDialog;

import org.angmarch.views.NiceSpinner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ApproveAcknowledgeActivity extends BaseActivity {
    Context context;
    EditText edtDate;
    Calendar myCalendar;
    NiceSpinner spinarStatus;
    List<String> statusType;
    Button btnAckno,btnCancel;
    TextView txtAdminName,txtUserName,txtAddress1,txtAddress2,txtCity,txtCountry;
    private HashMap<String, String> mapdata;
    LinearLayout lvCompanyName,lvUserName,lvCountry,lvAddress1,lvAddress2;
    LinearLayout lvCity,lvPhone,lvSecondaryMob,lvEmail,lvSecondaryMail,lvPermonthReq;
    LinearLayout lvTextnumber,lvUserRemark,lvparent;
    TextView txtPhoneNo,txtSecondaryMobile,txtEmail,txtSecondaryEmail,txtPermonthReq;
    TextView txtTexNumber,txtUserRemark;
    private static final int MY_SOCKET_TIMEOUT_MS = 5000;
    private HashMap<String, String> map;
    EditText edtHoldingCapacity,edtRemark;
    private SharedPreferences settings;
    RelativeLayout rvParent;
    private SharedPreferences shpreRefresh;
    private int AcknowledgeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approve_acknowledge);
        context=this;
        mapdata= (HashMap<String, String>) getIntent().getSerializableExtra("dataMap");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Add User");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Acknowledge </font>"));
        edtDate=findViewById(R.id.edtDate);
        settings=context.getSharedPreferences("setting",MODE_PRIVATE);
        shpreRefresh=getSharedPreferences("ackRefresh",MODE_PRIVATE);
        spinarStatus=findViewById(R.id.spinarStatus);
        btnAckno=findViewById(R.id.btnSubmit);
        btnCancel=findViewById(R.id.btnCancel);
        statusType=new LinkedList<>();
        statusType.add("Pending");
        statusType.add("Approved");
        statusType.add("Rejected");
        spinarStatus.attachDataSource(statusType);
        spinarStatus.setSelectedIndex(1);
        txtAdminName=findViewById(R.id.txtAdminName);
        lvCompanyName=findViewById(R.id.lvCompanyName);
        lvUserName=findViewById(R.id.lvUserName);
        txtUserName=findViewById(R.id.txtUserName);
        lvCountry=findViewById(R.id.lvCountry);
        lvAddress1=findViewById(R.id.lvAddress1);
        txtAddress1=findViewById(R.id.txtAddress1);
        lvAddress2=findViewById(R.id.lvAddress2);
        txtAddress2=findViewById(R.id.txtAddress2);
        txtCity=findViewById(R.id.txtCity);
        lvCity=findViewById(R.id.lvCity);
        txtCountry=findViewById(R.id.txtCountry);
        lvPhone=findViewById(R.id.lvPhone);
        txtPhoneNo=findViewById(R.id.txtPhoneNo);
        lvSecondaryMob=findViewById(R.id.lvSecondaryMob);
        txtSecondaryMobile=findViewById(R.id.txtSecondaryMobile);
        lvEmail=findViewById(R.id.lvEmail);
        txtEmail=findViewById(R.id.txtEmail);
        lvSecondaryMail=findViewById(R.id.lvSecondaryMail);
        txtSecondaryEmail=findViewById(R.id.txtSecondaryEmail);
        lvPermonthReq=findViewById(R.id.lvPermonthReq);
        txtPermonthReq=findViewById(R.id.txtPermonthReq);
        lvTextnumber=findViewById(R.id.lvTextnumber);
        txtTexNumber=findViewById(R.id.txtTexNumber);
        lvUserRemark=findViewById(R.id.lvUserRemark);
        txtUserRemark=findViewById(R.id.txtUserRemark);
        edtHoldingCapacity=findViewById(R.id.edtHoldingCapacity);
        edtRemark=findViewById(R.id.edtRemark);
        rvParent=findViewById(R.id.rvParent);
        lvparent=findViewById(R.id.lvparent);
        lvparent.setVisibility(View.VISIBLE);

        callGetUserDetail(Integer.parseInt(mapdata.get("userId")));


        btnAckno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate(edtHoldingCapacity.getText().toString(),edtDate.getText().toString(),edtRemark.getText().toString())){
                    int UserId= Integer.parseInt(mapdata.get("userId"));
                    AcknowledgeId=Integer.parseInt(mapdata.get("acknowledgeId"));
                    int HoldingCapacity=Integer.parseInt(edtHoldingCapacity.getText().toString()+"");
                    String AcknowledgeDate=edtDate.getText().toString();
                    String Status=spinarStatus.getSelectedItem().toString();
                    String AchnowledgeRemark=edtRemark.getText().toString();
                    int CreatedBy= Integer.parseInt(settings.getString("userId","1"));
                    int AcknowledgeBy= Integer.parseInt(settings.getString("userId","1"));
                    try {
                        callAppriveAcknowledge(UserId,HoldingCapacity,AcknowledgeDate,
                                Status,AchnowledgeRemark,CreatedBy,AcknowledgeBy);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        edtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(context, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void callAppriveAcknowledge(int userId, int holdingCapacity, String acknowledgeDate,
                                        String status, String achnowledgeRemark, int createdBy,
                                        int acknowledgeBy) throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"MobAcknowledge/ApproveAcknowledge";
        final JSONObject jsonBody=new JSONObject();
        jsonBody.put("UserId",userId);
        jsonBody.put("HoldingCapacity",holdingCapacity);
        jsonBody.put("AcknowledgeDate",acknowledgeDate);
        jsonBody.put("Status",status+"");
        jsonBody.put("AchnowledgeRemark",achnowledgeRemark);
        jsonBody.put("CreatedBy",createdBy);
        jsonBody.put("AcknowledgeBy",acknowledgeBy);
        jsonBody.put("AcknowledgeId",AcknowledgeId);
        final String v = jsonBody.toString();
        Log.d("parambers==>",v);

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        lvparent.setVisibility(View.VISIBLE);
                        Log.d("response==>",response.toString()+"");
                        try{
                            JSONObject jsonObject=response;
                            if(jsonObject.getBoolean("status")){
                                Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_SHORT).show();
                                SharedPreferences.Editor userFilterEditor = shpreRefresh.edit();
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

    private void callGetUserDetail(int userId) {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobUser/GetUserbyId?UserId="+userId;
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
                            JSONObject data=j.getJSONObject("data");
                            map = new HashMap<>();
                            map.put("userId", data.getString("userId") + "");
                            map.put("fullName", data.getString("fullName") + "");

                            if(data.getString("fullName").equals("")||data.getString("fullName").equals("null")){
                                lvUserName.setVisibility(View.GONE);
                                txtUserName.setVisibility(View.GONE);
                            }else {
                                txtUserName.setText(data.getString("fullName"));
                            }

                            map.put("companyId", data.getString("companyId") + "");
                            map.put("address1", data.getString("address1") + "");
                            if(data.getString("address1").equals("") || data.getString("address1").equals("null")){
                                lvAddress1.setVisibility(View.GONE);
                                txtAddress1.setVisibility(View.GONE);
                            }else {
                                txtAddress1.setText(data.getString("address1"));
                            }

                            map.put("address2", data.getString("address2") + "");
                            if(data.getString("address2").equals("") || data.getString("address2").equals("null")){
                                lvAddress2.setVisibility(View.GONE);
                                txtAddress2.setVisibility(View.GONE);
                            }else {
                                txtAddress2.setText(data.getString("address2"));
                            }
                            map.put("city", data.getString("city") + "");
                            if(data.getString("city").equals("") || data.getString("city").equals("null")){
                                lvCity.setVisibility(View.GONE);
                                txtCity.setVisibility(View.GONE);
                            }else {
                                txtCity.setText(data.getString("address2"));
                            }
                            map.put("county", data.getString("county") + "");
                            if(data.getString("county").equals("") || data.getString("county").equals("null")){
                                lvCountry.setVisibility(View.GONE);
                                txtCountry.setVisibility(View.GONE);
                            }else {
                                txtCountry.setText(data.getString("county"));
                            }

                            map.put("zipCode", data.getString("zipCode") + "");
                            map.put("phone", data.getString("phone") + "");
                            if(data.getString("phone").equals("") || data.getString("phone").equals("null")){
                                lvPhone.setVisibility(View.GONE);
                                txtPhoneNo.setVisibility(View.GONE);
                            }else {
                                txtPhoneNo.setText(data.getString("phone"));
                            }
                            map.put("secondaryPhone", data.getString("secondaryPhone") + "");
                            if(data.getString("secondaryPhone").equals("") || data.getString("secondaryPhone").equals("null")){
                                lvSecondaryMob.setVisibility(View.GONE);
                                txtSecondaryMobile.setVisibility(View.GONE);
                            }else {
                                txtSecondaryMobile.setText(data.getString("secondaryPhone"));
                            }
                            map.put("holdingCapacity", data.getString("holdingCapacity") + "");
                            if(data.getString("holdingCapacity").equals("") || data.getString("holdingCapacity").equals("null")){
                                lvPermonthReq.setVisibility(View.GONE);
                                txtPermonthReq.setVisibility(View.GONE);
                            }else {
                                txtPermonthReq.setText(data.getString("holdingCapacity"));
                            }
                            map.put("taxNumber", data.getString("taxNumber") + "");
                            if(data.getString("taxNumber").equals("") || data.getString("taxNumber").equals("null")){
                                lvTextnumber.setVisibility(View.GONE);
                                txtTexNumber.setVisibility(View.GONE);
                            }else {
                                txtTexNumber.setText(data.getString("taxNumber"));
                            }

                            map.put("email", data.getString("email") + "");
                            if(data.getString("email").equals("") || data.getString("email").equals("null")){
                                    lvEmail.setVisibility(View.GONE);
                                txtEmail.setVisibility(View.GONE);
                            }else {
                                txtEmail.setText(data.getString("email"));
                            }
                            map.put("secondaryEmail", data.getString("secondaryEmail") + "");
                            if(data.getString("email").equals("") || data.getString("email").equals("null")){
                                lvSecondaryMail.setVisibility(View.GONE);
                                txtSecondaryEmail.setVisibility(View.GONE);
                            }else {
                                txtSecondaryEmail.setText(data.getString("secondaryEmail"));
                            }

                            map.put("emailPassword", data.getString("emailPassword") + "");
                            map.put("accNo", data.getString("accNo") + "");
                            map.put("userType", data.getString("userType") + "");
                            map.put("createdBy", data.getString("createdBy") + "");
                            map.put("createdByName", data.getString("createdByName") + "");
                            map.put("createdDate", data.getString("createdDate") + "");
                            map.put("createdDateStr", data.getString("createdDateStr") + "");
                            map.put("modifiedBy", data.getString("modifiedBy") + "");
                            map.put("modifiedDate", data.getString("modifiedDate") + "");
                            map.put("forgotPassword", data.getString("forgotPassword") + "");
                            map.put("forgotPasswordDate", data.getString("forgotPasswordDate") + "");
                            map.put("companyName", data.getString("companyName") + "");
                            if(data.getString("companyName").equals("") || data.getString("companyName").equals("null")){
                                lvCompanyName.setVisibility(View.GONE);
                                txtAdminName.setVisibility(View.GONE);
                            }else {
                                txtAdminName.setText(data.getString("companyName"));
                            }
                            map.put("companyType", data.getString("companyType") + "");
                            if(mapdata.get("remark").equals("") || mapdata.get("remark").equals("null")){
                                lvUserRemark.setVisibility(View.GONE);
                                txtUserRemark.setVisibility(View.GONE);
                            }else {
                                txtUserRemark.setText(mapdata.get("remark"));
                            }
                        }else {
                            Toast.makeText(context, j.getString("message"), Toast.LENGTH_LONG).show();
                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    private void updateLabel() {
        //String myFormat = "MM/dd/yy"; //In which you need put here
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDate.setText(sdf.format(myCalendar.getTime()));
    }
    public boolean validate(String fullName,String date,String remark) {
        boolean valid = true;

        if (fullName.isEmpty()) {
            edtHoldingCapacity.setError("Field is Required.");
            valid = false;
        } else {
            edtHoldingCapacity.setError(null);
        }
        if(date.isEmpty()){
            edtDate.setError("Field is Required.");
            valid=false;
        }else {
            edtDate.setError(null);
        }
        if(remark.isEmpty()){
            edtRemark.setError("Field is Required.");
            valid=false;
        }else{
            edtRemark.setError(null);
        }
        return valid;
    }
}