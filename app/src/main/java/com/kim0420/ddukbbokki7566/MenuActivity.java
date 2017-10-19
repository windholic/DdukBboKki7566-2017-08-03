package com.kim0420.ddukbbokki7566;

import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MenuActivity extends AppCompatActivity {

    RecyclerView recy1;
//    Switch switch1,switch2,switch3;

    MenuAdapter adapter1;
    ArrayList<FoodMenu> menus = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        loaddata();

        recy1 = (RecyclerView)findViewById(R.id.recy_menu1);
//        switch1 = (Switch)findViewById(R.id.switch_menu1);
        recy1.setLayoutManager(new GridLayoutManager(this,2));




        adapter1 = new MenuAdapter(this,menus);

        if(menus !=null && menus.size()!=0){
            recy1.setAdapter(adapter1);
            recy1.setVisibility(View.VISIBLE);
        }

    }


    public void savedata(){

        for(FoodMenu t: menus){
            t.willdel=false;
        }

        try {
            FileOutputStream fos = openFileOutput("datamenu.txt",MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(menus);
            oos.flush();
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void loaddata(){
        try {
            FileInputStream fis = openFileInput("datamenu.txt");
            ObjectInputStream ois = new ObjectInputStream(fis);
            menus = (ArrayList<FoodMenu>)ois.readObject();
            ois.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void clickadd1(View v){
        for(FoodMenu t : menus){
            t.willdel = false;
        }
        adapter1.notifyDataSetChanged();
        final Dialog adddialog = new Dialog(this);
        adddialog.setContentView(R.layout.dialog_addmenu);
        adddialog.setTitle("메뉴 추가");
        adddialog.show();

        Button btnadd = (Button)adddialog.findViewById(R.id.btn_add_dialog);
        Button btncan = (Button)adddialog.findViewById(R.id.btn_cancel_dialog);
        final EditText et = (EditText)adddialog.findViewById(R.id.et_menu);

        btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoodMenu t = new FoodMenu(et.getText().toString(),0);
                menus.add(t);
                savedata();
                recy1.setVisibility(View.VISIBLE);
                adapter1 = new MenuAdapter(MenuActivity.this,menus);
                recy1.setAdapter(adapter1);
                adddialog.dismiss();
                Toast.makeText(MenuActivity.this,menus.size()+"",Toast.LENGTH_SHORT).show();

            }
        });

        btncan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adddialog.dismiss();
            }
        });


    }

//    public void clickadd2(View v){
//        final Dialog adddialog = new Dialog(this);
//        adddialog.setContentView(R.layout.dialog_addmenu);
//        adddialog.setTitle("메뉴 추가");
//        adddialog.show();
//
//        Button btnadd = (Button)adddialog.findViewById(R.id.btn_add_dialog);
//        Button btncan = (Button)adddialog.findViewById(R.id.btn_cancel_dialog);
//        final EditText et = (EditText)adddialog.findViewById(R.id.et_menu);
//
//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(et.getText().toString().trim().length()>0){
//                    FoodMenu t = new FoodMenu(et.getText().toString(),1);
//                    menus.add(t);
//                    tmp2.add(t);
//                    recy2.setAdapter(null);
//                    recy2.setAdapter(adapter2);
//                    recy2.setVisibility(View.VISIBLE);
//                    savedata();
//                    adddialog.dismiss();
//                    Toast.makeText(MenuActivity.this,tmp2.size()+"",Toast.LENGTH_SHORT).show();
//                }
//
//            }
//        });
//
//        btncan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adddialog.dismiss();
//            }
//        });
//
//
//
//
//    }
//    public void clickadd3(View v){
//        final Dialog adddialog = new Dialog(this);
//        adddialog.setContentView(R.layout.dialog_addmenu);
//        adddialog.setTitle("메뉴 추가");
//        adddialog.show();
//
//        Button btnadd = (Button)adddialog.findViewById(R.id.btn_add_dialog);
//        Button btncan = (Button)adddialog.findViewById(R.id.btn_cancel_dialog);
//        final EditText et = (EditText)adddialog.findViewById(R.id.et_menu);
//
//        btnadd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(et.getText().toString().trim().length()>0){
//                    FoodMenu t = new FoodMenu(et.getText().toString(),2);
//                    menus.add(t);
//                    savedata();
//                    tmp3.add(t);
//                    recy3.setAdapter(null);
//                    recy3.setAdapter(adapter3);
//                    recy3.setVisibility(View.VISIBLE);
//                    adddialog.dismiss();
//                }
//
//            }
//        });
//
//        btncan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                adddialog.dismiss();
//            }
//        });
//    }

    public void clickdelete1(View v){
        for(FoodMenu t:menus){
            t.willdel = !t.willdel;
        }
        adapter1.notifyDataSetChanged();

    }
//    public void clickdelete2(View v){
//        for(FoodMenu t:tmp2){
//            t.willdel = !t.willdel;
//        }
//        adapter2.notifyDataSetChanged();
//
//
//    }
//    public void clickdelete3(View v){
//        for(FoodMenu t:tmp3){
//            t.willdel = !t.willdel;
//        }
//        adapter3.notifyDataSetChanged();
//    }

    @Override
    public void onBackPressed() {
        savedata();
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        savedata();
        super.onPause();
    }
}
