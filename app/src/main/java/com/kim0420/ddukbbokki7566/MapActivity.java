package com.kim0420.ddukbbokki7566;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class MapActivity extends NMapActivity{

    NMapView mv;

    NMapViewerResourceProvider mMapViewerResourceProvider = null;

    String MapID = "U_s_ztBySozXRIBNFZmB";
    String MapPW = "4VE3J1KYOY";

    ArrayList<Item> items;
    int position;
    Item item;


    NMapOverlayItem mapOverlayItem;
    NMapPOIdataOverlay poiDataOverlay;
    NMapOverlayManager mOverlayManager;
    View contentmenu;

    NGeoPoint point;
    NMapLocationManager manager;
    NMapLocationManager.OnLocationChangeListener locationChangeListener;
    NMapPlacemark placemark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        items = (ArrayList<Item>)getIntent().getSerializableExtra("item");
        position = getIntent().getIntExtra("position",-1);


        item = items.get(position);

        GeoPoint geoPoint = new GeoPoint(item.mapx,item.mapy);

        geoPoint = GeoTrans.convert(GeoTrans.KATEC,GeoTrans.GEO,geoPoint);

        contentmenu = findViewById(R.id.contentmenu);

        locationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
            @Override
            public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
                if(nMapLocationManager.isMyLocationFixed()) {
                    point = nGeoPoint;
                    findPlacemarkAtLocation(point.getLongitude(), point.getLatitude());
                    Toast.makeText(MapActivity.this,point.getLongitude()+","+point.getLatitude(),Toast.LENGTH_SHORT).show();
                    return true;
                }else return false;

            }

            @Override
            public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
                Toast.makeText(MapActivity.this, "내 위치 찾기 실패", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

                Toast.makeText(MapActivity.this, "내 위치 찾기 불가", Toast.LENGTH_SHORT).show();
            }
        };



        mv = new NMapView(this);
        setContentView(mv);
        mv.setClientId(MapID);
        mv.setClickable(true);
        mv.setEnabled(true);
        mv.setFocusable(true);
        mv.setFocusableInTouchMode(true);
        mv.requestFocus();


        mMapViewerResourceProvider = new NMapViewerResourceProvider(this);
        mOverlayManager = new NMapOverlayManager(this, mv, mMapViewerResourceProvider);

        NGeoPoint point = new NGeoPoint(geoPoint.getX(),geoPoint.getY());

        for(Item t: items){
            GeoPoint tmp = new GeoPoint(t.mapx,t.mapy);
            tmp = GeoTrans.convert(GeoTrans.KATEC,GeoTrans.GEO,tmp);
            t.nmapx = tmp.getX();
            t.nmapy = tmp.getY();

        }



        NMapPOIdata mapPOIdata = new NMapPOIdata(items.size(),mMapViewerResourceProvider);

        mapPOIdata.beginPOIdata(items.size());
        for(int i= 0; i < items.size(); i++) {


            NGeoPoint tmp = new NGeoPoint(items.get(i).nmapx, items.get(i).nmapy);
            mapPOIdata.addPOIitem(tmp, items.get(i).title,getResources().getDrawable(R.drawable.ic_pin_01) , i);

        }
        mapPOIdata.endPOIdata();

        Toast.makeText(this,items.get(position).title,Toast.LENGTH_SHORT).show();

        poiDataOverlay = mOverlayManager.createPOIdataOverlay(mapPOIdata,null);
        poiDataOverlay.setOnStateChangeListener(maplistener);




        poiDataOverlay.showAllPOIdata(0);
        mv.getMapController().setMapCenter(point,14);

    }


    NMapPOIdataOverlay.OnStateChangeListener maplistener= new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(final NMapPOIdataOverlay nMapPOIdataOverlay, final NMapPOIitem nMapPOIitem) {

            if(nMapPOIitem==null){
                return;
            }


            ArrayList<Item> savedata = loadfavo();

            final PopupMenu popupMenu = new PopupMenu(MapActivity.this,contentmenu);
            popupMenu.getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
            popupMenu.setGravity(Gravity.CENTER);

            for(int i = 0 ; i<savedata.size();i++){
                if(savedata.get(i).title.equals(items.get(nMapPOIitem.getId()).title)){
                    popupMenu.getMenu().getItem(1).setTitle("즐겨찾기 해제");
                }else{
                    popupMenu.getMenu().getItem(1).setTitle("즐겨찾기 등록");
                }
            }


            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    String title = item.getTitle().toString();



                    if(title.equals("상세 정보")){

                    }else if(title.equals("즐겨찾기 등록")) {
                        items.get(nMapPOIitem.getId()).favo = !items.get(nMapPOIitem.getId()).favo;

                        ArrayList<Item> savedata = loadfavo();
                        savedata.add(items.get(nMapPOIitem.getId()));
                        savefavo(savedata);
                        Toast.makeText(MapActivity.this, "즐겨찾기가 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    }else if(title.equals("즐겨찾기 해제")){
                        items.get(nMapPOIitem.getId()).favo = !items.get(nMapPOIitem.getId()).favo;

                        ArrayList<Item> savedata = loadfavo();
                        for(int i = savedata.size()-1; i>=0;i--){
                            if(savedata.get(i).title.equals(items.get(nMapPOIitem.getId()).title)){
                                savedata.remove(i);
                            }
                        }
                        savefavo(savedata);
                        Toast.makeText(MapActivity.this,"즐겨찾기가 삭제되었습니다.",Toast.LENGTH_SHORT).show();

                    }else if(title.equals("기록 쓰기")){
                        Intent intent = new Intent(MapActivity.this,WriteActivity.class);
                        intent.putExtra("title",items.get(nMapPOIitem.getId()).title);
                        intent.putExtra("addr",items.get(nMapPOIitem.getId()).address);
                        startActivity(intent);
                        MapActivity.this.finish();


                    }else if(title.equals("한 줄 평 보기")){
                        Intent intent = new Intent(MapActivity.this,CommentActivity.class);
                        intent.putExtra("title",items.get(nMapPOIitem.getId()).title);
                        intent.putExtra("pnum",items.get(nMapPOIitem.getId()).telephone);
                        startActivity(intent);
                        MapActivity.this.finish();

                    }else if(title.equals("길 찾기")){

                        setMapDataProviderListener(new NMapActivity.OnDataProviderListener() {
                            @Override
                            public void onReverseGeocoderResponse(NMapPlacemark nMapPlacemark, NMapError nMapError) {
                                if(nMapError!=null) {
                                    Toast.makeText(MapActivity.this, nMapError.message.toString() + "error", Toast.LENGTH_SHORT).show();
                                }
                                placemark = nMapPlacemark;
                            }
                        });


                        manager = new NMapLocationManager(MapActivity.this);
                        manager.setOnLocationChangeListener(MapActivity.this.locationChangeListener);
                        manager.enableMyLocation(false);


                        NGeoPoint mGeoPoint = manager.getMyLocation();

                        double dx,dy;
                        dx = items.get(nMapPOIitem.getId()).nmapx / 0.0000001 * 0.0000001;
                        dy = items.get(nMapPOIitem.getId()).nmapy / 0.0000001 * 0.0000001;


                        String url ="daummaps://route?sp="+mGeoPoint.getLatitude()+","+mGeoPoint.getLongitude()+"&ep="+
                                dy+","+dx+"&by=FOOT";

                        Uri uri = Uri.parse(url.toString());
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setData(uri);
                        if(intent.resolveActivity(getPackageManager())!=null){
                            startActivity(intent);
                        }else {
                            Toast.makeText(MapActivity.this, "DaumMap이 설치되어있지 않습니다", Toast.LENGTH_SHORT).show();
                            Uri store = Uri.parse("market://details?id=net.daum.android.map");
                            Intent intent2 = new Intent();
                            intent2.setAction(Intent.ACTION_VIEW);
                            intent2.setData(store);
                            startActivity(intent2);

                        }

                    }else if(title.equals("취소")){
                        popupMenu.dismiss();
                    }







                    return true;
                }
            });
            popupMenu.show();

        }

        @Override
        public void onCalloutClick(NMapPOIdataOverlay nMapPOIdataOverlay, NMapPOIitem nMapPOIitem) {

        }
    };

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
        manager.removeOnLocationChangeListener(locationChangeListener);
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onPause() {
        manager.removeOnLocationChangeListener(locationChangeListener);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        manager.removeOnLocationChangeListener(locationChangeListener);
        super.onDestroy();
    }


}
