package com.example.subham.helpnotifier;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity
{
    Button btnManage,btnSMS;
    SQLiteDatabase db;
    double Latitude;
    double Longitude;
    String LocationInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS  ) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS},1);
        }

        db = openOrCreateDatabase("Test",MODE_PRIVATE,null);
        db.execSQL("create table if not exists Contacts(Name varchar(20), Number varchar(20))");

         btnManage = (Button) findViewById(R.id.btnManage);
         btnSMS = (Button) findViewById(R.id.btnSMS);

        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);

        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,5000,5,new MyLocation());

         btnManage.setOnClickListener(new MyClick());
         btnSMS.setOnClickListener(new MyClick());
    }

    class MyClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            if(v == btnManage)
            {
                Intent I = new Intent(MainActivity.this,Main2Activity.class);
                startActivity(I);
            }
            else if(v == btnSMS)
            {
                Cursor cur = db.rawQuery("select * from Contacts",null);
                if(cur.getCount() > 0)
                {
                    String Message = "Help Me! My Current Location : '" + LocationInfo + "'";

                    while (cur.moveToNext())
                    {
                        String Number = cur.getString(1);

                        SmsManager sms = SmsManager.getDefault();
                        sms.sendTextMessage(Number, null, Message, null, null);

                        Toast.makeText(MainActivity.this, "Sent", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Empty Contact List", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    class MyLocation implements LocationListener
    {
        @Override
        public void onLocationChanged(Location location)
        {
            Latitude = location.getLatitude();
            Longitude = location.getLongitude();

            try
            {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(Latitude,Longitude,1);
                Address a = addresses.get(0);

                LocationInfo = a.getAddressLine(0)+"\n"+a.getSubLocality()+"\n"+a.getLocality()+"\n"+a.getSubAdminArea()+"\n"+a.getAdminArea();

            }
            catch(Exception E)
            {
                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onProviderEnabled(String provider)
        {

        }

        @Override
        public void onProviderDisabled(String provider)
        {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {

        }
    }
}
