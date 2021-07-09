package com.technerds.racelogger.Utils;

public class Distances {
    private int one_Mile = 1;
    private int Five_K = 2;
    private int Ten_K = 3;
    private int Fifteen_K = 4;
    private int Twenty_K = 5;
    private int Half_marathon = 6;
    private int Thirty_K = 7;
    private int Marathon = 8;
    private int Fifty_K = 9;
    private int Fifty_Mile = 10;
    private int Hundred_Mile = 11;

    public static String getDistanceFromId(int pos) {
        switch (pos) {
            case 1:
                return "1 Mile";
            case 2:
                return "5K";
            case 3:
                return "10K";
            case 4:
                return "15K";
            case 5:
                return "20K";
            case 6:
                return "1/2 Marathon";
            case 7:
                return "30K";
            case 8:
                return "Marathon";
            case 9:
                return "50K";
            case 10:
                return "50 Mile";
            case 11:
                return "100 Mile";
           
                
            default:
                return null;
        }
    }

    public static String[] getDistances() {
        String[] Types = {
                "1 Mile",
                "5K",
                "10K",
                "15K",
                "20K",
                "1/2 Marathon",
                "30K",
                "Marathon",
                "50K",
                "50 Mile",
                "100 Mile"
        };

        return Types;
    }
}
