package com.mycompany.labkic_3.util;

public class TrackValidator {
    public static String formatCheck(String filename) {
        if (filename.endsWith(".kml")) {
            return ".kml";
        } else if (filename.endsWith(".gpx")) {
            return ".gpx";
        }
        return null;
    }
}
