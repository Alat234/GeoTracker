package com.mycompany.labkic_3.dto;

public class TrackStatsDto {
    private Long trackId;
    private String fileName;
    private int totalPoints;
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
    private Double minLatitude;
    private Double maxLatitude;
    private Double minLongitude;
    private Double maxLongitude;
    private Double approximateDistanceKm;

    public Long getTrackId() { return trackId; }
    public void setTrackId(Long trackId) { this.trackId = trackId; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
    public Double getStartLatitude() { return startLatitude; }
    public void setStartLatitude(Double startLatitude) { this.startLatitude = startLatitude; }
    public Double getStartLongitude() { return startLongitude; }
    public void setStartLongitude(Double startLongitude) { this.startLongitude = startLongitude; }
    public Double getEndLatitude() { return endLatitude; }
    public void setEndLatitude(Double endLatitude) { this.endLatitude = endLatitude; }
    public Double getEndLongitude() { return endLongitude; }
    public void setEndLongitude(Double endLongitude) { this.endLongitude = endLongitude; }
    public Double getMinLatitude() { return minLatitude; }
    public void setMinLatitude(Double minLatitude) { this.minLatitude = minLatitude; }
    public Double getMaxLatitude() { return maxLatitude; }
    public void setMaxLatitude(Double maxLatitude) { this.maxLatitude = maxLatitude; }
    public Double getMinLongitude() { return minLongitude; }
    public void setMinLongitude(Double minLongitude) { this.minLongitude = minLongitude; }
    public Double getMaxLongitude() { return maxLongitude; }
    public void setMaxLongitude(Double maxLongitude) { this.maxLongitude = maxLongitude; }
    public Double getApproximateDistanceKm() { return approximateDistanceKm; }
    public void setApproximateDistanceKm(Double approximateDistanceKm) { this.approximateDistanceKm = approximateDistanceKm; }
}
