package com.kim0420.ddukbbokki7566;

import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

public class WriteActivity extends AppCompatActivity {

    Calendar calendar;

    EditText title,content;
    TextView addr,date,ttitle;

    String saddr,stitle,sdate;
    Button btn;

    ArrayList<String> filelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        filelist = loadfilelist();


        setContentView(R.layout.activity_write);
        calendar = Calendar.getInstance();
        Intent mIntent = getIntent();
        stitle = mIntent.getStringExtra("title");
        saddr= mIntent.getStringExtra("addr");
        sdate = calendar.get(Calendar.YEAR)+"-"+(calendar.get(Calendar.MONTH)+1)+"-"+calendar.get(Calendar.DAY_OF_MONTH)+" "+calendar.get(Calendar.HOUR_OF_DAY)+":"+(calendar.get(Calendar.MINUTE)<10?"0"+calendar.get(Calendar.MINUTE):calendar.get(Calendar.MINUTE)+"");


        title = (EditText) findViewById(R.id.et_title_write);
        content = (EditText) findViewById(R.id.et_content_write);

        ttitle = (TextView) findViewById(R.id.tv_title_write);
        addr = (TextView) findViewById(R.id.tv_addr_write);
        date = (TextView) findViewById(R.id.tv_date_write);
        btn = (Button) findViewById(R.id.btn_write);

        addr.setText(saddr);
        date.setText(sdate);
        ttitle.setText(stitle);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {


                    String filename = stitle+"-"+sdate+".txt";
//                    String filePath = getApplicationContext().getFilesDir().getAbsolutePath().toString() + "/diary/"+filename;
                    String filepath = Environment.getExternalStorageDirectory()+"/diary/";
                    File mfolder = new File(filepath);
                    if(!mfolder.exists()){
                        mfolder.mkdirs();
                    }

                    File file = new File(mfolder.getAbsolutePath()+"/"+filename);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    filelist.add(file.getName());
                    savefilelist();
                    diarydata savedata = new diarydata(ttitle.getText().toString(),addr.getText().toString(),date.getText().toString());
                    savedata.content= content.getText().toString();
                    savedata.ctitle= title.getText().toString();
                    FileOutputStream fos = new FileOutputStream(file,false);
                    ObjectOutputStream oos = new ObjectOutputStream(fos);
                    oos.writeObject(savedata);
                    oos.flush();
                    oos.close();

                    Toast.makeText(WriteActivity.this,filename.toString(),Toast.LENGTH_SHORT).show();




                    finish();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });



    }

//    public void btnsave(){
//        try {
//            String file = saddr+"-"+sdate+".txt";
//            filelist.add(file);
//            savefilelist();
//            diarydata savedata = new diarydata(ttitle.getText().toString(),addr.getText().toString(),date.getText().toString());
//            savedata.content= content.getText().toString();
//            FileOutputStream fos = openFileOutput(file,MODE_PRIVATE);
//            ObjectOutputStream oos = new ObjectOutputStream(fos);
//            oos.writeObject(savedata);
//            oos.flush();
//            oos.close();
//            finish();
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//

//
//    }

    public void savefilelist(){
        try {
//            String filename = "filelist.txt";
//            String filePath = getApplicationContext().getFilesDir().getAbsolutePath().toString() + "/diary/";
//            File file = new File(filePath+filename);
            String filepath = Environment.getExternalStorageDirectory()+"/diary/";
            String filename = "filelist.txt";

            File mfolder = new File(filepath);
            if(!mfolder.exists()){
                mfolder.mkdirs();
            }

            File file = new File(mfolder.getAbsolutePath()+filename);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file,false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(filelist);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    public ArrayList<String> loadfilelist(){
        try {

//            String filePath = getApplicationContext().getFilesDir().getAbsolutePath().toString() + "/diary/";
//            File file = new File(filePath+"filelist.txt");

            String filepath = Environment.getExternalStorageDirectory()+"/diary/";
            File mfolder = new File(filepath);
            if(!mfolder.exists()){
                mfolder.mkdir();
            }

            File file = new File(mfolder.getAbsolutePath()+"/diary/filelist.txt");
            if(!file.exists()||!file.canRead()){
                return new ArrayList<>();
            }
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            ArrayList<String> savedata = (ArrayList<String>) ois.readObject();
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


}
