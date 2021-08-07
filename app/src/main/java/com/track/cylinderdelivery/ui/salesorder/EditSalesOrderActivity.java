package com.track.cylinderdelivery.ui.salesorder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import com.track.cylinderdelivery.ui.cylinder.CylinderQRActivity;
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
    EditText edtSoNumber,edtSoDate,edtSoGenerateby;
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
    private ArrayList<HashMap<String,String>> pendingSalesList;
    private String warehouseId;
    private Button btnCancel;
    Button btnSaveAndContinue;
    SharedPreferences spSorting;
    private String soNumber;
    private String SOGeneratedBy,soDate;
    private static final String BASE_URL = "http://test.hdvivah.in";
    LinearLayout lvTab1;
    RelativeLayout lvTab2;
    TextView txtClientInfo;
    TextView txtPurchaseOrderDetail;
    TextView txtPurchasodUnderline;
    TextView txtLineinfoUnderline;
    NiceSpinner nSPendingSales;
    private int pendingSalespos=0;
    private String soId;
    private String dnDetailId;
    private String productId;
    private ArrayList<HashMap<String,String>> pendingsalesList;
    ImageView btnScanCylinders;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;
    TextView txtCylinderNos;
    private ArrayList<String> qrcodeList;
    private Button btnAdd,btnLastSubmit,btnSaveAsDraft;
    private ArrayList<HashMap<String,String>> sODetailList;
    RecyclerView recyclerView;
    EditSODetailListAdapter sODetailListAdapter;
    TextView txtUserName11;


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
        txtUserName11=findViewById(R.id.txtUserName11);
        NSWarehouse.setVisibility(View.GONE);
        txtUserName11.setVisibility(View.GONE);
        edtSoGenerateby=findViewById(R.id.edtPOGeneratedBy);
        edtSoGenerateby.setText(mapdata.get("soGeneratedBy"));
        btnCancel=findViewById(R.id.btnCancel);
        btnSaveAndContinue=findViewById(R.id.btnSaveAndContinue);
        spSorting=context.getSharedPreferences("SOFilter",MODE_PRIVATE);
        lvTab1=findViewById(R.id.lvTab1);
        lvTab2=findViewById(R.id.lvTab2);
        txtClientInfo=findViewById(R.id.txtClientInfo);
        txtPurchaseOrderDetail=findViewById(R.id.txtPurchaseOrderDetail);
        txtPurchasodUnderline=findViewById(R.id.txtPurchasodUnderline);
        txtLineinfoUnderline=findViewById(R.id.txtLineinfoUnderline);
        lvTab1.setVisibility(View.VISIBLE);
        lvTab2.setVisibility(View.GONE);
        nSPendingSales=findViewById(R.id.NSPendingSales);
        btnScanCylinders=findViewById(R.id.btnScanCylinders);
        txtCylinderNos=findViewById(R.id.txtCylinderNos);
        qrcodeList=new ArrayList<String>();
        btnAdd=findViewById(R.id.btnAdd);
        recyclerView=findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        btnLastSubmit=findViewById(R.id.btnLastSubmit);
        btnSaveAsDraft=findViewById(R.id.btnSaveAsDraft);


        if(isNetworkConnected()) {
            callGetReadyforDeliveryDeliveryList();
        }else {
            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
        }


        btnSaveAsDraft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnLastSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sODetailList.size()==0){
                    Toast.makeText(context, "Kindly scan cylinder first.", Toast.LENGTH_LONG).show();
                }else if(isNetworkConnected()){
                    callSubmitSO();
                }else {
                    Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                }
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate1()){
                    if(isNetworkConnected()){
                        try {
                            callAddSOCylinder();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        btnScanCylinders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EditSalesOrderActivity.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    openQrScan();
                }
            }
        });
        txtClientInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvTab1.setVisibility(View.VISIBLE);
                lvTab2.setVisibility(View.GONE);
                txtPurchaseOrderDetail.setTextColor(getResources().getColor(R.color.lightGrey));
                txtClientInfo.setTextColor(getResources().getColor(R.color.green));
                txtLineinfoUnderline.setBackgroundColor(getResources().getColor(R.color.green));
                txtPurchasodUnderline.setBackgroundColor(getResources().getColor(R.color.lightGrey));
            }
        });
        btnSaveAndContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()){
                    SharedPreferences.Editor userFilterEditor = spSorting.edit();
                    userFilterEditor.putBoolean("dofilter",true);
                    userFilterEditor.commit();
                    soNumber=edtSoNumber.getText().toString();
                    //PoDate=edtPoDate.getText().toString();
                    SOGeneratedBy=edtSoGenerateby.getText().toString();
                    soDate=edtSoDate.getText().toString();
                    if(validate()){
                        try {
                            callAddEditSO();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        nSPendingSales.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                nSPendingSales.setError(null);
                Log.d("checkedId==>",position+"");
                hideSoftKeyboard(view);
                pendingSalespos=position;
                if(position!=0) {
                    soId=pendingSalesList.get(position-1).get("dnDetailId");
                    dnDetailId=pendingsalesList.get(position-1).get("dnDetailId");
                    productId=pendingsalesList.get(position-1).get("productId");
                }else {
                    //clintvalue="";
                    //clinttext="";
                }
            }
        });
        nSPendingSales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(v);
            }
        });

        NSWarehouse.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                NSWarehouse.setError(null);
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
    private boolean validate1() {
        boolean valid = true;
        if (qrcodeList.size()==0) {
            txtCylinderNos.setError("Field is Required.");
            valid = false;
        } else {
            txtCylinderNos.setError(null);
        }
        if (pendingSalespos<=0) {
            nSPendingSales.setError("Field is Required.");
            valid = false;
        } else {
            nSPendingSales.setError(null);
        }
        return valid;
    }
    private void openQrScan() {
        txtCylinderNos.setError(null);
        Intent intent=new Intent(context, CylinderQRActivity.class);
        intent.putExtra("scanlist",qrcodeList);
        startActivityForResult(intent,201);
    }
    private void callAddEditSO() throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"/Api/MobSalesOrder/AddEditSO";
        final JSONObject jsonBody=new JSONObject();
        jsonBody.put("SOId",Integer.parseInt(mapdata.get("soId")));
        jsonBody.put("DNId",Integer.parseInt(dnId));
        jsonBody.put("SONumber",soNumber);
        jsonBody.put("UserId",Integer.parseInt(clintvalue));
        jsonBody.put("SODate",soDate);
        jsonBody.put("SOGeneratedBy",SOGeneratedBy+"");
        jsonBody.put("CreatedBy",Integer.parseInt(settings.getString("userId","1")));
        jsonBody.put("WarehouseId",warehouseId);

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
                                Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_LONG).show();
                                //POId=jsonObject.getInt("data");
                                lvTab1.setVisibility(View.GONE);
                                lvTab2.setVisibility(View.VISIBLE);
                                txtPurchaseOrderDetail.setTextColor(getResources().getColor(R.color.green));
                                txtClientInfo.setTextColor(getResources().getColor(R.color.lightGrey));
                                txtLineinfoUnderline.setBackgroundColor(getResources().getColor(R.color.lightGrey));
                                txtPurchasodUnderline.setBackgroundColor(getResources().getColor(R.color.green));
                                if(isNetworkConnected()){
                                    callGetSalesOrderDetail();
                                }else {
                                    Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                                }
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

    private void callSubmitSO() {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobSalesOrder/SubmitSO?SOId="+Integer.parseInt(mapdata.get("soId"))+
                "&UserId="+Integer.parseInt(settings.getString("userId","1"));
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
                        Toast.makeText(context, j.getString("message")+"", Toast.LENGTH_LONG).show();
                        finish();
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
    private void callAddSOCylinder() throws JSONException {
        //isLoading=true;
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        /*if(podetailList==null){
            isLoading=false;
            isLastPage=false;
            pageno=0;*/
        progressDialog.show();
        /*}else {
            progressBar.setVisibility(View.VISIBLE);
        }*/
        String url = BASE_URL+"/Api/MobSalesOrder/AddSOCylinder";
        final JSONObject jsonBody=new JSONObject();
        jsonBody.put("SOId",Integer.parseInt(mapdata.get("soId")));
        jsonBody.put("ProductId",Integer.parseInt(productId));
        jsonBody.put("DNDetailId",Integer.parseInt(dnDetailId));
        jsonBody.put("DNid",Integer.parseInt(dnId));
        JSONArray jsonArrayCylList=new JSONArray(qrcodeList.toString());
        jsonBody.put("CylinderList",jsonArrayCylList);
        Log.d("jsonRequest==>",jsonBody.toString()+"");
        jsonBody.put("CreatedBy",Integer.parseInt(settings.getString("userId","1")));

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST,url,jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        //progressBar.setVisibility(View.GONE);
                        // isLoading=false;
                        Log.d("response==>",response.toString()+"");
                        try{
                            JSONObject jsonObject=response;
                            if(jsonObject.getBoolean("status")){
                                //Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_LONG).show();
                                JSONObject dataobj=jsonObject.getJSONObject("data");
                                //totalRecord= dataobj.getInt("totalRecord");
                                JSONArray jsonArray=dataobj.getJSONArray("list");
                                Boolean flgfirstload=false;
                                if(sODetailList==null){
                                    sODetailList=new ArrayList<>();
                                    //  flgfirstload=true;
                                }
                                for(int i=0;i<jsonArray.length();i++){
                                    HashMap<String,String> map=new HashMap<>();
                                    map.put("soDetailId", jsonArray.getJSONObject(i).getString("soDetailId"));
                                    map.put("soId", jsonArray.getJSONObject(i).getString("soId"));
                                    map.put("productId", jsonArray.getJSONObject(i).getString("productId"));
                                    map.put("productName",jsonArray.getJSONObject(i).getString("productName"));
                                    map.put("cylinderProductMappingId", jsonArray.getJSONObject(i).getString("cylinderProductMappingId"));
                                    map.put("dnDetailId",jsonArray.getJSONObject(i).getString("dnDetailId"));
                                    map.put("cylinderID",jsonArray.getJSONObject(i).getString("cylinderID"));
                                    map.put("cylinderList",jsonArray.getJSONObject(i).getString("cylinderList"));
                                    map.put("createdBy",jsonArray.getJSONObject(i).getString("createdBy"));
                                    map.put("createdDate",jsonArray.getJSONObject(i).getString("createdDate"));
                                    map.put("dNid",jsonArray.getJSONObject(i).getString("dNid"));
                                    map.put("createdByName",jsonArray.getJSONObject(i).getString("createdByName"));
                                    map.put("createdDateStr",jsonArray.getJSONObject(i).getString("createdDateStr"));
                                    sODetailList.add(map);
                                }
                                sODetailListAdapter=new EditSODetailListAdapter(sODetailList,context);
                                recyclerView.setAdapter(sODetailListAdapter);

/*                                if(podetailList.size()>=totalRecord){
                                    isLastPage=true;
                                }
                                if(flgfirstload){
                                    flgfirstload=false;
                                    productAddListAdapter=new ProductAddListAdapter(podetailList,context);
                                    recyclerView.setAdapter(productAddListAdapter);
                                }else {
                                    productAddListAdapter.notifyDataSetChanged();
                                }*/

                                Toast.makeText(context,jsonObject.getString("message").toString()+"",Toast.LENGTH_SHORT).show();
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
                        /*progressBar.setVisibility(View.GONE);
                        isLoading=false;*/
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonObjectRequest);
    }
    private void callGetSalesOrderDetail() {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobSalesOrder/GetSalesOrderDetail?search=&pageno=0&totalinpage="+Integer.MAX_VALUE+
                "&SortBy=&Sort=desc&SOId="+Integer.parseInt(mapdata.get("soId"))+"&DNId="+Integer.parseInt(dnId)+"&UserId="+Integer.parseInt(clintvalue);
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
                    pendingsalesList=new ArrayList<>();
                    JSONArray datalist=j.getJSONArray("list");
                    List<String> imtes=new ArrayList<>();
                    imtes.add("Select");
                    for(int i=0;i<datalist.length();i++){
                        HashMap<String,String> map=new HashMap<>();
                        JSONObject dataobj=datalist.getJSONObject(i);
                        map.put("dnDetailId",dataobj.getInt("dnDetailId")+"");
                        map.put("dnId",dataobj.getString("dnId"));
                        map.put("companyId",dataobj.getString("companyId"));
                        map.put("userId",dataobj.getString("userId"));
                        map.put("productId",dataobj.getString("productId"));
                        map.put("productName",dataobj.getString("productName"));
                        map.put("poDetailId",dataobj.getString("poDetailId"));
                        map.put("quantity",dataobj.getString("quantity"));
                        map.put("createdBy",dataobj.getString("createdBy"));
                        map.put("createdDate",dataobj.getString("createdDate"));
                        map.put("createdByName",dataobj.getString("createdByName"));
                        map.put("createdDateStr",dataobj.getString("createdDateStr"));
                        map.put("userName",dataobj.getString("userName"));
                        map.put("deliveryNoteNo",dataobj.getString("deliveryNoteNo"));
                        map.put("poNo",dataobj.getString("poNo"));
                        map.put("cylinderList",dataobj.getString("cylinderList"));
                        map.put("cylinders",dataobj.getString("cylinders"));

                        imtes.add(dataobj.getString("productName") +
                                "/"+dataobj.getString("quantity")+
                                "/"+dataobj.getString("userName")+
                                "/"+dataobj.getString("cylinderList"));
                        pendingsalesList.add(map);
                    }
                    nSPendingSales.attachDataSource(imtes);

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

    private boolean validate() {
        boolean valid = true;

        if (soNumber.isEmpty()) {
            edtSoNumber.setError("Field is Required.");
            valid = false;
        } else {
            edtSoNumber.setError(null);
        }
        if(soDate.isEmpty()){
            edtSoDate.setError("Field is Required.");
            valid=false;
        }else {
            edtSoDate.setError(null);
        }
        if(delnotepos<=0){
            NsDeliveyNote.setError("Field is Required.");
            valid=false;
        }else {
            NsDeliveyNote.setError(null);
        }
/*        if(wareHousepos<=0){
            NSWarehouse.setError("Field is Required.");
            valid=false;
        }else {
            NSWarehouse.setError(null);
        }*/

        if(SOGeneratedBy.isEmpty()){
            edtSoGenerateby.setError("Field is Required.");
            valid=false;
        }else {
            edtSoGenerateby.setError(null);
        }
        return valid;
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
                        NSWarehouse.setVisibility(View.VISIBLE);
                        txtUserName11.setVisibility(View.VISIBLE);
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
                                //callGetUserWarehouse(clintvalue);
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

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
    public void hideSoftKeyboard(View view){
        InputMethodManager imm =(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    public void callChangeCompanyStatus(String soDetailId) {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(context, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobSalesOrder/DeleteSODetail?SODetailId="+Integer.parseInt(soDetailId);
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
                        for(int i=0;i<sODetailList.size();i++){
                            if(sODetailList.get(i).get("soDetailId").equals(soDetailId)){
                                sODetailList.remove(i);
                                break;
                            }
                        }
                        sODetailListAdapter.notifyDataSetChanged();
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
}