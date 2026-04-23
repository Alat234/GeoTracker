package com.mycompany.labkic_3.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
public class TrackData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double latitude;
    private Double longitude;

    @Column(length = 50)
    private String geohash;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploaded_file_id")
    private UploadedFile uploadedFile;

    public TrackData() {
    }

    public TrackData(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public TrackData(Double latitude, Double longitude, String geohash) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.geohash = geohash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setGeohash(String geohash) {
        this.geohash = geohash;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public Long getId() {
        return id;
    }

    public String getGeohash() {
        return geohash;
    }

    @JsonIgnore
    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
