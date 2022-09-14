package com.he.musicplus.utils;


import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChangeTime {
    public static Date StringChangeTime(String str) throws ParseException {
        java.sql.Date date = java.sql.Date.valueOf(str);
        return date;
    }
}
