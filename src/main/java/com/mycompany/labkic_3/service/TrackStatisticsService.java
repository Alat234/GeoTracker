package com.mycompany.labkic_3.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.labkic_3.dto.TrackStatsDto;
import com.mycompany.labkic_3.entity.UploadedFile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrackStatisticsService {
    private static final double EARTH_RADIUS_KM = 6371.0;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TrackStatsDto calculateStats(UploadedFile file) {
        List<List<Double>> coordinates = parseCoordinates(file.getTrackCoordinatesJson());
        TrackStatsDto stats = new TrackStatsDto();
        stats.setTrackId(file.getId());
        stats.setFileName(file.getFileName());
        stats.setTotalPoints(coordinates.size());

        if (coordinates.isEmpty()) {
            return stats;
        }

        List<Double> start = coordinates.get(0);
        List<Double> end = coordinates.get(coordinates.size() - 1);

        stats.setStartLatitude(start.get(0));
        stats.setStartLongitude(start.get(1));
        stats.setEndLatitude(end.get(0));
        stats.setEndLongitude(end.get(1));

        double minLat = start.get(0);
        double maxLat = start.get(0);
        double minLon = start.get(1);
        double maxLon = start.get(1);
        double distance = 0.0;

        for (int i = 0; i < coordinates.size(); i++) {
            double lat = coordinates.get(i).get(0);
            double lon = coordinates.get(i).get(1);
            minLat = Math.min(minLat, lat);
            maxLat = Math.max(maxLat, lat);
            minLon = Math.min(minLon, lon);
            maxLon = Math.max(maxLon, lon);

            if (i > 0) {
                List<Double> prev = coordinates.get(i - 1);
                distance += haversineKm(prev.get(0), prev.get(1), lat, lon);
            }
        }

        stats.setMinLatitude(minLat);
        stats.setMaxLatitude(maxLat);
        stats.setMinLongitude(minLon);
        stats.setMaxLongitude(maxLon);
        stats.setApproximateDistanceKm(distance);
        return stats;
    }

    private List<List<Double>> parseCoordinates(String json) {
        try {
            if (json == null || json.isBlank()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    private double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 2 * EARTH_RADIUS_KM * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
    }
}
