package com.technerds.racelogger.Utils;

public class AgeGroups {
    private int ten_under = 1;
    private int eleven_fourteen = 2;
    private int fifteen_ninteen = 3;
    private int twenty_twentyfour = 4;
    private int twentyfive_twentynine = 5;
    private int thirty_thirtyfour = 6;
    private int thirtyfive_thirtynine = 7;
    private int forty_fortyfour = 8;
    private int fortyfive_fortynine = 9;
    private int fifty_fiftyfour = 10;
    private int fiftyfive_fiftynine = 11;
    private int sixty_sixtyfour = 12;
    private int sixtyfive_sixtynine = 13;
    private int seventy_seventyfour = 14;
    private int seventyfive_plus = 15;
    
    public static String getAgeGroupFromId(int pos) {
        switch (pos) {
            case 1:
                return "10 and under";
            case 2:
                return "11-14";
            case 3:
                return "15-19";
            case 4:
                return "20-24";
            case 5:
                return "25-29";
            case 6:
                return "30-34";
            case 7:
                return "35-39";
            case 8:
                return "40-44";
            case 9:
                return "45-49";
            case 10:
                return "50-54";
            case 11:
                return "55-59";
            case 12:
                return "60-64";
            case 13:
                return "65-69";
            case 14:
                return "70-74";
            case 15:
                return "75+";
            
            default:
                return null;
        }
    }
    
    public static int getAgeGroupFromDOB(long year) {
        if (year < 11) {
            return 1;
        } else if (year < 15) {
            return 2;
        } else if (year < 20) {
            return 3;
        } else if (year < 25) {
            return 4;
        } else if (year < 30) {
            return 5;
        } else if (year < 35) {
            return 6;
        } else if (year < 40) {
            return 7;
        } else if (year < 45) {
            return 8;
        } else if (year < 50) {
            return 9;
        } else if (year < 55) {
            return 10;
        } else if (year < 60) {
            return 11;
        } else if (year < 65) {
            return 12;
        } else if (year < 70) {
            return 13;
        } else if (year < 75) {
            return 14;
        } else {
            return 15;
        }
    }
    
    public static String[] getAgrGroups() {
        String[] Types = {
                "10 and under",
                "11-14",
                "15-19",
                "20-24",
                "25-29",
                "30-34",
                "35-39",
                "40-44",
                "45-49",
                "50-54",
                "55-59",
                "60-64",
                "65-69",
                "70-74",
                "75+"
        };
        
        return Types;
    }
}
