package com.track.cylinderdelivery.ui.salesorder;

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

public class EditSalesOrderActivity extends AppCompatActivity {

    private HashMap<String, String> mapdata;
    EditSalesOrderActivity context;
    EditText edtSoNumber,edtSoDate;
    NiceSpinner NsDeliveyNote,NSClient;
    private SharedPreferences settings;
    private ArrayList<HashMap<String,String>> deliveryList;
    private int delnotepos=0;
    private String dnNumber;
    private String dnId;
    private ArrayList<HashMap<String,String>> customerList;
    private int clientpos=0;
    private static final int MY_SOCKET_TIMEOUT_MS = 10000;
    NiceSpinner NSWarehouse;
    private String clintvalue;
    private String clinttext;
    private int wareHousepos=0;
    private ArrayList<HashMap<String,String>> warehouseList;
    private String warehouseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_sales_order);
        mapdata= (HashMap<String, String>) getIntent().getSerializableExtra("editData");
        context=this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Drawable upArrow =  ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
        upArrow.setColorFilter(ContextCompat.getColor(context, R.color.black), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setTitle("Edit Sales Order");
        getSupportActionBar().setTitle(Html.fromHtml("<font color='#734CEA'>Edit Sales Order</font>"));
        settings=context.getSharedPreferences("setting",MODE_PRIVATE);
        edtSoNumber=findViewById(R.id.edtSoNumber);
        edtSoNumber.setText(mapdata.get("soNumber"));
        edtSoDate=findViewById(R.id.edtSoDate);
        edtSoDate.setText(mapdata.get("strDNDate"));
        NsDeliveyNote=findViewById(R.id.NSUserName);
        NSClient=findViewById(R.id.NSClient);
        NSWarehouse=findViewById(R.id.NSWarehouse);

        if(isNetworkConnected()) {
            callGetReadyforDeliveryDeliveryList();
        }else {
            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
        }

        NSWarehouse.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                NSClient.setError(null);
                Log.d("checkedId==>",position+"");
                hideSoftKeyboard(view);
                wareHousepos=position;
                if(position!=0) {
                    warehouseId=warehouseList.get(position-1).get("warehouseId");
                }else {
                    //clintvalue="";
                    //clinttext="";
                }
            }
        });
        NSWarehouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });
        NSClient.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                NSClient.setError(null);
                Log.d("checkedId==>",position+"");
                hideSoftKeyboard(view);
                clientpos=position;
                if(position!=0) {
                    clintvalue=customerList.get(position-1).get("value");
                    clinttext=customerList.get(position-1).get("text");
                    if(isNetworkConnected()){
                        callGetUserWarehouse(clintvalue);
                    }else {
                        Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                    }
                }else {
                    clintvalue="";
                    clinttext="";
                }
            }
        });
        NSClient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });
        NsDeliveyNote.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                NsDeliveyNote.setError(null);
                Log.d("checkedId==>",position+"");
                hideSoftKeyboard(view);
                delnotepos=position;
                if(position!=0) {
                    dnNumber=deliveryList.get(position-1).get("dnNumber");
                    dnId=deliveryList.get(position-1).get("dnId");
                    if(isNetworkConnected()){
                        if(dnId!=null){
                            callgetDeliveryNoteCustomerList(dnId);
                        }
                    }else {
                        Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                    }
                }else {
                    List<String> imtes=new ArrayList<>();
                    imtes.add("Select");
                    NSClient.attachDataSource(imtes);
                    NSClient.setSelectedIndex(0);
                }
            }
        });
        NsDeliveyNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });
    }

    private void callGetUserWarehouse(String clintvalue) {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobWarehouse/GetUserWarehouse?UserId="+Integer.parseInt(clintvalue);
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
                        warehouseList=new ArrayList<>();
                        JSONArray datalist=j.getJSONArray("data");
                        List<String> imtes=new ArrayList<>();
                        imtes.add("Select");
                        for(int i=0;i<datalist.length();i++){
                            HashMap<String,String> map=new HashMap<>();
                            JSONObject dataobj=datalist.getJSONObject(i);
                            map.put("warehouseId",dataobj.getInt("warehouseId")+"");
                            map.put("name",dataobj.getString("name"));
                            if(mapdata.get("warehouseId").equals(dataobj.getString("warehouseId"))){
                                wareHousepos=i;
                            }
                            imtes.add(dataobj.getString("name") + "");
                            warehouseList.add(map);
                        }
                        NSWarehouse.attachDataSource(imtes);
                        NSWarehouse.setSelectedIndex(wareHousepos+1);
                    }else {
                        Toast.makeText(context, j.getString("message")+"", Toast.LENGTH_LONG).show();
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

    private void callgetDeliveryNoteCustomerList(String dnId) {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobSalesOrder/getDeliveryNoteCustomerList?DNId="+Integer.parseInt(dnId);
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
                        customerList=new ArrayList<>();
                        JSONArray datalist=j.getJSONArray("data");
                        List<String> imtes=new ArrayList<>();
                        imtes.add("Select");
                        for(int i=0;i<datalist.length();i++){
                            HashMap<String,String> map=new HashMap<>();
                            JSONObject dataobj=datalist.getJSONObject(i);
                            map.put("value",dataobj.getInt("value")+"");
                            map.put("text",dataobj.getString("text"));
                            if(mapdata.get("username").equals(dataobj.getString("text"))){
                                clientpos=i+1;
                            }
                            imtes.add(dataobj.getString("text") + "");
                            customerList.add(map);
                        }
                        NSClient.attachDataSource(imtes);
                        NSClient.setSelectedIndex(clientpos);
                        if(clientpos!=0){
                            clintvalue=customerList.get(clientpos-1).get("value");
                            clinttext=customerList.get(clientpos-1).get("text");
                            if(isNetworkConnected()){
                                callGetUserWarehouse(clintvalue);
                            }else {
                                Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        Toast.makeText(context, j.getString("message")+"", Toast.LENGTH_LONG).show();
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
    private void callGetReadyforDeliveryDeliveryList() {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobSalesOrder/GetReadyforDeliveryDeliveryList?CompanyId="+Integer.parseInt(settings.getString("companyId","1"));
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
                        deliveryList=new ArrayList<>();
                        JSONArray datalist=j.getJSONArray("data");
                        List<String> imtes=new ArrayList<>();
                        imtes.add("Select");
                        for(int i=0;i<datalist.length();i++){
                            HashMap<String,String> map=new HashMap<>();
                            JSONObject dataobj=datalist.getJSONObject(i);
                            map.put("dnId",dataobj.getInt("dnId")+"");
                            map.put("dnNumber",dataobj.getString("dnNumber"));
                            if(mapdata.get("dnNo").equals(dataobj.getString("dnNumber"))){
                                delnotepos=i+1;
                            }
                            imtes.add(dataobj.getString("dnNumber") + "");
                            deliveryList.add(map);
                        }
                        NsDeliveyNote.attachDataSource(imtes);
                        NsDeliveyNote.setSelectedIndex(delnotepos);
                        if(delnotepos!=0){
                            dnNumber=deliveryList.get(delnotepos-1).get("dnNumber");
                            dnId=deliveryList.get(delnotepos-1).get("dnId");
                            if(isNetworkConnected()){
                                if(dnId!=null){
                                    callgetDeliveryNoteCustomerList(dnId);
                                }
                            }else {
                                Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                            }
                        }
                    }else {
                        Toast.makeText(context, j.getString("message")+"", Toast.LENGTH_LONG).show();
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}