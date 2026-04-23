package com.mycompany.labkic_3.service;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.dto.RecentTrackDto;
import com.mycompany.labkic_3.entity.AppUser;
import com.mycompany.labkic_3.entity.UploadedFile;
import com.mycompany.labkic_3.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class TrackDashboardService {
    private final FileRepository fileRepository;
    private final CurrentUserService currentUserService;
    private final TrackStatisticsService trackStatisticsService;

    public TrackDashboardService(FileRepository fileRepository,
                                 CurrentUserService currentUserService,
                                 TrackStatisticsService trackStatisticsService) {
        this.fileRepository = fileRepository;
        this.currentUserService = currentUserService;
        this.trackStatisticsService = trackStatisticsService;
    }

    public CompareResult buildInitialDashboardResult(String search, String sortBy) {
        CompareResult result = new CompareResult();
        result.setSimilarityStep(5);
        result.setCurrentMode("compare");
        enrichResult(result, search, sortBy);
        return result;
    }

    public void enrichResult(CompareResult result, String search, String sortBy) {
        AppUser currentUser = currentUserService.getCurrentUser();
        Long ownerId = currentUser.getId();

        String normalizedSearch = search == null ? "" : search.trim();
        String normalizedSort = normalizeSort(sortBy);

        result.setSearchQuery(normalizedSearch);
        result.setSortBy(normalizedSort);
        result.setTrackListForHtml(getFilteredTracks(ownerId, normalizedSearch, normalizedSort));
        result.setRecentTracks(getRecentTracks(ownerId));
    }

    private List<UploadedFile> getFilteredTracks(Long ownerId, String search, String sortBy) {
        List<UploadedFile> tracks = fileRepository.findByOwnerAndSearch(ownerId, search);

        Comparator<UploadedFile> comparator = switch (sortBy) {
            case "oldest" -> Comparator.comparing(UploadedFile::getUploadedAt, Comparator.nullsLast(Comparator.naturalOrder()));
            case "name_asc" -> Comparator.comparing(UploadedFile::getFileName, String.CASE_INSENSITIVE_ORDER);
            case "name_desc" -> Comparator.comparing(UploadedFile::getFileName, String.CASE_INSENSITIVE_ORDER).reversed();
            default -> Comparator.comparing(UploadedFile::getUploadedAt, Comparator.nullsLast(Comparator.reverseOrder()));
        };

        return tracks.stream().sorted(comparator).toList();
    }

    private List<RecentTrackDto> getRecentTracks(Long ownerId) {
        return fileRepository.findTop5ByOwnerIdOrderByUploadedAtDesc(ownerId)
                .stream()
                .map(file -> {
                    RecentTrackDto dto = new RecentTrackDto();
                    dto.setFileName(file.getFileName());
                    dto.setUploadedAt(file.getUploadedAt());
                    dto.setTotalPoints(trackStatisticsService.calculateStats(file).getTotalPoints());
                    return dto;
                })
                .toList();
    }

    public String normalizeSort(String sortBy) {
        if (sortBy == null || sortBy.isBlank()) {
            return "newest";
        }
        return switch (sortBy) {
            case "newest", "oldest", "name_asc", "name_desc" -> sortBy;
            default -> "newest";
        };
    }
}
