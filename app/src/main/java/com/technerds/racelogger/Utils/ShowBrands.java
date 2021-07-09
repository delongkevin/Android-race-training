package com.technerds.racelogger.Utils;

public class ShowBrands {
    private int New_Balance = 1;
    private int Brooks = 2;
    private int Saucony = 3;
    private int Asics = 4;
    private int Nike = 5;
    private int Hoka_One_One = 6;
    private int Mizuno = 7;
    private int Adidas = 8;
    private int Altra = 9;

    public static String getShoeBrandFromId(int pos) {
        switch (pos) {
            case 1:
                return "New Balance";
            case 2:
                return "Brooks";
            case 3:
                return "Saucony";
            case 4:
                return "Asics";
            case 5:
                return "Nike";
            case 6:
                return "Hoka One One";
            case 7:
                return "Mizuno";
            case 8:
                return "Adidas";
            case 9:
                return "Altra";
            case 10:
                return "Other";
                
            default:
                return null;
        }
    }

    public static String[] getShoeBrands() {
        String[] Types = {
                "New Balance",
                "Brooks",
                "Saucony",
                "Asics",
                "Nike",
                "Hoka One One",
                "Mizuno",
                "Adidas",
                "Altra",
                "Other"
        };

        return Types;
    }
}
