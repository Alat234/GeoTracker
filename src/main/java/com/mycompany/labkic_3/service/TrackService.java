package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.entity.AppUser;
import com.mycompany.labkic_3.entity.TrackData;
import com.mycompany.labkic_3.entity.UploadedFile;
import com.mycompany.labkic_3.exception.InvalidTrackFileException;
import com.mycompany.labkic_3.exception.TrackProcessingException;
import com.mycompany.labkic_3.repository.FileRepository;
import com.mycompany.labkic_3.util.Converter;
import com.mycompany.labkic_3.util.TrackParser;
import com.mycompany.labkic_3.util.TrackValidator;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class TrackService {
    private final FileRepository fileRepository;
    private final FileStorageService fileStorageService;
    private final CurrentUserService currentUserService;

    public TrackService(FileRepository fileRepository,
                        FileStorageService fileStorageService,
                        CurrentUserService currentUserService) {
        this.fileRepository = fileRepository;
        this.fileStorageService = fileStorageService;
        this.currentUserService = currentUserService;
    }

    public void uploadTrack(MultipartFile file) {
        String filename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        if (TrackValidator.formatCheck(filename) == null) {
            throw new InvalidTrackFileException("Unsupported file format. Use .kml or .gpx");
        }

        fileStorageService.store(file);
        saveTrackData(filename);
    }

    public void saveTrackData(String filename) {
        try {
            AppUser currentUser = currentUserService.getCurrentUser();

            UploadedFile uploadedFile = new UploadedFile();
            uploadedFile.setFileName(filename);
            uploadedFile.setOwner(currentUser);

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

    public int deleteSelected(List<Long> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty()) {
            return 0;
        }

        AppUser currentUser = currentUserService.getCurrentUser();
        List<UploadedFile> ownedSelectedFiles = fileRepository.findByIdInAndOwnerId(selectedIds, currentUser.getId());
        if (!ownedSelectedFiles.isEmpty()) {
            fileRepository.deleteAll(ownedSelectedFiles);
        }
        return ownedSelectedFiles.size();
    }
}
