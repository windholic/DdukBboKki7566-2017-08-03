package com.kim0420.ddukbbokki7566;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by alfo6-1 on 2017-08-14.
 */

public class FavAdapter extends RecyclerView.Adapter {

    ArrayList<Item> items;
    Context context;
    LocationManager lm;
    LayoutInflater li;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;

    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;


    public FavAdapter(ArrayList<Item> items, Context context,LocationManager lm) {
        this.items = items;
        this.context = context;
        this.lm = lm;

        li = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        VH h = new VH(li.inflate(R.layout.item_recy_favo, parent, false));


        return h;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        ((VH) holder).title.setText(items.get(position).title.toString());
        ((VH) holder).pnum.setText(items.get(position).telephone.toString());
        ((VH) holder).comm.setHint(items.get(position).favcomment);
        ((VH) holder).btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((VH) holder).comm.setHint(((VH) holder).comm.getText().toString());
                ((VH) holder).comm.setText("");
                ((VH) holder).comm.clearFocus();
            }
        });
        ((VH) holder).call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("tel:" + items.get(position).telephone.toString());
                Intent intent = new Intent(Intent.ACTION_DIAL, uri);

                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                context.startActivity(intent);
            }
        });

        ((VH) holder).fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                items.get(position).willdelete = !items.get(position).willdelete;
                if (items.get(position).willdelete) {
                    ((VH) holder).fav.setImageResource(R.drawable.fbtn2b);
                } else ((VH) holder).fav.setImageResource(R.drawable.fbtn2f);
                Toast.makeText(context, "하트가 빈 상태에서 해당 화면을 종료하면 즐겨찾기가 삭제됩니다.", Toast.LENGTH_SHORT).show();
            }
        });
        ((VH) holder).scom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CommentActivity.class);
                intent.putExtra("title", items.get(position).title);
                context.startActivity(intent);
            }
        });
        ((VH) holder).nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager = lm;


                if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("위치 정보 확인 옵션이 설정되어 있지 않습니다." +
                            "앱의 사용을 위해서는 '위치 및 보안 설정'의 무선 네트워크 및 GPS 위성 옵션에 대해 사용으로 설정하십시오.")
                            .setCancelable(false).setPositiveButton("설정", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            context.startActivity(intent);
                        }
                    }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                } else {
                    Location location = null;

                    if (manager.isProviderEnabled("gps")) {
                        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            //
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }

                        location = manager.getLastKnownLocation("gps");
                    }else if(manager.isProviderEnabled("network")){
                        location = manager.getLastKnownLocation("network");
                    }else{
                        location = manager.getLastKnownLocation("passive");
                    }


                    double dx,dy;
                    double mx,my;
                    dx = items.get(position).nmapx / 0.0000001 * 0.0000001;
                    dy = items.get(position).nmapy / 0.0000001 * 0.0000001;
                    mx = location.getLatitude() / 0.0000001 * 0.0000001;
                    my = location.getLongitude() / 0.0000001 * 0.0000001;




                    String url ="daummaps://route?sp="+mx+","+my+"&ep="+
                            dy+","+dx+"&by=FOOT";

                    Toast.makeText(context, location.getLatitude()+","+location.getLongitude(), Toast.LENGTH_SHORT).show();

                    Uri uri = Uri.parse(url.toString());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(uri);
                    if(intent.resolveActivity(context.getPackageManager())!=null){
                        context.startActivity(intent);
                    }else {
                        Toast.makeText(context, "DaumMap이 설치되어있지 않습니다", Toast.LENGTH_SHORT).show();
                        Uri store = Uri.parse("market://details?id=net.daum.android.map");
                        Intent intent2 = new Intent();
                        intent2.setAction(Intent.ACTION_VIEW);
                        intent2.setData(store);
                        context.startActivity(intent2);

                    }

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView title,pnum;
        EditText comm;
        Button btn;
        ImageView call,fav,scom,nav;


        public VH(View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.tv_favo_title);
            pnum = (TextView)itemView.findViewById(R.id.tv_favo_pnum);
            comm = (EditText)itemView.findViewById(R.id.et_recyitem_favo);
            btn = (Button)itemView.findViewById(R.id.btn_recyitem_favo);
            call = (ImageView)itemView.findViewById(R.id.btn_fav_call);
            fav = (ImageView)itemView.findViewById(R.id.btn_fav_fav);
            scom = (ImageView)itemView.findViewById(R.id.btn_fav_com);
            nav = (ImageView)itemView.findViewById(R.id.btn_fav_nav);



        }
    }






}
