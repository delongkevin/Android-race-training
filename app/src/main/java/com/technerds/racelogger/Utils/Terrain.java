package com.technerds.racelogger.Utils;

public class Terrain {
    private int Flat = 1;
    private int Hilly = 2;
    private int Rolling_Hills = 3;
    private int Mountains = 4;
    private int Sandy = 5;
    private int Muddy = 6;
    private int Bridge_Run = 7;
    private int Water_Crossing = 8;
   
    public static String getTerrainFromId(int pos) {
        switch (pos) {
            case 1:
                return "Flat";
            case 2:
                return "Hilly";
            case 3:
                return "Rolling Hills";
            case 4:
                return "Mountains";
            case 5:
                return "Sandy";
            case 6:
                return "Muddy";
            case 7:
                return "Bridge Run";
            case 8:
                return "Water Crossing";
                
            default:
                return null;
        }
    }

    public static String[] getTerrain() {
        String[] Types = {
                "Flat",
                "Hilly",
                "Rolling Hills",
                "Mountains",
                "Sandy",
                "Muddy",
                "Bridge Run",
                "Water Crossing"
        };

        return Types;
    }
}
