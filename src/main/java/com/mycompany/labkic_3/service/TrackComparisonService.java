package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.entity.UploadedFile;
import com.mycompany.labkic_3.exception.TrackComparisonException;
import com.mycompany.labkic_3.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TrackComparisonService {
    private final FileRepository fileRepository;

    public TrackComparisonService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public CompareResult compareTracks(List<Long> fileIds, int step, String mode) {
        if (fileIds == null || fileIds.isEmpty()) {
            throw new TrackComparisonException("Select at least one track");
        }

        CompareResult result = new CompareResult();
        if (Objects.equals(mode, "compare")) {
            if (fileIds.size() == 1) {
                result = compareOneFileWithOther(fileIds.get(0), step);
            } else if (fileIds.size() == 2) {
                result = compareTwoFiles(fileIds, step);
            } else {
                result = prepareFilesToShow(fileIds);
            }
        } else if (Objects.equals(mode, "view")) {
            result = prepareFilesToShow(fileIds);
        }

        result.setHighlightedIds(fileIds);
        result.setTrackListForHtml(fileRepository.findAll());
        result.setCurrentMode(mode);
        result.setSimilarityStep(step);
        return result;
    }

    private CompareResult compareOneFileWithOther(Long fileId, int step) {
        CompareResult compareResult = new CompareResult();
        Map<Long, Double> similarityMap = new HashMap<>();

        List<String> temp = fileRepository.findUniqueGeohashesByStep(fileId, step);
        Set<String> coordinateTrackToCompare = new HashSet<>(temp);

        List<Long> allFileIds = fileRepository.findAllUploadFileIds();

        for (Long currentId : allFileIds) {
            if (currentId.equals(fileId)) {
                continue;
            }

            Set<String> currentTrackSet = new HashSet<>(fileRepository.findUniqueGeohashesByStep(currentId, step));
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

    private CompareResult compareTwoFiles(List<Long> fileIds, int step) {
        CompareResult compareResult = new CompareResult();

        List<String> temp = fileRepository.findUniqueGeohashesByStep(fileIds.get(0), step);
        Set<String> coordinateTrackToCompare = new HashSet<>(temp);
        Set<String> intersection = new HashSet<>(coordinateTrackToCompare);
        Set<String> union = new HashSet<>(coordinateTrackToCompare);

        union.addAll(fileRepository.findUniqueGeohashesByStep(fileIds.get(1), step));
        intersection.retainAll(fileRepository.findUniqueGeohashesByStep(fileIds.get(1), step));

        double result = union.isEmpty() ? 0.0 : (double) intersection.size() / union.size();
        compareResult.setPairSimilarityPercentage(result * 100);

        List<String> names = new ArrayList<>();
        for (UploadedFile file : fileRepository.findAll()) {
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

    private CompareResult prepareFilesToShow(List<Long> fileIds) {
        List<UploadedFile> fileToDraw = new ArrayList<>();
        for (Long id : fileIds) {
            fileRepository.findById(id).ifPresent(fileToDraw::add);
        }
        return new CompareResult(fileToDraw, fileToDraw);
    }
}
