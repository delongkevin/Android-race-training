package com.technerds.racelogger.Utils;

public class CourseTypes {
    private int Road = 1;
    private int Trail_Off_Road = 2;
    private int Trail_Paved = 3;
    private int Obstacle = 4;
    private int Cross_Country = 5;
    private int Track = 6;
    private int Beach = 7;

    public static String getCourseTypesFromId(int pos) {
        switch (pos) {
            case 1:
                return "Road";
            case 2:
                return "Trail - Off Road";
            case 3:
                return "Trail - Paved";
            case 4:
                return "Obstacle";
            case 5:
                return "Cross Country";
            case 6:
                return "Track";
            case 7:
                return "Beach";
          
            default:
                return null;
        }
    }

    public static String[] getCourseTypes() {
        String[] Types = {
                "Road",
                "Trail - Off Road",
                "Trail - Paved",
                "Obstacle",
                "Cross Country",
                "Track",
                "Beach"
        };

        return Types;
    }
}
