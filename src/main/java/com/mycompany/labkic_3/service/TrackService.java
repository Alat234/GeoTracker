package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.entity.TrackData;
import com.mycompany.labkic_3.entity.UploadedFile;
import com.mycompany.labkic_3.exception.InvalidTrackFileException;
import com.mycompany.labkic_3.exception.TrackProcessingException;
import com.mycompany.labkic_3.repository.FileRepository;
import com.mycompany.labkic_3.util.Converter;
import com.mycompany.labkic_3.util.TrackParser;
import com.mycompany.labkic_3.util.TrackValidator;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TrackService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;

    public TrackService(FileRepository fileRepository, FileStorageService fileStorageService) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
    }

    public void uploadTrack(MultipartFile file) {
        String filename = fileStorageService.store(file);
        if (TrackValidator.formatCheck(filename) == null) {
            throw new InvalidTrackFileException("Valid formats are .kml and .gpx: " + filename);
        }
        saveTrackData(filename);
    }

    public List<UploadedFile> getAllFiles() {
        return fileRepository.findAll();
    }

    public void saveTrackData(String filename) {
        try {
            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileName(filename);

            List<TrackData> data = TrackParser.parseFile(filename);
            uploadedFile.setTrackCoordinatesJson(Converter.convertTrackDataToJson(data));

            for (TrackData point : data) {
                String geohashString = Converter.convertPointsToString(point.getLatitude(), point.getLongitude(), 30).toString();
                uploadedFile.addTrackData(new TrackData(point.getLatitude(), point.getLongitude(), geohashString));
            }

            fileRepository.save(uploadedFile);
        } catch (Exception e) {
            throw new TrackProcessingException("Failed to process track file: " + filename, e);
        }
    }

    public void deleteSelected(List<Long> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            return;
        }
        fileRepository.deleteAllById(selectedIds);
    }

    public CompareResult buildInitialResult() {
        CompareResult result = new CompareResult();
        result.setTrackListForHtml(getAllFiles());
        result.setSimilarityStep(5);
        result.setCurrentMode("compare");
        return result;
    }
}
