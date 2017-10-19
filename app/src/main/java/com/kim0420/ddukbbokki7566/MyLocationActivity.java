package com.kim0420.ddukbbokki7566;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.maps.overlay.NMapPathData;
import com.nhn.android.maps.overlay.NMapPathLineStyle;
import com.nhn.android.mapviewer.overlay.NMapMyLocationOverlay;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPathDataOverlay;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
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

public class MyLocationActivity extends NMapActivity {


    NMapView mv;
    NMapViewerResourceProvider mMapViewerResourceProvider = null;

    String MapID = "U_s_ztBySozXRIBNFZmB";
    String MapPW = "4VE3J1KYOY";
    String SearchID = "oAn9W5C8wD4ldqnbpjdq";
    String SearchPW = "TnBUYZGxl8";

    String text_search;

    ArrayList<Item> items= new ArrayList<>();
    ArrayList<FoodMenu> menus = new ArrayList<>();
    int position;
    Item item;
    View contentmenu;


    NGeoPoint point;


    NMapOverlayItem mapOverlayItem;
    NMapPOIdataOverlay poiDataOverlay;
    NMapOverlayManager mOverlayManager;
    NMapLocationManager manager;

    NMapPlacemark placemark;
    NMapPathData pathData;
    NMapPathDataOverlay nMapPathDataOverlay;

    NMapLocationManager.OnLocationChangeListener locationChangeListener = new NMapLocationManager.OnLocationChangeListener() {
        @Override
        public boolean onLocationChanged(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {
            if(nMapLocationManager.isMyLocationFixed()) {
                point = nGeoPoint;
                findPlacemarkAtLocation(point.getLongitude(), point.getLatitude());
                Toast.makeText(MyLocationActivity.this,point.getLongitude()+","+point.getLatitude(),Toast.LENGTH_SHORT).show();
                return true;
            }else return false;

        }

        @Override
        public void onLocationUpdateTimeout(NMapLocationManager nMapLocationManager) {
            Toast.makeText(MyLocationActivity.this, "내 위치 찾기 실패", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onLocationUnavailableArea(NMapLocationManager nMapLocationManager, NGeoPoint nGeoPoint) {

            Toast.makeText(MyLocationActivity.this, "내 위치 찾기 불가", Toast.LENGTH_SHORT).show();
        }
    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mylocation);

        contentmenu = findViewById(R.id.contentmenu);

        loadmenu();


        setMapDataProviderListener(new NMapActivity.OnDataProviderListener() {
            @Override
            public void onReverseGeocoderResponse(NMapPlacemark nMapPlacemark, NMapError nMapError) {
                if(nMapError!=null) {
                    Toast.makeText(MyLocationActivity.this, nMapError.message.toString() + "error", Toast.LENGTH_SHORT).show();
                }
                placemark = nMapPlacemark;
            }
        });


        manager = new NMapLocationManager(this);
        manager.setOnLocationChangeListener(locationChangeListener);
        manager.enableMyLocation(false);




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






        synchronized (items) {
            new Thread() {
                @Override
                public void run() {




                    try {

                        if (!(items.isEmpty())) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    items.clear();
                                }
                            });

                        }

                        while (placemark==null){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(MyLocationActivity.this,"sleep",Toast.LENGTH_SHORT).show();
                                }
                            });
                            sleep(50);
                        }

                        for(int j = 0 ; j<menus.size();j++) {

                            text_search = placemark.toString() + " " + menus.get(j).menu.toString();

                            text_search = URLEncoder.encode(text_search, "utf-8");


                            String queryUrl = "https://openapi.naver.com/v1/search/local?query=" + text_search + "&display=30";

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
                                        synchronized (items) {
                                            for (Item t : items) {
                                                GeoPoint tmp = new GeoPoint(t.mapx, t.mapy);
                                                tmp = GeoTrans.convert(GeoTrans.KATEC, GeoTrans.GEO, tmp);
                                                t.nmapx = tmp.getX();
                                                t.nmapy = tmp.getY();

                                            }


                                            NMapPOIdata mapPOIdata = new NMapPOIdata(items.size(), mMapViewerResourceProvider);

                                            mapPOIdata.beginPOIdata(items.size());
                                            for (int i = 0; i < items.size(); i++) {


                                                NGeoPoint tmp = new NGeoPoint(items.get(i).nmapx, items.get(i).nmapy);
                                                mapPOIdata.addPOIitem(tmp, items.get(i).title, getResources().getDrawable(R.drawable.ic_pin_01), i);

                                            }
                                            mapPOIdata.endPOIdata();


                                            poiDataOverlay = mOverlayManager.createPOIdataOverlay(mapPOIdata, null);
//                                        poiDataOverlay.showAllPOIdata(0);
                                            poiDataOverlay.setOnStateChangeListener(maplistener);
                                        }

                                    }
                                });

                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(MyLocationActivity.this, items.size() + "망", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }


                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        }


        while(true) {
            if(point!=null)
            mv.getMapController().setMapCenter(point, 14);
            break;
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


    NMapPOIdataOverlay.OnStateChangeListener maplistener= new NMapPOIdataOverlay.OnStateChangeListener() {
        @Override
        public void onFocusChanged(final NMapPOIdataOverlay nMapPOIdataOverlay, final NMapPOIitem nMapPOIitem) {

            if(nMapPOIitem==null){
                return;
            }


            ArrayList<Item> savedata = loadfavo();

            final PopupMenu popupMenu = new PopupMenu(MyLocationActivity.this,contentmenu);
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
                        Toast.makeText(MyLocationActivity.this, "즐겨찾기가 등록되었습니다.", Toast.LENGTH_SHORT).show();

                    }else if(title.equals("즐겨찾기 해제")){
                        items.get(nMapPOIitem.getId()).favo = !items.get(nMapPOIitem.getId()).favo;

                        ArrayList<Item> savedata = loadfavo();
                        for(int i = savedata.size()-1; i>=0;i--){
                            if(savedata.get(i).title.equals(items.get(nMapPOIitem.getId()).title)){
                                savedata.remove(i);
                            }
                        }
                        savefavo(savedata);
                        Toast.makeText(MyLocationActivity.this,"즐겨찾기가 삭제되었습니다.",Toast.LENGTH_SHORT).show();

                    }else if(title.equals("기록 쓰기")){

                        Intent intent = new Intent(MyLocationActivity.this,WriteActivity.class);
                        intent.putExtra("title",items.get(nMapPOIitem.getId()).title);
                        intent.putExtra("addr",items.get(nMapPOIitem.getId()).address);
                        startActivity(intent);
                        MyLocationActivity.this.finish();

                    }else if(title.equals("한 줄 평 보기")){
                        Intent intent = new Intent(MyLocationActivity.this,CommentActivity.class);
                        intent.putExtra("title",items.get(nMapPOIitem.getId()).title);
                        intent.putExtra("pnum",items.get(nMapPOIitem.getId()).telephone);
                        startActivity(intent);
                        MyLocationActivity.this.finish();

                    }else if(title.equals("길 찾기")){
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
                            Toast.makeText(MyLocationActivity.this, "DaumMap이 설치되어있지 않습니다", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(MyLocationActivity.this, nMapPOIitem.getId()+"", Toast.LENGTH_SHORT).show();


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





}
