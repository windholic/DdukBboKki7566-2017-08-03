package com.kim0420.ddukbbokki7566;

import java.io.Serializable;

/**
 * Created by alfo6-1 on 2017-07-24.
 */

public class SearchData implements Serializable{

    String addr,date;

    public SearchData(String addr, String date) {
        this.addr = addr;
        this.date = date;
    }
}
