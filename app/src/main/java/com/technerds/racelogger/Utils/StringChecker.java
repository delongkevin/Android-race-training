package com.technerds.racelogger.Utils;

import com.technerds.racelogger.R;

public class StringChecker {
    
    public static boolean checkString(String value){
        if (value != null && (!value.equalsIgnoreCase(""))
                && value != "" && (!value.equalsIgnoreCase("0"))) {
            return true;
        } else {
            return false;
        }
    }
}
