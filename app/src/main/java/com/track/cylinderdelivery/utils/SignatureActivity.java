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
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.track.cylinderdelivery.R;
import com.williamww.silkysignature.views.SignaturePad;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                imageView2.setImageBitmap(signatureBitmap);
                if(signatureBitmap!=null) {
                    uploadimage(signatureBitmap);
                }
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
        MarketPlaceApiInterface apiService = Apiclient.getClient().create(MarketPlaceApiInterface.class);
        File file = new File(getCacheDir().getPath() + "SignImage.jpg");
        try {
            OutputStream fOut = new FileOutputStream(file);
            signatureBitmap.compress(Bitmap.CompressFormat.JPEG,85,fOut);
            fOut.flush();
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse("text/plain"), file);
        MultipartBody.Part BusinessImage = MultipartBody.Part.
                createFormData("files", file.getName(), requestFile);
        apiService.UpdateMarketPlaceProducts(Collections.singletonList(BusinessImage))
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            Log.d("onResponse", "onResponse: " + response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("onFailure", "onResponse: " + t.getMessage());

                    }
                });
    }

}