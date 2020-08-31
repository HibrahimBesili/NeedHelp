package com.example.needhelp;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    static SQLiteDatabase database;
    SessionManager sessionManager;
    HashMap<String,String> user=sessionManager.getUserDetail();
    String user_name=user.get(sessionManager.USERNAME);
    String phone_number=user.get(sessionManager.Phone_Number);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);
        locationManager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                SharedPreferences sharedPreferences=MapsActivity.this.getSharedPreferences("com.example.needhelp",MODE_PRIVATE);
                boolean firstimecheck=sharedPreferences.getBoolean("notfirstime",false);

                if(!firstimecheck){
                    LatLng userlocation=new LatLng(location.getLatitude(),location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userlocation,15));
                    sharedPreferences.edit().putBoolean("notfirsttime",true).apply();
                }



            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (Build.VERSION.SDK_INT >=23){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }else{
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,50,locationListener);
                mMap.clear();
                Location lastlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if(lastlocation!=null){
                    LatLng lastuserlocation=new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15));
                }



            }
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,50,locationListener);
            Location lastlocation=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(lastlocation!=null){
                LatLng lastuserlocation=new LatLng(lastlocation.getLatitude(),lastlocation.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastuserlocation,15));
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length>0){
            if (requestCode==1){
                if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,50,locationListener);
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Geocoder geocoder=new Geocoder(getApplicationContext(), Locale.getDefault());
        String address="";
        try {
            List<Address> addressList=geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
            if (addressList!=null && addressList.size()>0){
                if (addressList.get(0).getSubThoroughfare()!=null){
                    address+=addressList.get(0).getSubThoroughfare();
                    if (addressList.get(1).getSubThoroughfare() !=null){
                        address+=addressList.get(1).getSubThoroughfare();
                        if (addressList.get(2).getSubThoroughfare()!=null){
                            address+=addressList.get(2).getSubThoroughfare();
                        }

                    }
                }
            }
            else {
                address="New Place";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        mMap.addMarker(new MarkerOptions().title(address).position(latLng));
        Toast.makeText(getApplicationContext(),"New Place Ok!",Toast.LENGTH_SHORT).show();

        try {
            Double l1=latLng.latitude;
            Double l2=latLng.longitude;

            String coord1=l1.toString();
            String coord2=l2.toString();
            //String coord3=complaint.editText.getText().toString();
            database=this.openOrCreateDatabase("help.db",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS places(username VARCHAR,latitude VARCHAR,longtitude VARCHAR,address VARCHAR,phonenumber VARCHAR(15))");

            String toCompile="INSERT INTO places (username,latitude,longitude,address,phonenumber) VALUES(?,?,?,?,?)";
            SQLiteStatement sqLiteStatement=database.compileStatement(toCompile);

            sqLiteStatement.bindString(1,user_name);
            sqLiteStatement.bindString(2,coord1);
            sqLiteStatement.bindString(3,coord2);
            sqLiteStatement.bindString(4,address);
            sqLiteStatement.bindString(5,phone_number);
            //sqLiteStatement.bindString(6,coord3);

            sqLiteStatement.execute();
            //Intent intent1=new Intent(MapsActivity.this,HomeActivity.class);
            //startActivity(intent1);



        }catch (Exception e){

        }

    }
}
