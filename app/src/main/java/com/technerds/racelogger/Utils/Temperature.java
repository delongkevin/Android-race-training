package com.technerds.racelogger.Utils;

public class Temperature {
    private int Hot = 1;
    private int Nice = 2;
    private int Cool = 3;
    private int Cold = 4;
    
    
    public static String getTemperatureFromId(int pos) {
        switch (pos) {
            case 1:
                return "Hot (70+ Degrees)";
            case 2:
                return "Nice (55-69)";
            case 3:
                return "Cool (45-54)";
            case 4:
                return "Cold (Under 45)";
            
            default:
                return null;
        }
    }
    
    public static int getTemperatureId(String temp) {
        
        if (temp.equalsIgnoreCase("Hot (70+ Degrees)")) {
            return 1;
        } else if (temp.equalsIgnoreCase("Nice (55-69)")) {
            return 2;
        } else if (temp.equalsIgnoreCase("Cool (45-54)")) {
            return 3;
        } else if (temp.equalsIgnoreCase("Cold (Under 45)")) {
            return 4;
        } else {
            return 1;
        }
    }
    
    public static String[] getTemperature() {
        String[] Types = {
                "Hot (70+ Degrees)",
                "Nice (55-69)",
                "Cool (45-54)",
                "Cold (Under 45)"
        };
        
        return Types;
    }
}
