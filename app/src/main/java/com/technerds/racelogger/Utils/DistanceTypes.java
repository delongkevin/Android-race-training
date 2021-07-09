package com.technerds.racelogger.Utils;

public class DistanceTypes {
    private int m = 1;
    private int km = 2;
    private int mi = 3;

    public static String getDistanceTypesFromId(int pos) {
        switch (pos) {
            case 1:
                return "m";
            case 2:
                return "km";
            case 3:
                return "mi";
            default:
                return null;
        }
    }

    public static String[] getDistanceTypes() {
        String[] distanceTypes = {
                "m",
                "km",
                "mi",
        };

        return distanceTypes;
    }
}
