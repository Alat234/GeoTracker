
package com.mycompany.labkic_3.Model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String fileName;
    @Lob
    private String trackCoordinatesJson;
    @OneToMany(
            mappedBy = "uploadedFile",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<TrackData> trackDataList= new ArrayList<TrackData>();
    public  UploadedFile(String fileName) {
        this.fileName = fileName;

    }
    public  UploadedFile() {
        this.fileName = fileName;

    }

    public Long getId() { return id; }
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }


    public void setId(Long id) {
        this.id = id;
    }

    public void setTrackCoordinatesJson(String trackCoordinatesJson) {
        this.trackCoordinatesJson = trackCoordinatesJson;
    }

    public void setTrackDataList(List<TrackData> trackDataList) {
        this.trackDataList = trackDataList;
    }

    public String getTrackCoordinatesJson() {
        return trackCoordinatesJson;
    }
    @JsonIgnore
    public List<TrackData> getTrackDataList() {
        return trackDataList;
    }
    public void addTrackData(TrackData trackData) {
        trackDataList.add(trackData);
        trackData.setUploadedFile(this);
    }
}
