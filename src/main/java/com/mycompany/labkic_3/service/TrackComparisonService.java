package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.entity.AppUser;
import com.mycompany.labkic_3.entity.UploadedFile;
import com.mycompany.labkic_3.exception.TrackComparisonException;
import com.mycompany.labkic_3.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackComparisonService {
    private final FileRepository fileRepository;
    private final CurrentUserService currentUserService;

    public TrackComparisonService(FileRepository fileRepository, CurrentUserService currentUserService) {
        this.fileRepository = fileRepository;
        this.currentUserService = currentUserService;
    }

    public CompareResult compareTracks(List<Long> fileIds, int step, String mode) {
        if (fileIds == null || fileIds.isEmpty()) {
            throw new TrackComparisonException("Select at least one track");
        }

        AppUser currentUser = currentUserService.getCurrentUser();
        Long ownerId = currentUser.getId();

        List<UploadedFile> ownedSelectedFiles = fileRepository.findByIdInAndOwnerId(fileIds, ownerId);
        List<Long> visibleFileIds = ownedSelectedFiles.stream().map(UploadedFile::getId).toList();

        if (visibleFileIds.isEmpty()) {
            throw new TrackComparisonException("Select at least one track");
        }

        CompareResult result = new CompareResult();
        if (Objects.equals(mode, "compare")) {
            if (visibleFileIds.size() == 1) {
                result = compareOneFileWithOther(visibleFileIds.get(0), step, ownerId);
            } else if (visibleFileIds.size() == 2) {
                result = compareTwoFiles(visibleFileIds, step, ownerId);
            } else {
                result = prepareFilesToShow(visibleFileIds, ownerId);
            }
        } else if (Objects.equals(mode, "view")) {
            result = prepareFilesToShow(visibleFileIds, ownerId);
        }

        result.setHighlightedIds(visibleFileIds);
        result.setTrackListForHtml(fileRepository.findByOwnerId(ownerId));
        result.setCurrentMode(mode);
        result.setSimilarityStep(step);
        return result;
    }

    private CompareResult compareOneFileWithOther(Long fileId, int step, Long ownerId) {
        CompareResult compareResult = new CompareResult();
        Map<Long, Double> similarityMap = new HashMap<>();

        List<String> temp = fileRepository.findUniqueGeohashesByStepAndOwnerId(fileId, step, ownerId);
        Set<String> coordinateTrackToCompare = new HashSet<>(temp);

        List<Long> allFileIds = fileRepository.findAllUploadFileIdsByOwnerId(ownerId);

        for (Long currentId : allFileIds) {
            if (currentId.equals(fileId)) {
                continue;
            }

            Set<String> currentTrackSet = new HashSet<>(fileRepository.findUniqueGeohashesByStepAndOwnerId(currentId, step, ownerId));
            Set<String> intersection = new HashSet<>(coordinateTrackToCompare);
            Set<String> union = new HashSet<>(coordinateTrackToCompare);

            intersection.retainAll(currentTrackSet);
            union.addAll(currentTrackSet);

            double result = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
            similarityMap.put(currentId, result * 100);
        }

        compareResult.setSimilarityOfTrack(similarityMap);
        return compareResult;
    }

    private CompareResult compareTwoFiles(List<Long> fileIds, int step, Long ownerId) {
        CompareResult compareResult = new CompareResult();

        List<String> temp = fileRepository.findUniqueGeohashesByStepAndOwnerId(fileIds.get(0), step, ownerId);
        Set<String> coordinateTrackToCompare = new HashSet<>(temp);
        Set<String> intersection = new HashSet<>(coordinateTrackToCompare);
        Set<String> union = new HashSet<>(coordinateTrackToCompare);

        union.addAll(fileRepository.findUniqueGeohashesByStepAndOwnerId(fileIds.get(1), step, ownerId));
        intersection.retainAll(fileRepository.findUniqueGeohashesByStepAndOwnerId(fileIds.get(1), step, ownerId));

        double result = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
        compareResult.setPairSimilarityPercentage(result * 100);

        List<String> names = new ArrayList<>();
        for (UploadedFile file : fileRepository.findByOwnerId(ownerId)) {
            if (fileIds.contains(file.getId())) {
                names.add(file.getFileName());
            }
            if (names.size() == 2) {
                break;
            }
        }
        compareResult.setComparedFileNames(names);
        return compareResult;
    }

    private CompareResult prepareFilesToShow(List<Long> fileIds, Long ownerId) {
        List<UploadedFile> fileToDraw = new ArrayList<>();
        for (Long id : fileIds) {
            fileRepository.findByIdAndOwnerId(id, ownerId).ifPresent(fileToDraw::add);
        }
        return new CompareResult(fileToDraw, fileToDraw);
    }
}
