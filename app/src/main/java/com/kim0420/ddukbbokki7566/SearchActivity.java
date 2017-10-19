package com.kim0420.ddukbbokki7566;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;

public class SearchActivity extends AppCompatActivity {


    String SearchID = "oAn9W5C8wD4ldqnbpjdq";
    String SearchPW = "TnBUYZGxl8";

    String MapID = "U_s_ztBySozXRIBNFZmB";
    String MapPW = "4VE3J1KYOY";

    EditText et;
    TextView tv;

    ArrayList<SearchData> searchDatas;
    ArrayList<Item> items=new ArrayList<>();
    ArrayList<FoodMenu> menus;

    SearchAdapter searchAdapter;
    SearchdataAdapter searchdataAdapter;


    RecyclerView rv;
    RecyclerView nrv;


    Calendar cal;

    GestureDetector gestureDetector;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        et = (EditText)findViewById(R.id.et);
        tv = (TextView)findViewById(R.id.tv_search);
        rv = (RecyclerView)findViewById(R.id.recy_search);
        nrv = (RecyclerView)findViewById(R.id.recy_nsearch);

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        nrv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

        loaddata();
        loadmenu();
        if(searchDatas==null){
            searchDatas = new ArrayList<>();

        }

        if(searchDatas==null||searchDatas.size()==0){
            tv.setVisibility(View.VISIBLE);
        }else{
            rv.setVisibility(View.VISIBLE);
        }

        searchAdapter = new SearchAdapter(searchDatas,this);
        rv.setAdapter(searchAdapter);

        searchdataAdapter = new SearchdataAdapter(this,items);
        nrv.setAdapter(searchdataAdapter);


        nrv.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                LocationManager manager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);


                if(!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchActivity.this);
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
                    Intent intent = new Intent(SearchActivity.this, MapActivity.class);
                    intent.putExtra("item", items);
                    intent.putExtra("position", position);

                    startActivity(intent);
                }
            }
        }));




    }


    public void clickbtn(View v){

        new Thread(){
            @Override
            public void run() {



                try {

                    if(!(items.isEmpty())){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                items.clear();
                            }
                        });

                    }


                    cal = Calendar.getInstance();





                    String text_search = et.getText().toString();
                    String dt = cal.get(Calendar.YEAR)+"-"+(cal.get(Calendar.MONTH)+1)+"-"+cal.get(Calendar.DAY_OF_MONTH);

                    searchDatas.add(new SearchData(text_search,dt));

                    text_search = URLEncoder.encode(text_search,"utf-8");

                    String queryUrl = "";

                    for(int j = 0; j<menus.size();j++) {
                        queryUrl = "https://openapi.naver.com/v1/search/local?query=" + text_search + " " + menus.get(j).menu + "&display=30";


                        URL url = new URL(queryUrl);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        conn.setRequestProperty("X-Naver-Client-Id", SearchID);
                        conn.setRequestProperty("X-Naver-Client-Secret", SearchPW);

                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                        final StringBuffer buffer = new StringBuffer();
                        String line = br.readLine();

                        while (line != null) {
                            buffer.append(line);
                            line = br.readLine();
                        }


                        JSONObject jsonObject = new JSONObject(buffer.toString());
                        int total = jsonObject.getInt("total");
                        if (total != 0) {
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Item item = new Item();
                                JSONObject jsonitem = jsonArray.getJSONObject(i);
                                item.title = jsonitem.getString("title");
                                item.title = item.title.replace("<b>", "");
                                item.title = item.title.replace("</b>", "");
                                item.telephone = jsonitem.getString("telephone");
                                item.address = jsonitem.getString("address");
                                item.roadAddress = jsonitem.getString("roadAddress");
                                item.mapx = jsonitem.getInt("mapx");
                                item.mapy = jsonitem.getInt("mapy");
                                items.add(item);
                            }
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    nrv.setVisibility(View.VISIBLE);
                                    searchdataAdapter.notifyDataSetChanged();
                                    rv.setVisibility(View.GONE);

                                }
                            });

                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv.setText("검색 결과가 없습니다.");
                                }
                            });
                        }
                    }

                    savedata();



                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }


    public void loaddata(){
        try {

            FileInputStream fis = openFileInput("datasearch.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            searchDatas = (ArrayList<SearchData>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void loadmenu(){
        try {

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

    public void savedata(){

        try {
            FileOutputStream fos = openFileOutput("datasearch.txt",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(searchDatas);
            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPause() {
        savedata();
        super.onPause();
    }



}
