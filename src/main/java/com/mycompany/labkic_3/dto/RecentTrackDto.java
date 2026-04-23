package com.mycompany.labkic_3.dto;

import java.time.LocalDateTime;

public class RecentTrackDto {
    private String fileName;
    private LocalDateTime uploadedAt;
    private int totalPoints;

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
    public int getTotalPoints() { return totalPoints; }
    public void setTotalPoints(int totalPoints) { this.totalPoints = totalPoints; }
}
