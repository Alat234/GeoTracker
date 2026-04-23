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
    private final TrackDashboardService trackDashboardService;
    private final TrackStatisticsService trackStatisticsService;

    public TrackComparisonService(FileRepository fileRepository,
                                  CurrentUserService currentUserService,
                                  TrackDashboardService trackDashboardService,
                                  TrackStatisticsService trackStatisticsService) {
        this.fileRepository = fileRepository;
        this.currentUserService = currentUserService;
        this.trackDashboardService = trackDashboardService;
        this.trackStatisticsService = trackStatisticsService;
    }

    public CompareResult compareTracks(List<Long> fileIds, int step, String mode, String search, String sortBy) {
        AppUser currentUser = currentUserService.getCurrentUser();
        Long ownerId = currentUser.getId();

        CompareResult result = new CompareResult();
        result.setCurrentMode(mode);
        result.setSimilarityStep(step);

        List<UploadedFile> ownedSelectedFiles = fileIds == null ? List.of() : fileRepository.findByIdInAndOwnerId(fileIds, ownerId);
        List<Long> visibleFileIds = ownedSelectedFiles.stream().map(UploadedFile::getId).toList();

        if (visibleFileIds.isEmpty()) {
            trackDashboardService.enrichResult(result, search, sortBy);
            result.setInfoMessage("No tracks selected");
            return result;
        }

        if (Objects.equals(mode, "compare")) {
            if (visibleFileIds.size() == 1) {
                result = compareOneFileWithOther(visibleFileIds.get(0), step, ownerId);
            } else if (visibleFileIds.size() == 2) {
                result = compareTwoFiles(visibleFileIds, step, ownerId);
            } else {
                result = prepareFilesToShow(visibleFileIds, ownerId);
            }
            result.setInfoMessage("Track comparison completed");
        } else if (Objects.equals(mode, "view")) {
            result = prepareFilesToShow(visibleFileIds, ownerId);
            result.setInfoMessage("Track view updated");
        } else {
            throw new TrackComparisonException("Unsupported mode");
        }

        result.setHighlightedIds(visibleFileIds);
        result.setCurrentMode(mode);
        result.setSimilarityStep(step);
        result.setSelectedTrackStats(ownedSelectedFiles.stream().map(trackStatisticsService::calculateStats).toList());
        trackDashboardService.enrichResult(result, search, sortBy);
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

        Set<String> coordinateTrackToCompare = new HashSet<>(fileRepository.findUniqueGeohashesByStepAndOwnerId(fileIds.get(0), step, ownerId));
        Set<String> intersection = new HashSet<>(coordinateTrackToCompare);
        Set<String> union = new HashSet<>(coordinateTrackToCompare);

        Set<String> secondTrack = new HashSet<>(fileRepository.findUniqueGeohashesByStepAndOwnerId(fileIds.get(1), step, ownerId));
        union.addAll(secondTrack);
        intersection.retainAll(secondTrack);

        double result = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
        compareResult.setPairSimilarityPercentage(result * 100);
        compareResult.setSharedGeohashCount(intersection.size());
        compareResult.setUnionGeohashCount(union.size());
        compareResult.setSimilarityLabel(buildSimilarityLabel(result * 100));

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

    private String buildSimilarityLabel(double similarityPercentage) {
        if (similarityPercentage >= 70) {
            return "High similarity";
        }
        if (similarityPercentage >= 35) {
            return "Medium similarity";
        }
        return "Low similarity";
    }
}
