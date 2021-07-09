package com.technerds.racelogger.Utils;

public class Months {
    private int January = 1;
    private int February = 2;
    private int March = 3;
    private int April = 4;
    private int May = 5;
    private int June = 6;
    private int July = 7;
    private int August = 8;
    private int September = 9;
    private int October = 10;
    private int November = 11;
    private int December = 12;

    public static String getMonthFromId(int pos) {
        switch (pos) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
                
            default:
                return null;
        }
    }

    public static String[] getMonths() {
        String[] Types = {
                "January",
                "February",
                "March",
                "April",
                "May",
                "June",
                "July",
                "August",
                "September",
                "October",
                "November",
                "December"
        };

        return Types;
    }
}
