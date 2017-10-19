package com.kim0420.ddukbbokki7566;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

public class DiaryActivity extends AppCompatActivity {

    RecyclerView rv;
    DiaryAdapter adapter;
    TextView tv;

    ArrayList<diarydata> datas = new ArrayList<>();
    ArrayList<String> filelist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary);
        filelist = loadfilelist();

        rv = (RecyclerView)findViewById(R.id.recy_diary);
        tv = (TextView)findViewById(R.id.tv_diary);

        if(filelist!=null&&filelist.size()!=0){
            tv.setVisibility(View.INVISIBLE);
        }



        for(int i = 0; i < filelist.size();i++){
            String file = filelist.get(i).toString();
            diarydata tmp = loaddiary(file);
            datas.add(tmp);
        }



        adapter = new DiaryAdapter(this,datas);
        rv.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,true));
        rv.setAdapter(adapter);


    }


    public ArrayList<String> loadfilelist(){
        try {


//            String filePath = getApplicationContext().getFilesDir().getAbsolutePath().toString() + "/diary/";

            String filepath = Environment.getExternalStorageDirectory()+"/diary/";
            File mfolder = new File(filepath);
            if(!mfolder.exists()) {
                mfolder.mkdir();
            }

//            File file = new File(filePath+"filelist.txt");

            File file = new File(mfolder.getAbsolutePath()+"filelist.txt");
            if(!file.exists()||!file.canRead()) return new ArrayList<String>();

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

    public diarydata loaddiary(String filename){

        try {

//            String filePath = getApplicationContext().getFilesDir().getAbsolutePath().toString() + "diary/";
            String filepath = Environment.getExternalStorageDirectory()+"/diary/";
            File mfolder = new File(filepath);
            if(!mfolder.exists()) mfolder.mkdir();

            File file = new File(mfolder.getAbsolutePath()+"/"+filename.toString());
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            diarydata data = (diarydata)ois.readObject();
            ois.close();
            return data;


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
