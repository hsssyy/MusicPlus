package com.he.musicplus.utils;


import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeTime {
    public static Timestamp StringChangeTime(String str) throws ParseException {
//        System.out.println(str);
        StringBuilder sb = new StringBuilder(str);
        for(int i = 0;i<str.length();i++){
            if(str.charAt(i)=='/'){
                sb.setCharAt(i,'-');
            }else{
                sb.setCharAt(i,str.charAt(i));
            }

        }
        String s = String.valueOf(sb);
        SimpleDateFormat sdf =   new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        java.util.Date  date = sdf.parse(s);// 2008-07-10 19:20:00

        Timestamp timestamp = new Timestamp(date.getTime());
        return timestamp;
    }
}
