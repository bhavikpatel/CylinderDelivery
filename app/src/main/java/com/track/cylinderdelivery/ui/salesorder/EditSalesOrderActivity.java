package com.track.cylinderdelivery.ui.salesorder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;

import com.track.cylinderdelivery.R;

import java.util.HashMap;

public class EditSalesOrderActivity extends AppCompatActivity {

    private HashMap<String, String> mapdata;
    EditSalesOrderActivity context;
    EditText edtSoNumber,edtSoDate;
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
        edtSoNumber=findViewById(R.id.edtSoNumber);
        edtSoNumber.setText(mapdata.get("soNumber"));
        edtSoDate=findViewById(R.id.edtSoDate);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}