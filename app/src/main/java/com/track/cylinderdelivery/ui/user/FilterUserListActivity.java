package com.track.cylinderdelivery.ui.user;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.track.cylinderdelivery.R;

import org.angmarch.views.NiceSpinner;
import org.angmarch.views.OnSpinnerItemSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class FilterUserListActivity extends AppCompatActivity {

    Context context;
    RelativeLayout rvBlank;
    private ArrayList<HashMap<String,String>> userTypeList;
    private ArrayList<HashMap<String,String>> companyList;
    RadioGroup userRadioGroup,companyRadioGroup;
    Button btnCancel,btnApply;
    private SharedPreferences spUserFilter;
    private SharedPreferences spCompanyFilter;

    NiceSpinner spinnerCompany;
    NiceSpinner spinnerUser;

    List<String> companyDataSet,userDataSet;

    int indexCompanySelected=1,indexUserSelected=1;
    private int CompanyPosition;
    private int userPosition;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_user_list);
        context=this;
        userTypeList= (ArrayList<HashMap<String,String>>) getIntent().getSerializableExtra("userTypeList");
        companyList= (ArrayList<HashMap<String,String>>) getIntent().getSerializableExtra("companyList");
        rvBlank=(RelativeLayout)findViewById(R.id.rvBlank);
        userRadioGroup=findViewById(R.id.userRadioGroup);
        companyRadioGroup=findViewById(R.id.companyRadioGroup);
        btnCancel=findViewById(R.id.btnCancel);
        btnApply=findViewById(R.id.btnApply);
        spUserFilter=context.getSharedPreferences("userFilter",MODE_PRIVATE);
        spCompanyFilter=context.getSharedPreferences("companyFilter",MODE_PRIVATE);

        spinnerCompany = (NiceSpinner) findViewById(R.id.spinnerCompany);
        companyDataSet = new LinkedList<>();

        spinnerUser = (NiceSpinner) findViewById(R.id.spinnerUser);
        userDataSet = new LinkedList<>();

        for(int i=0;i<companyList.size();i++){
            //RadioButton radioButton=new RadioButton(context);
            //radioButton.setId(1+i);
            //RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            //radioButton.setText(companyList.get(i).get("text"));
            companyDataSet.add(companyList.get(i).get("text"));
            //radioButton.setTextColor(getResources().getColor(R.color.black));
            //radioButton.setTextSize(getResources().getDimension(R.dimen.radiolbl));
            //radioButton.setButtonTintList(getRadioButtonColors());
            //companyRadioGroup.addView(radioButton,i,rprms);
            if(companyList.get(i).get("text").trim().equals(spCompanyFilter.getString("text","").trim())){
            //    radioButton.setChecked(true);
                indexCompanySelected=i;
            }
        }
       // companyDataSet.add("data");
        spinnerCompany.attachDataSource(companyDataSet);
        spinnerCompany.setSelectedIndex(indexCompanySelected);

        for(int i=0;i<userTypeList.size();i++){
            //RadioButton radioButton=new RadioButton(context);
            //radioButton.setId(1+i);
            //RadioGroup.LayoutParams rprms = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
            //radioButton.setText(userTypeList.get(i).get("text"));
            userDataSet.add(userTypeList.get(i).get("text"));
            //radioButton.setTextColor(getResources().getColor(R.color.black));
            //radioButton.setTextSize(getResources().getDimension(R.dimen.radiolbl));
            //radioButton.setButtonTintList(getRadioButtonColors());
            //userRadioGroup.addView(radioButton,i,rprms);
            if(userTypeList.get(i).get("text").toString().trim().equals(spUserFilter.getString("text","").trim())){
              //  radioButton.setChecked(true);
                indexUserSelected=i;
            }
        }
        spinnerUser.attachDataSource(userDataSet);
        spinnerUser.setSelectedIndex(indexUserSelected);

        spinnerCompany.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                Log.d("checkedId==>",position+"");
                CompanyPosition=position;

            }
        });

        spinnerUser.setOnSpinnerItemSelectedListener(new OnSpinnerItemSelectedListener() {
            @Override
            public void onItemSelected(NiceSpinner parent, View view, int position, long id) {
                Log.d("checkedId==>",position+"");
                userPosition=position;

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor userFilterEditor = spUserFilter.edit();
                userFilterEditor.putBoolean("dofilter",true);
                userFilterEditor.putInt("index",userPosition);
                userFilterEditor.putString("text",userTypeList.get(userPosition).get("text"));
                userFilterEditor.commit();

                SharedPreferences.Editor companyFilterEditor = spCompanyFilter.edit();
                companyFilterEditor.putBoolean("dofilter",true);
                companyFilterEditor.putInt("index",CompanyPosition);
                companyFilterEditor.putString("text",companyList.get(CompanyPosition).get("text"));
                companyFilterEditor.commit();

                finish();
            }
        });
        rvBlank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor userFilterEditor = spUserFilter.edit();
                userFilterEditor.putBoolean("dofilter",false);
                userFilterEditor.commit();
                SharedPreferences.Editor companyFilterEditor = spCompanyFilter.edit();
                companyFilterEditor.putBoolean("dofilter",false);
                companyFilterEditor.commit();
                finish();
            }
        });
    }
/*    private ColorStateList getRadioButtonColors() {
        return new ColorStateList (
                new int[][] {
                        new int[] {android.R.attr.state_checked}, // checked
                        new int[] {android.R.attr.state_enabled} // unchecked
                },
                new int[] {
                        Color.GREEN, // checked
                        Color.BLUE   // unchecked
                }
        );
    }*/
}