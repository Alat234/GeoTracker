package com.mycompany.labkic_3.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CompareResult {
    private List<UploadedFile> trackListForHtml;


    private List<UploadedFile> tracksToDrawOnMap;
    private List<String> comparedFileNames = new ArrayList<>();

    public void setComparedFileNames(List<String> comparedFileNames) {
        this.comparedFileNames = comparedFileNames;
    }

    public List<String> getComparedFileNames() {
        return comparedFileNames;
    }

    public CompareResult(List<UploadedFile> trackListForHtml, List<UploadedFile> tracksToDrawOnMap) {
        this.trackListForHtml = trackListForHtml;
        this.tracksToDrawOnMap = tracksToDrawOnMap;
    }

    public CompareResult(List<UploadedFile> tracksToDrawOnMap) {
        this.tracksToDrawOnMap = tracksToDrawOnMap;
    }
    private int similarityStep;

    public void setSimilarityStep(int similarityStep) {
        this.similarityStep = similarityStep;
    }

    public int getSimilarityStep() {
        return similarityStep;
    }

    private Map<Long,Double > similarityOfTrack;

    private Double pairSimilarityPercentage;

    private List<Long> highlightedIds = new ArrayList<>();
    private String CurrentMode;

    public void setCurrentMode(String currentMode) {
        CurrentMode = currentMode;
    }

    public String getCurrentMode() {
        return CurrentMode;
    }

    public CompareResult() {
    }

    public CompareResult(List<UploadedFile> trackListForHtml, Map<Long, Double> similarityOfTrack) {
        this.trackListForHtml = trackListForHtml;
        this.similarityOfTrack = similarityOfTrack;
    }

    public CompareResult(Map<Long, Double> similarityOfTrack) {
        this.similarityOfTrack = similarityOfTrack;
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

    public CompareResult(List<UploadedFile> trackListForHtml, List<UploadedFile> tracksToDrawOnMap, Map<Long, Double> similarityOfTrack, Double pairSimilarityPercentage, List<Long> highlightedIds) {
        this.trackListForHtml = trackListForHtml;
        this.tracksToDrawOnMap = tracksToDrawOnMap;
        this.similarityOfTrack = similarityOfTrack;
        this.pairSimilarityPercentage = pairSimilarityPercentage;
        this.highlightedIds = highlightedIds;
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
