package com.track.cylinderdelivery;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.ui.BaseActivity;
import com.track.cylinderdelivery.ui.cylinder.CylinderListAdapter;
import com.track.cylinderdelivery.utils.TransparentProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity {

    private TextView txtSignup,txtForgotPass;
    private Context context;
    private Button btn_login;
    private EditText edt_user,edt_password;
    private SharedPreferences settings;
    private static final int MY_SOCKET_TIMEOUT_MS = 100000;
   // private boolean loggedIN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context=this;
        txtSignup=(TextView)findViewById(R.id.txtSignup);
        btn_login=(Button)findViewById(R.id.btn_login);
        edt_user=(EditText)findViewById(R.id.edt_user_id);
        edt_password=(EditText)findViewById(R.id.edt_password);
        txtForgotPass=findViewById(R.id.txtForgotPass);
        edt_user.setText("admin@admin.com");
        edt_password.setText("admin@123");
        settings=getSharedPreferences("setting",MODE_PRIVATE);
       // loggedIN=settings.getBoolean("loggedIN",false);

        txtForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateemail()) {
                    if (isNetworkConnected()) {
                         callForgotPassApi(edt_user.getText().toString().trim());
                    }else {
                        Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    try {
                        if(isNetworkConnected()){
                            callLoginApi(edt_user.getText().toString().trim(), edt_password.getText().toString().trim());
                        }else {
                            Toast.makeText(context, "Kindly check your internet connectivity.", Toast.LENGTH_LONG).show();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });
        txtSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupActivity=new Intent(context,SignUp.class);
                startActivity(signupActivity);
                finish();
            }
        });
    }

    private void callForgotPassApi(String email) {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(LoginActivity.this, R.drawable.loader);
        progressDialog.show();
        String url = "http://test.hdvivah.in/Api/MobLogin/ForgotPassword?email="+email;
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
                    String message=j.getString("message");
                    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
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

    private void callLoginApi(String username, String password) throws JSONException {
        Log.d("Api Calling==>","Api Calling");
        final TransparentProgressDialog progressDialog = new TransparentProgressDialog(LoginActivity.this, R.drawable.loader);
        progressDialog.show();
        String url = BASE_URL+"MobLogin";
        final JSONObject jsonBody=new JSONObject();
        jsonBody.put("EmailId",username+"");
        jsonBody.put("UserPassword",password+"");
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
                                JSONObject jsonData=new JSONObject(jsonObject.getString("data")+"");
                                SharedPreferences.Editor editor = settings.edit();
                                editor.putString("userId",jsonData.getString("userId")+"");
                                editor.putString("fullName",jsonData.getString("fullName")+"");
                                editor.putString("email",jsonData.getString("email")+"");
                                editor.putString("userType",jsonData.getString("userType")+"");
                                editor.putString("companyId",jsonData.getString("companyId")+"");
                                editor.putBoolean("loggedIN",true);
                                editor.commit();
                                //JSONArray accessModel=jsonBody.getJSONArray("accessModel");
                                Intent signupActivity=new Intent(context,Dashboard.class);
                                startActivity(signupActivity);
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
/*        RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(jsonObjectRequest);*/
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }


    public boolean validate() {
        boolean valid = true;
        String email1 = edt_user.getText().toString().trim();
        String password1 = edt_password.getText().toString().trim();
        if (email1.isEmpty()) {
            edt_user.setError("Field is Required.");
            valid = false;
        } else {
            edt_user.setError(null);
        }
        if (password1.isEmpty()) {
            edt_password.setError("Field is Required.");
            valid = false;
        } else {
            edt_password.setError(null);
        }
        return valid;
    }
    public boolean validateemail() {
        boolean valid = true;
        String email1 = edt_user.getText().toString().trim();

        if (email1.isEmpty()) {
            edt_user.setError("Field is Required.");
            valid = false;
        } else {
            edt_user.setError(null);
        }
        return valid;
    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}