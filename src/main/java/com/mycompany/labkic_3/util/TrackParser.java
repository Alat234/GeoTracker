package com.mycompany.labkic_3.util;

import com.mycompany.labkic_3.entity.TrackData;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TrackParser {
    public static final String DATA_DIRECTORY = "upload-dir";
    private static final Logger LOGGER = Logger.getLogger(TrackParser.class.getName());

    public static List<TrackData> parseFile(String filename) {
        try {
            if (TrackValidator.formatCheck(filename) == null) {
                throw new RuntimeException("Invalid file format of file: " + filename);
            } else if (TrackValidator.formatCheck(filename).equals(".kml")) {
                return parseKml(filename);
            } else if (TrackValidator.formatCheck(filename).equals(".gpx")) {
                return parseGpx(filename);
            } else {
                throw new RuntimeException("Format file error: " + filename);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse file " + filename, e);
            throw new RuntimeException("Failed to parse file " + filename, e);
        }
    }

    public static List<TrackData> parseKml(String filename) {
        List<TrackData> trackDataList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(getFilePath(filename)));
            doc.getDocumentElement().normalize();

            NodeList pointNodes = doc.getElementsByTagName("Point");

            for (int i = 0; i < pointNodes.getLength(); i++) {
                Node node = pointNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element pointElement = (Element) node;
                    NodeList coordinateNodes = pointElement.getElementsByTagName("coordinates");

                    if (coordinateNodes.getLength() > 0) {
                        String coordinatesText = coordinateNodes.item(0).getTextContent().trim();
                        String[] coords = coordinatesText.split(",");

                        if (coords.length >= 2) {
                            try {
                                double longitude = Double.parseDouble(coords[0].trim());
                                double latitude = Double.parseDouble(coords[1].trim());
                                trackDataList.add(new TrackData(latitude, longitude));
                            } catch (NumberFormatException e) {
                                LOGGER.log(Level.WARNING, "Invalid coordinate format in KML: " + coordinatesText, e);
                            }
                        }
                    }
                }
            }
            LOGGER.info("Successfully parsed KML file: " + filename + " with " + trackDataList.size() + " points");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse KML file " + filename, e);
            throw new RuntimeException("Failed to parse KML file " + filename, e);
        }
        return trackDataList;
    }

    public static List<TrackData> parseGpx(String filename) {
        List<TrackData> trackDataList = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new File(getFilePath(filename)));
            doc.getDocumentElement().normalize();

            NodeList trkptNodes = doc.getElementsByTagName("trkpt");
            for (int i = 0; i < trkptNodes.getLength(); i++) {
                Node node = trkptNodes.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    try {
                        double latitude = Double.parseDouble(element.getAttribute("lat"));
                        double longitude = Double.parseDouble(element.getAttribute("lon"));
                        trackDataList.add(new TrackData(latitude, longitude));
                    } catch (NumberFormatException e) {
                        LOGGER.log(Level.WARNING, "Invalid coordinate or altitude format in GPX at index: " + i, e);
                    }
                }
            }
            LOGGER.info("Successfully parsed GPX file: " + filename + " with " + trackDataList.size() + " points");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to parse GPX file " + filename, e);
            throw new RuntimeException("Failed to parse GPX file " + filename, e);
        }
        return trackDataList;
    }

    public static String getFilePath(String filename) {
        Path directoryPath = Paths.get(DATA_DIRECTORY).toAbsolutePath();
        File file = new File(directoryPath.toFile(), filename);
        try {
            if (file.exists()) {
                return file.getAbsolutePath();
            }
            throw new RuntimeException("Not found file on directory: " + file.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to get path for file " + filename, e);
            throw new RuntimeException("Failed to get path " + filename, e);
        }
    }
}
