package com.mycompany.labkic_3.controller;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.service.TrackComparisonService;
import com.mycompany.labkic_3.service.TrackDashboardService;
import com.mycompany.labkic_3.service.TrackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class FileController {

    private final TrackService trackService;
    private final TrackComparisonService trackComparisonService;
    private final TrackDashboardService trackDashboardService;

    public FileController(TrackService trackService,
                          TrackComparisonService trackComparisonService,
                          TrackDashboardService trackDashboardService) {
        this.trackService = trackService;
        this.trackComparisonService = trackComparisonService;
        this.trackDashboardService = trackDashboardService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        trackService.uploadTrack(file);
        redirectAttributes.addFlashAttribute("successMessage", "Track uploaded successfully");
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteSelected(@RequestParam(required = false) List<Long> selectedIds,
                                 RedirectAttributes redirectAttributes) {
        int deletedCount = trackService.deleteSelected(selectedIds);
        if (deletedCount > 0) {
            redirectAttributes.addFlashAttribute("successMessage", "Selected track(s) deleted successfully");
        } else {
            redirectAttributes.addFlashAttribute("infoMessage", "No tracks selected");
        }
        return "redirect:/";
    }

    @PostMapping("/process")
    public String compareTracks(@RequestParam(required = false) List<Long> selectedIds,
                                @RequestParam String mode,
                                @RequestParam int similarityLevel,
                                @RequestParam(required = false, defaultValue = "") String search,
                                @RequestParam(required = false, defaultValue = "newest") String sort,
                                Model model) {
        CompareResult result = trackComparisonService.compareTracks(selectedIds, similarityLevel, mode, search, sort);
        model.addAttribute("result", result);
        return "index";
    }

    @GetMapping("/")
    public String getMainPage(@RequestParam(required = false, defaultValue = "") String search,
                              @RequestParam(required = false, defaultValue = "newest") String sort,
                              Model model) {
        model.addAttribute("result", trackDashboardService.buildInitialDashboardResult(search, sort));
        return "index";
    }
}
