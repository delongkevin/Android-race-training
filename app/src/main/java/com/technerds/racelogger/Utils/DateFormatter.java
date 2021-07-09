package com.technerds.racelogger.Utils;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormatter
{
    public static String getFormattedDateFromDateTimeString(String date) {
        Date newDate = null;
        Log.wtf("-Date", "Given Date : " + date);
        if (date != null) {
            try {
                // 2020-10-12 12:31:00
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                newDate = spf.parse(date);
                Log.wtf("-Date", "Parsed Date : " + newDate);
                spf= new SimpleDateFormat("MM/dd/yyyy");
                date = spf.format(newDate);
                
                Log.wtf("-Date", "Formatted Date : " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return date;
        } else {
            return date;
        }
        
    }
    
    public static String getFormattedDateTimeFromDateTimeStringAmPm(String date) {
        Date newDate = null;
        Log.wtf("-Date", "Given Date : " + date);
        if (date != null) {
            try {
                // 2020-10-12 12:31:00
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                newDate = spf.parse(date);
                Log.wtf("-Date", "Parsed Date : " + newDate);
                spf= new SimpleDateFormat("MM/dd/yyyy hh:mm a");
                date = spf.format(newDate);
                
                Log.wtf("-Date", "Formatted Date : " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return date;
        } else {
            return date;
        }
        
    }
    
    public static String getFormattedDateFromDateString(String date) {
        Date newDate = null;
        if (date != null) {
            try {
                // 2020-10-12
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                newDate = spf.parse(date);
                
                spf= new SimpleDateFormat("MM/dd/yyyy");
                date = spf.format(newDate);
                
                Log.wtf("-Date", "Formatted Date : " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return date;
        } else {
            return date;
        }
        
    }
    
    public static String getFormattedDateTimeFromDateTimeString(String date) {
        Date newDate = null;
        if (date != null) {
            try {
                // 2020-10-12 12:31:00
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                newDate = spf.parse(date);
                
                spf= new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
                date = spf.format(newDate);
                
                Log.wtf("-Date", "Formatted Date : " + date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return date;
        } else {
            return date;
        }
        
    }
    
    public static Date getDateFromString(String date) {
        Date newDate = null;
        if (date != null) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("MM-dd-yyyy");
                newDate = spf.parse(date);
                
                Log.wtf("-Date", "Formatted Date : " + newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return newDate;
        } else {
            return newDate;
        }
        
    }
    
    public static Date getDateTimeFromString(String date) {
        Date newDate = null;
        if (date != null) {
            try {
                SimpleDateFormat spf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
                newDate = spf.parse(date);
                
                Log.wtf("-Date", "Formatted Date : " + newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            
            return newDate;
        } else {
            return newDate;
        }
        
    }
}
