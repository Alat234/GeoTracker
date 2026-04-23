package com.mycompany.labkic_3.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.labkic_3.entity.TrackData;

import java.util.ArrayList;
import java.util.List;

public class Converter {

    public static String convertTrackDataToJson(List<TrackData> trackDataList) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<List<Double>> coordinatesList = new ArrayList<>();

            for (TrackData trackData : trackDataList) {
                List<Double> point = new ArrayList<>();
                point.add(trackData.getLatitude());
                point.add(trackData.getLongitude());
                coordinatesList.add(point);
            }

            return objectMapper.writeValueAsString(coordinatesList);
        } catch (Exception e) {
            return "[]";
        }
    }

    public static StringBuilder convertPointsToString(double lat, double lon, int step) {
        StringBuilder pointsString = new StringBuilder();
        double xK = 180;
        double yK = 90;
        double xP = -180;
        double yP = -90;
        for (int i = 0; i < step; i++) {
            if ((lat < ((xP + xK) / 2)) && (lon > ((yP + yK) / 2))) {
                xK = ((xP + xK) / 2);
                yP = ((yP + yK) / 2);
                pointsString.append("p");
                continue;
            }
            if ((lat > ((xP + xK) / 2)) && (lon > ((yP + yK) / 2))) {
                xP = (xP + xK) / 2;
                yP = (yP + yK) / 2;
                pointsString.append("q");
                continue;
            }
            if ((lat < ((xP + xK) / 2)) && (lon < ((yK + yP) / 2))) {
                xK = ((xP + xK) / 2);
                yK = ((yP + yK) / 2);
                pointsString.append("s");
                continue;
            }
            if ((lat > ((xP + xK) / 2)) && (lon < ((yP + yK) / 2))) {
                xP = (xP + xK) / 2;
                yK = ((yP + yK) / 2);
                pointsString.append("t");
            }
        }
        return pointsString;
    }
}
