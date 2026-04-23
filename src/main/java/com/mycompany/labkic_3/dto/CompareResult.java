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

    public CompareResult() {
    }

    public CompareResult(List<UploadedFile> trackListForHtml, List<UploadedFile> tracksToDrawOnMap) {
        this.trackListForHtml = trackListForHtml;
        this.tracksToDrawOnMap = tracksToDrawOnMap;
    }

    public void setComparedFileNames(List<String> comparedFileNames) {
        this.comparedFileNames = comparedFileNames;
    }

    public List<String> getComparedFileNames() {
        return comparedFileNames;
    }

    public void setSimilarityStep(int similarityStep) {
        this.similarityStep = similarityStep;
    }

    public int getSimilarityStep() {
        return similarityStep;
    }

    public void setCurrentMode(String currentMode) {
        this.currentMode = currentMode;
    }

    public String getCurrentMode() {
        return currentMode;
    }

    public void setSimilarityOfTrack(Map<Long, Double> similarityOfTrack) {
        this.similarityOfTrack = similarityOfTrack;
    }

    public void setPairSimilarityPercentage(Double pairSimilarityPercentage) {
        this.pairSimilarityPercentage = pairSimilarityPercentage;
    }

    public void setHighlightedIds(List<Long> highlightedIds) {
        this.highlightedIds = highlightedIds;
    }

    public void setTrackListForHtml(List<UploadedFile> trackListForHtml) {
        this.trackListForHtml = trackListForHtml;
    }

    public void setTracksToDrawOnMap(List<UploadedFile> tracksToDrawOnMap) {
        this.tracksToDrawOnMap = tracksToDrawOnMap;
    }

    public List<UploadedFile> getTrackListForHtml() {
        return trackListForHtml;
    }

    public List<UploadedFile> getTracksToDrawOnMap() {
        return tracksToDrawOnMap;
    }

    public Map<Long, Double> getSimilarityOfTrack() {
        return similarityOfTrack;
    }

    public Double getPairSimilarityPercentage() {
        return pairSimilarityPercentage;
    }

    public List<Long> getHighlightedIds() {
        return highlightedIds;
    }
}
