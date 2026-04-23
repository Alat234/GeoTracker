package com.mycompany.labkic_3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    @Lob
    private String trackCoordinatesJson;

    private LocalDateTime uploadedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private AppUser owner;

    @OneToMany(mappedBy = "uploadedFile", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TrackData> trackDataList = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (uploadedAt == null) {
            uploadedAt = LocalDateTime.now();
        }
    }

    public UploadedFile() {
    }

    public UploadedFile(String fileName) {
        this.fileName = fileName;
    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    public void setId(Long id) { this.id = id; }
    public void setTrackCoordinatesJson(String trackCoordinatesJson) { this.trackCoordinatesJson = trackCoordinatesJson; }
    public void setTrackDataList(List<TrackData> trackDataList) { this.trackDataList = trackDataList; }
    public String getTrackCoordinatesJson() { return trackCoordinatesJson; }
    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }

    @JsonIgnore
    public AppUser getOwner() { return owner; }
    public void setOwner(AppUser owner) { this.owner = owner; }

    @JsonIgnore
    public List<TrackData> getTrackDataList() { return trackDataList; }

    public void addTrackData(TrackData trackData) {
        trackDataList.add(trackData);
        trackData.setUploadedFile(this);
    }
}
