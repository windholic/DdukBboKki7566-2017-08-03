package com.kim0420.ddukbbokki7566;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FavoritActivity extends AppCompatActivity {

    RecyclerView rv;
    TextView tv;
    ArrayList<Item> Favitems;
    FavAdapter adapter;
    LocationManager lm;
    LocationListener mLocationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorit);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();
                float accuracy = location.getAccuracy();
                String provider = location.getProvider();

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

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
        lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,mLocationListener);


        Favitems = loadfavo();

        rv = (RecyclerView)findViewById(R.id.rv_fav);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        tv = (TextView)findViewById(R.id.tv_fav);

        if((Favitems==null)||Favitems.size()==0){
            tv.setVisibility(View.VISIBLE);
        }else {
            rv.setVisibility(View.VISIBLE);
            adapter = new FavAdapter(Favitems, this,lm);
            rv.setAdapter(adapter);
        }


    }

    public ArrayList<Item> loadfavo(){
        try {
            FileInputStream fis = openFileInput("datafavo.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<Item> savedata = (ArrayList<Item>) ois.readObject();
            ois.close();
            return savedata;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void savefavo(ArrayList<Item> savedata){

        try {
            FileOutputStream fos = openFileOutput("datafavo.txt",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(savedata);
            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed() {

        for(int i = Favitems.size()-1;i>=0;i--){
            if(Favitems.get(i).willdelete){
                Favitems.remove(i);
            }

        }

        savefavo(Favitems);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        for(int i = Favitems.size()-1;i>=0;i--){
        if(Favitems.get(i).willdelete){
            Favitems.remove(i);
        }
    }
        savefavo(Favitems);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        for(int i = Favitems.size()-1;i>=0;i--){
            if(Favitems.get(i).willdelete){
                Favitems.remove(i);
            }
        }
        savefavo(Favitems);
        super.onPause();
    }
}
