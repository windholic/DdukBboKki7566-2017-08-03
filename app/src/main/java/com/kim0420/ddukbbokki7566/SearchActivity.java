package com.kim0420.ddukbbokki7566;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

public class SearchActivity extends AppCompatActivity {


    String SearchID = "oAn9W5C8wD4ldqnbpjdq";
    String SearchPW = "TnBUYZGxl8";

    String MapID = "U_s_ztBySozXRIBNFZmB";
    String MapPW = "4VE3J1KYOY";

    EditText et;
    TextView tv;

    ArrayList<SearchData> searchDatas;
    ArrayList<Item> items;

    RecyclerView rv;


    Calendar cal;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        et = (EditText)findViewById(R.id.et);
        tv = (TextView)findViewById(R.id.tv_search);
        rv = (RecyclerView)findViewById(R.id.recy_search);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));


        loaddata();

        if(searchDatas==null||searchDatas.size()==0){
            tv.setVisibility(View.VISIBLE);
        }else{
            rv.setVisibility(View.VISIBLE);
        }


    }


    public void clickbtn(View v){

        new Thread(){
            @Override
            public void run() {



                try {

                    String text_search = et.getText().toString();

                    text_search = URLEncoder.encode(text_search,"utf-8");

                    String queryUrl = "https://openapi.naver.com/v1/search/local.json"+"?query="+text_search;

                    URL url = new URL(queryUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setRequestProperty("X-Naver-Client-Id",SearchID);
                    conn.setRequestProperty("X-Naver-Client-Secret",SearchPW);

                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    final StringBuffer buffer = new StringBuffer();
                    String line = br.readLine();

                    while(line != null){
                        buffer.append(line);
                        line=br.readLine();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            tv.setText(buffer.toString());
                            if(searchDatas==null||searchDatas.size()==0){
                                tv.setVisibility(View.VISIBLE);
                            }else{
                                rv.setVisibility(View.VISIBLE);
                            }
                        }
                    });









                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
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
}
