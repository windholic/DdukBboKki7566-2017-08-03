package com.kim0420.ddukbbokki7566;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;

public class CommentActivity extends AppCompatActivity {

    RecyclerView rv;
    EditText et;

    CommentAdapter adapter;

    ArrayList<Comment> list = new ArrayList<>();


    String title;
    String pnum;

    String writeboardUrl = "http://windholic7566.dothome.co.kr/map/saveBoard.php";
    String loadboardUrl = "http://windholic7566.dothome.co.kr/map/loadBoard.php";

    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        title = getIntent().getStringExtra("title");
        pnum = "";

        rv = (RecyclerView)findViewById(R.id.rv_comment);
        et = (EditText)findViewById(R.id.et_comment);

        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        adapter = new CommentAdapter(this,list);
        rv.setAdapter(adapter);


        loadboard();



    }

    public void clickcomment(View v){

        new Thread(){
            @Override
            public void run() {
                String text = et.getText().toString();
                calendar = Calendar.getInstance();
                String date = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+(calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)+"");

                try {
                    text = URLEncoder.encode(text,"utf-8");
                    title = URLEncoder.encode(title,"utf-8");
                    date = URLEncoder.encode(date,"utf-8");
                    pnum = URLEncoder.encode(pnum,"utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                try {
                    URL url= new URL(writeboardUrl);
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);

                    String data = "Pnum="+pnum+"&Title="+title+"&Text="+text+"&Date="+date;
                    OutputStream os = conn.getOutputStream();
                    os.write(data.getBytes());
                    os.flush();
                    os.close();

                    InputStream is = conn.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    final StringBuffer buffer = new StringBuffer();
                    String line = br.readLine();

                    while(line!=null){
                        buffer.append(line);
                        line= br.readLine();
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommentActivity.this,buffer.toString(),Toast.LENGTH_SHORT).show();

                            et.setText("");
                        }
                    });

                    loadboard();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();
    }



    public void loadboard(){


        new Thread(){
            @Override
            public void run() {

                try {
                    URL url = new URL(loadboardUrl);

                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);




                    InputStream is = conn.getInputStream();
                    InputStreamReader isr= new InputStreamReader(is);
                    BufferedReader br = new BufferedReader(isr);

                    title = URLEncoder.encode(title,"utf-8");



//                    String selecttitle = "Title="+title;
//                    OutputStream os = conn.getOutputStream();
//                    os.write(selecttitle.getBytes());
//                    os.flush();
//                    os.close();

                    StringBuffer buffer=new StringBuffer();
                    String line=br.readLine();

                    while (line!=null){
                        buffer.append(line);
                        line = br.readLine();
                    }
                    String str = buffer.toString();

                    String[] rows = str.split(";");

                    list.clear();

                    for(String row : rows){

                        String[] datas = row.split("&");

                        if(datas.length<4){
                            continue;
                        }

                        String loadedpnum = datas[0];
                        String loadedtitle = datas[1];
                        String loadedtext = datas[2];
                        String loadeddate = datas[3];

                        if(CommentActivity.this.title.equals(loadedtitle)) list.add(new Comment(loadedpnum,loadedtitle,loadedtext,loadeddate));


                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(CommentActivity.this,list.size()+"",Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                            if(list.size()>=1) {
                                rv.scrollToPosition(list.size() - 1);
                            }
                        }
                    });




                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }.start();



    }


}
