package com.mycompany.labkic_3.controller;

import com.mycompany.labkic_3.dto.CompareResult;
import com.mycompany.labkic_3.service.TrackComparisonService;
import com.mycompany.labkic_3.service.TrackService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class FileController {

    private final TrackService trackService;
    private final TrackComparisonService trackComparisonService;

    public FileController(TrackService trackService, TrackComparisonService trackComparisonService) {
        this.trackService = trackService;
        this.trackComparisonService = trackComparisonService;
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        trackService.uploadTrack(file);
        return "redirect:/";
    }

    @PostMapping("/delete")
    public String deleteSelected(@RequestParam(required = false) List<Long> selectedIds) {
        trackService.deleteSelected(selectedIds);
        return "redirect:/";
    }

    @PostMapping("/process")
    public String compareTracks(@RequestParam(required = false) List<Long> selectedIds,
                                @RequestParam String mode,
                                @RequestParam int similarityLevel,
                                Model model) {
        CompareResult result = trackComparisonService.compareTracks(selectedIds, similarityLevel, mode);
        model.addAttribute("result", result);
        return "index";
    }

    @GetMapping("/")
    public String getMainPage(Model model) {
        model.addAttribute("result", trackService.buildInitialResult());
        return "index";
    }
}
