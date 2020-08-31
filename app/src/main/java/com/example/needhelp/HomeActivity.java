package com.example.needhelp;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {
    static ArrayList<String> usernames=new ArrayList<String>();
    static ArrayList<String> addresses=new ArrayList<String>();
    static ArrayList<String> phone_numbers=new ArrayList<String>();
    static ArrayList<LatLng> locations=new ArrayList<LatLng>();
    static ArrayAdapter arrayAdapter;

    SessionManager sessionManager;
    private TextView username,phone_number;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.add_help,menu);


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.add_help){
            Intent intentt = new Intent(HomeActivity.this,MapsActivity.class);
            startActivity(intentt);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        sessionManager=new SessionManager(this);
        //sessionManager.checkLogin();
        username=findViewById(R.id.edittext_username);
        phone_number=findViewById(R.id.edittext_phone_number);


        HashMap<String,String> user=sessionManager.getUserDetail();
        String user_name=user.get(sessionManager.USERNAME);
        String phone_number=user.get(sessionManager.Phone_Number);


        System.out.println(user_name);


        ListView listView=(ListView)findViewById(R.id.listview);

        try {
            MapsActivity.database=this.openOrCreateDatabase("help.db",MODE_PRIVATE,null);
            Cursor cursor=MapsActivity.database.rawQuery("SELECT * FROM places",null);
            int latitudeInd=cursor.getColumnIndex("latitude");
            int longitudeInd=cursor.getColumnIndex("longitude");
            int adressInd=cursor.getColumnIndex("address");
            int usernameInd=cursor.getColumnIndex("username");
            int phonenumberInd=cursor.getColumnIndex("phonenumber");
            int complaintsInd=cursor.getColumnIndex("complaints");



            while (cursor.moveToNext()){
                String addressfromdb=cursor.getString(adressInd);
                String usernamefromdb=cursor.getString(usernameInd);
                String phonenumberfromdb=cursor.getString(phonenumberInd);
                String complaintsfromdb=cursor.getString(complaintsInd);
                String latitudefromdb=cursor.getString(latitudeInd);
                String longitudefromdb=cursor.getString(longitudeInd);
                System.out.println(phonenumberfromdb);

                addresses.add(addressfromdb);
                phone_numbers.add(phonenumberfromdb);
                usernames.add(usernamefromdb);

                double l1=Double.parseDouble(latitudefromdb);
                double l2=Double.parseDouble(longitudefromdb);

                LatLng locationfromdb=new LatLng(l1,l2);

                locations.add(locationfromdb);


            }
            cursor.close();





        }catch (Exception e){
            System.out.println("ERROR");
        }

        arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1,phone_numbers);
        listView.setAdapter(arrayAdapter);
    }}