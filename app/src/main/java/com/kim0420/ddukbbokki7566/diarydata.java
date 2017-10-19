package com.kim0420.ddukbbokki7566;

import java.io.Serializable;

/**
 * Created by alfo6-1 on 2017-10-16.
 */

public class diarydata implements Serializable {
    String addr,title,date,content,ctitle;


    public diarydata(String title,String addr, String date) {
        this.addr = addr;
        this.title = title;
        this.date = date;
        this.ctitle="";
        this.content="";
    }
}
