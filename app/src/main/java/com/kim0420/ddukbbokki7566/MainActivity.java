package com.kim0420.ddukbbokki7566;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    ImageView Search,AroundSearch,Bookmark,Diary;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Search = (ImageView)findViewById(R.id.img1);
        AroundSearch = (ImageView)findViewById(R.id.img2);
        Bookmark = (ImageView)findViewById(R.id.img3);
        Diary = (ImageView)findViewById(R.id.img4);





    }

    public void clickbtn1(View v){

        Intent intent = new Intent(this,SearchActivity.class);
        startActivity(intent);




    }

    public void clickbtn2(View v){


    }

    public void clickbtn3(View v){


    }

    public void clickbtn4(View v){


    }

}
