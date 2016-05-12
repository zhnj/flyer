package com.njdp.njdp_drivers.util;

/**
 * Created by USER-PC on 2016/4/6.
 */
public class JsonUtil {

    //json解析
    public static String JsonToker(String str)
    {
        if(str!=null&&str.startsWith("<br")){
            str=str.substring(1);
        }
        return  str;
    }
}
