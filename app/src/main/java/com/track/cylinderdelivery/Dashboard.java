package com.track.cylinderdelivery;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.track.cylinderdelivery.ui.BaseActivity;
import com.track.cylinderdelivery.ui.dashboard.DashboardFragment;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.json.JSONObject;

public class Dashboard extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private Context context;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private TextView txtUserName,txtEmail,txtLogout;
    private SharedPreferences settings;
    public static final int MY_PERMISSIONS_REQUEST_CAMERA = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        context=this;
        settings=getSharedPreferences("setting",MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Snackbar.make(view, "We will put universal QR scan", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                if (ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Dashboard.this,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_CAMERA);
                }else {
                    openQrScan();
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        View hView =  navigationView.getHeaderView(0);
        txtUserName=(TextView)hView.findViewById(R.id.txtUserName);
        txtEmail=(TextView)hView.findViewById(R.id.txtEmail);
        txtLogout=(TextView)hView.findViewById(R.id.txtLogout);
        txtUserName.setText(settings.getString("fullName",""));
        txtEmail.setText(settings.getString("email",""));

        txtLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = settings.edit();
                //editor.putBoolean("loggedIN",false);
                editor.clear();
                editor.apply();
                editor.commit();
                finish();
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        /*mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard)
                .setDrawerLayout(drawer)
                .build();*/
         mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_company, R.id.nav_user,R.id.nav_cylinder,
                R.id.nav_product,R.id.nav_warehouse,R.id.nav_acknowledge,R.id.nav_purchaseorder,
                 R.id.nav_cylinderproductmapping,R.id.nav_report,R.id.nav_cylinderWarehouseMapping,
                 R.id.nav_permssion,R.id.nav_delivery_note,R.id.nav_sales_order,R.id.nav_return_order)
                .setDrawerLayout(drawer)
                .build();
/*        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_dashboard, R.id.nav_company, R.id.nav_user,R.id.nav_acknowledge,
                R.id.nav_cylinderproductmapping,R.id.nav_cylinderWarehouseMapping,
                R.id.nav_purchaseorder,R.id.nav_delivery_note,R.id.nav_sales_order,
                R.id.nav_return_order,R.id.nav_cylinder,
                R.id.nav_product,R.id.nav_warehouse,R.id.nav_report,R.id.nav_permssion)
                .setDrawerLayout(drawer)
                .build();*/

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



       JSONObject menuJsonobj=new JSONObject();
       Menu menu = navigationView.getMenu();

/*        SubMenu subMenu = menu.addSubMenu(getString(R.string.company));
        subMenu.add(0, Menu.FIRST, Menu.FIRST, getString(R.string.company_list))
                .setIcon(R.drawable.ic_menu_gallery);
        subMenu.add(1, Menu.FIRST + 1, Menu.FIRST, getString(R.string.company_list))
                .setIcon(R.drawable.ic_menu_camera);*/

       addMenuDynamic(menu,menuJsonobj);


        /*navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==1){
                    Toast.makeText(context,getText(R.string.dashboard),Toast.LENGTH_LONG).show();
                }else if(item.getItemId()==2){
                    Toast.makeText(context,getText(R.string.company),Toast.LENGTH_LONG).show();
                }
                drawer.closeDrawers();
                return false;
            }
        });*/
    }

    private void openQrScan() {
        Intent intent = new Intent(context, ContinuousCaptureActivity.class);
        startActivity(intent);
    }

    private void addMenuDynamic(Menu menu, JSONObject menuJsonobj) {
        //menu.findItem(R.id.nav_company).getSubMenu().addSubMenu(getResources().getString(R.string.company));
        menu.findItem(R.id.nav_product).setVisible(false);
        menu.findItem(R.id.nav_warehouse).setVisible(false);
        menu.findItem(R.id.nav_permssion).setVisible(false);
        menu.findItem(R.id.nav_report).setVisible(false);
       // menu.findItem(R.id.nav_return_order).setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

     //   getMenuInflater().inflate(R.menu.dashboard, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
}