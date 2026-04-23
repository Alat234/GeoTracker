package com.mycompany.labkic_3.dto;

import com.mycompany.labkic_3.entity.UploadedFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompareResult {
    private List<UploadedFile> trackListForHtml;
    private List<UploadedFile> tracksToDrawOnMap;
    private List<String> comparedFileNames = new ArrayList<>();
    private int similarityStep;
    private Map<Long, Double> similarityOfTrack;
    private Double pairSimilarityPercentage;
    private List<Long> highlightedIds = new ArrayList<>();
    private String currentMode;
    private String searchQuery = "";
    private String sortBy = "newest";
    private List<RecentTrackDto> recentTracks = new ArrayList<>();
    private List<TrackStatsDto> selectedTrackStats = new ArrayList<>();
    private Integer sharedGeohashCount;
    private Integer unionGeohashCount;
    private String similarityLabel;
    private String infoMessage;

    public CompareResult() {
    }

    public CompareResult(List<UploadedFile> trackListForHtml, List<UploadedFile> tracksToDrawOnMap) {
        this.trackListForHtml = trackListForHtml;
        this.tracksToDrawOnMap = tracksToDrawOnMap;
    }

    public void setComparedFileNames(List<String> comparedFileNames) { this.comparedFileNames = comparedFileNames; }
    public List<String> getComparedFileNames() { return comparedFileNames; }
    public void setSimilarityStep(int similarityStep) { this.similarityStep = similarityStep; }
    public int getSimilarityStep() { return similarityStep; }
    public void setCurrentMode(String currentMode) { this.currentMode = currentMode; }
    public String getCurrentMode() { return currentMode; }
    public void setSimilarityOfTrack(Map<Long, Double> similarityOfTrack) { this.similarityOfTrack = similarityOfTrack; }
    public void setPairSimilarityPercentage(Double pairSimilarityPercentage) { this.pairSimilarityPercentage = pairSimilarityPercentage; }
    public void setHighlightedIds(List<Long> highlightedIds) { this.highlightedIds = highlightedIds; }
    public void setTrackListForHtml(List<UploadedFile> trackListForHtml) { this.trackListForHtml = trackListForHtml; }
    public void setTracksToDrawOnMap(List<UploadedFile> tracksToDrawOnMap) { this.tracksToDrawOnMap = tracksToDrawOnMap; }
    public List<UploadedFile> getTrackListForHtml() { return trackListForHtml; }
    public List<UploadedFile> getTracksToDrawOnMap() { return tracksToDrawOnMap; }
    public Map<Long, Double> getSimilarityOfTrack() { return similarityOfTrack; }
    public Double getPairSimilarityPercentage() { return pairSimilarityPercentage; }
    public List<Long> getHighlightedIds() { return highlightedIds; }
    public String getSearchQuery() { return searchQuery; }
    public void setSearchQuery(String searchQuery) { this.searchQuery = searchQuery; }
    public String getSortBy() { return sortBy; }
    public void setSortBy(String sortBy) { this.sortBy = sortBy; }
    public List<RecentTrackDto> getRecentTracks() { return recentTracks; }
    public void setRecentTracks(List<RecentTrackDto> recentTracks) { this.recentTracks = recentTracks; }
    public List<TrackStatsDto> getSelectedTrackStats() { return selectedTrackStats; }
    public void setSelectedTrackStats(List<TrackStatsDto> selectedTrackStats) { this.selectedTrackStats = selectedTrackStats; }
    public Integer getSharedGeohashCount() { return sharedGeohashCount; }
    public void setSharedGeohashCount(Integer sharedGeohashCount) { this.sharedGeohashCount = sharedGeohashCount; }
    public Integer getUnionGeohashCount() { return unionGeohashCount; }
    public void setUnionGeohashCount(Integer unionGeohashCount) { this.unionGeohashCount = unionGeohashCount; }
    public String getSimilarityLabel() { return similarityLabel; }
    public void setSimilarityLabel(String similarityLabel) { this.similarityLabel = similarityLabel; }
    public String getInfoMessage() { return infoMessage; }
    public void setInfoMessage(String infoMessage) { this.infoMessage = infoMessage; }
}
