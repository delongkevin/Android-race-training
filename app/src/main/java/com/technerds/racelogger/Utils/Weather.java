package com.technerds.racelogger.Utils;

public class Weather {
    private int OK = 1;
    private int Beautiful = 2;
    private int Rainy = 3;
    private int Windy = 4;
    private int Humid = 5;

    public static String getWeatherFromId(int pos) {
        switch (pos) {
            case 1:
                return "OK";
            case 2:
                return "Beautiful";
            case 3:
                return "Rainy";
            case 4:
                return "Windy";
            case 5:
                return "Humid";
                
            default:
                return null;
        }
    }

    public static String[] getWeather() {
        String[] Types = {
                "OK",
                "Beautiful",
                "Rainy",
                "Windy",
                "Humid"
        };

        return Types;
    }
}
