package com.kim0420.ddukbbokki7566;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ImageView Search,AroundSearch,Bookmark,Diary;

    ArrayList<FoodMenu> menus;

    RelativeLayout cv1,cv2,cv3,cv4;
    ImageView iv1,iv2,iv3,iv4;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cv1 = (RelativeLayout) findViewById(R.id.cv1);
        cv2 = (RelativeLayout)findViewById(R.id.cv2);
        cv3 = (RelativeLayout)findViewById(R.id.cv3);
        cv4 = (RelativeLayout)findViewById(R.id.cv4);

        iv1 = (ImageView)findViewById(R.id.iv1);
        iv2 = (ImageView)findViewById(R.id.iv2);
        iv3 = (ImageView)findViewById(R.id.iv3);
        iv4 = (ImageView)findViewById(R.id.iv4);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if((checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED)||
                    (checkSelfPermission(Manifest.permission.CALL_PHONE)!=PackageManager.PERMISSION_GRANTED)||
                    (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)||
                    (checkSelfPermission(Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED)||
                    (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)||
                    (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CALL_PHONE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_FINE_LOCATION
                ,Manifest.permission.READ_EXTERNAL_STORAGE},100);

            }

        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case 100:
                if (grantResults ==new int[]{PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED,
                        PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED,PackageManager.PERMISSION_GRANTED}) {
                    return;
                }
                else{
                    Toast.makeText(this, "허가되지 않은 기능은 동작하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void clickbtn0(View v){
        Intent  intent = new Intent(this,MenuActivity.class);
        startActivity(intent);



    }

    public void clickbtn1(View v){

        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);




    }

    public void clickbtn2(View v){

        LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


        if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("위치 정보 확인 옵션이 설정되어 있지 않습니다." +
                    "앱의 사용을 위해서는 '위치 및 보안 설정'의 무선 네트워크 및 GPS 위성 옵션에 대해 사용으로 설정하십시오.")
            .setCancelable(false).setPositiveButton("설정", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else {

            Intent intent = new Intent(this, MyLocationActivity.class);
            startActivity(intent);
        }
    }

    public void clickbtn3(View v){

        Intent intent = new Intent(this,FavoritActivity.class);
        startActivity(intent);


    }

    public void clickbtn4(View v){


        Intent intent = new Intent(this,DiaryActivity.class);
        startActivity(intent);




    }

    public void loaddata(){
        try {
            File file = new File("datamenu.txt");
            if(!file.exists()){
                return;
            }

            FileInputStream fis = openFileInput("datamenu.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            menus = (ArrayList<FoodMenu>) ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){


            float y1 = cv1.getY();
            float y2 = cv2.getY();
            float y3 = cv3.getY();
            float y4 = cv4.getY();

            float h = y2 - y1;

            float x1 = cv1.getX();

            cv1.setRotation( -(float)Math.toDegrees(Math.atan2(cv1.getHeight(),cv1.getWidth())));
            cv2.setRotation( -(float)Math.toDegrees(Math.atan2(cv2.getHeight(),cv2.getWidth())));
            cv3.setRotation( -(float)Math.toDegrees(Math.atan2(cv3.getHeight(),cv3.getWidth())));
            cv4.setRotation( -(float)Math.toDegrees(Math.atan2(cv4.getHeight(),cv4.getWidth())));





            if(!(cv1.getY()<0)) {
                cv1.setY(y1 - (float) (h * Math.cos(Math.atan2(cv1.getWidth(), cv1.getHeight()))) - 30);
                cv2.setY(y2 - (float) (h * Math.cos(Math.atan2(cv1.getWidth(), cv1.getHeight()))) - 30);
                cv3.setY(y3 - (float) (h * Math.cos(Math.atan2(cv1.getWidth(), cv1.getHeight()))) - 30);
                cv4.setY(y4 - (float) (h * Math.cos(Math.atan2(cv1.getWidth(), cv1.getHeight()))) - 30);
            }

            cv1.setScaleX(1.5f);
            cv2.setScaleX(1.5f);
            cv3.setScaleX(1.5f);
            cv4.setScaleX(1.5f);



        }

    }
}
