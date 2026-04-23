package com.mycompany.labkic_3.ExtraUtilities;

public class TrackValidator {
     public static String FormatCheck(String filename){
          if(filename.endsWith(".kml")){
              return ".kml";
          }
          else  if(filename.endsWith(".gpx")){
              return ".gpx";
          }
          else return  null;
      }
}
