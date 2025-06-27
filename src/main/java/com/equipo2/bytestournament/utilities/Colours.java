package com.equipo2.bytestournament.utilities;

public class Colours {
    public static String paintGreen(String text) {
        return "\u001B[32m" + text + "\u001B[0m";
    }

    public static String paintRed(String text) {
        return "\u001B[31m" + text + "\u001B[0m";
    }
}
