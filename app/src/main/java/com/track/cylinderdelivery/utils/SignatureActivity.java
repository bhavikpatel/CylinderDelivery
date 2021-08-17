package com.track.cylinderdelivery.utils;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.R;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SignatureActivity extends AppCompatActivity {

    private SignaturePad mSilkySignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    Context context;
    String baseUrl="http://test.hdvivah.in/api/MobSalesOrder/UploadSalesImage";
    String SONumber="SO0001";
    ImageView imageView2;
    private static final int MY_SOCKET_TIMEOUT_MS = 100000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature);
        context=this;
        mClearButton = findViewById(R.id.clear_button);
        mSaveButton = findViewById(R.id.save_button);
        imageView2=findViewById(R.id.imageView2);

        mSilkySignaturePad = findViewById(R.id.signature_pad);

        mSilkySignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
                Toast.makeText(SignatureActivity.this, "OnStartSigning", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap signatureBitmap = mSilkySignaturePad.getSignatureBitmap();
              //  Log.d("bitmap==>",savedInstanceState+"");
                imageView2.setImageBitmap(signatureBitmap);
                if(signatureBitmap!=null) {
                    uploadimage(signatureBitmap);
                }
                /* String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/req_images");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = SONumber+"-" + n + ".jpg";
                File file = new File(myDir, fname);
                Log.i("TAG", "" + file);
                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    signatureBitmap.compress(Bitmap.CompressFormat.JPEG, 0, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (file != null) {
                   // imageUpload(file.getPath());
                } else {
                    Toast.makeText(getApplicationContext(), "Image not selected!", Toast.LENGTH_LONG).show();
                }*/
            }
        });
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSilkySignaturePad.clear();
            }
        });
    }

    private void uploadimage(Bitmap signatureBitmap) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, baseUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("response==>",response+"");
                            /*JSONObject jsonObject  = new JSONObject(response);
                            String Response = jsonObject.getString("response");
                            Toast.makeText(SignatureActivity.this, Response,Toast.LENGTH_LONG).show();*/
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        } )
        {
            @Override
            public String getBodyContentType() {
                return "multipart/form-data";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                final JSONObject jsonBody=new JSONObject();
                try {
                    jsonBody.put("files",imageToString(signatureBitmap));
                    Log.d("bitmap==>",imageToString(signatureBitmap)+"");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonBody.toString().getBytes();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("content-type","multipart/form-data");
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
    private byte[] imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream =  new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgbytes = byteArrayOutputStream.toByteArray();
       // return Base64.encodeToString(imgbytes, Base64.DEFAULT);
        return imgbytes;
    }
}