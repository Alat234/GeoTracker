package com.mycompany.labkic_3.ExtraUtilities;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.labkic_3.Model.TrackData;
import java.util.ArrayList;
import java.util.List;

public class Convertor {

    public static String ConvertTrackDataToJSON(List<TrackData> trackDataList){
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
            System.out.println("Error converting to JSON: " + e.getMessage());

            return "[]";
        }

    }
    public static StringBuilder convertPointsToString(double lat, double lon, int step) {
        StringBuilder PointsString = new StringBuilder();


        double xK = 180;
        double yK = 90;
        double xP = -180;
        double yP = -90;
        for (int i = 0; i < step; i++) {
            //Лівий верхній
            if ((lat < ((xP + xK) / 2)) && (lon > ((yP + yK) / 2))) {
                xP = xP;
                xK = ((xP + xK) / 2);
                yP = ((yP + yK) / 2);
                yK = yK;

                PointsString.append("p");
                continue;
            }
            //Правий верхній
            if ((lat > ((xP + xK) / 2)) && (lon > ((yP + yK) / 2))) {
                xP = (xP + xK) / 2;
                xK = xK;
                yP = (yP + yK) / 2;
                yK = yK;
                PointsString.append("q");
                continue;

            }
            //Лівий нижній
            if ((lat < ((xP + xK) / 2)) && (lon < ((yK + yP) / 2))) {
                xP = xP;
                xK = ((xP + xK) / 2);
                yP = yP;
                yK = ((yP + yK) / 2);
                PointsString.append("s");
                continue;
            }
            //Првий нижній
            if ((lat > ((xP + xK) / 2)) && (lon < ((yP + yK) / 2))) {
                xP = (xP + xK) / 2;
                xK = xK;
                yP = yP;
                yK = ((yP + yK) / 2);
                PointsString.append("t");


            }

        }

        return PointsString;
    }
    static public ArrayList<StringBuilder> ConvertTrackDataToStringArray(ArrayList<TrackData> ListOFDoublePoints, int step) {
        ArrayList<StringBuilder> ListOfStringPoints = new ArrayList<>();
        for (int i = 0; i < ListOFDoublePoints.size(); i++) {
            double lat = ListOFDoublePoints.get(i).getLatitude();
            double lon = ListOFDoublePoints.get(i).getLongitude();
            StringBuilder pointString = convertPointsToString(lat, lon, step);

            if (!ListOfStringPoints.contains(pointString)) {
                ListOfStringPoints.add(pointString);
            } else {
                System.out.println("Рядок вже присутній у списку: " + pointString);
            }
        }
        return ListOfStringPoints;
    }

}
