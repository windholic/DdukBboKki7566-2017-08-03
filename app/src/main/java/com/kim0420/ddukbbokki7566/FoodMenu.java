package com.kim0420.ddukbbokki7566;

import java.io.Serializable;

/**
 * Created by alfo6-1 on 2017-08-21.
 */

public class FoodMenu implements Serializable{

    String menu;
    int category;
    boolean willdel;

    public FoodMenu(String menu, int category) {
        this.menu = menu;
        this.category = category;
        willdel = false;
    }
}
