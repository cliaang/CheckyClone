package com.example.checky;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class Config {
    public static  String TODAY;
    public static  String YESTERDAY;
    public static String[] DAYS = new String[7];

    public static void configDays(){
        Calendar c = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        for(int i = 0; i<7;i++){
            if (i!=0){
                c.add(Calendar.DATE, -1);
            }
            DAYS[i] = dateFormat.format(c.getTime());
            Log.e("tag", "get day " + i + " :" + DAYS[i]);
        }
        TODAY = DAYS[0];
        YESTERDAY = DAYS[1];
    }

}
