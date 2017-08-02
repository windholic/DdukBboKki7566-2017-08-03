package com.kim0420.ddukbbokki7566;

import java.io.Serializable;

/**
 * Created by alfo6-1 on 2017-07-24.
 */

public class Item implements Serializable{

    String title;
    String link;
    String description;
    String telephone;
    String address;
    String roadAddress;
    Integer mapx;
    Integer mapy;

    public Item() {
    }

    public Item(String title, String telephone, String address, String roadAddress, Integer mapx, Integer mapy) {
        this.title = title;
        this.telephone = telephone;
        this.address = address;
        this.roadAddress = roadAddress;
        this.mapx = mapx;
        this.mapy = mapy;
    }


}
