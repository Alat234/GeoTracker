package com.mycompany.labkic_3.Controllers;
import com.mycompany.labkic_3.Services.TracksCompareServise;
import org.springframework.ui.Model;

import com.mycompany.labkic_3.Model.CompareResult;
import com.mycompany.labkic_3.Model.UploadedFile;
import com.mycompany.labkic_3.Services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class FileController {

    private final FileService fileService;
    private final TracksCompareServise tracksCompareServise;

    public FileController(FileService fileService, TracksCompareServise tracksCompareServise) {
        this.fileService = fileService;
        this.tracksCompareServise = tracksCompareServise;
    }


    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) {
        fileService.UploadedFile(file);
        return "redirect:/";

    }



    @PostMapping("/delete")
    public String DeleteAll(){
        fileService.DeleteAll();
        return "redirect:/";
    }
    @PostMapping("/process")
    public String CompareTracks(@RequestParam(required = false) List<Long> selectedIds,@RequestParam String mode,@RequestParam int similarityLevel,Model model){

      CompareResult result= tracksCompareServise.COMPARE_TRACKS_WITH_AUTOMATICALLY_СHOICE_METHOOD(selectedIds,similarityLevel,mode);
      result.setCurrentMode(mode);
      result.setSimilarityStep(similarityLevel);
       model.addAttribute("result",result);


        return "index";
    }
    @GetMapping("/")
    public String getMainPage(Model model) {
        List<UploadedFile>ListOfUploadedFile= fileService.GetAllFile();
        CompareResult result =new CompareResult();
        result.setTrackListForHtml(ListOfUploadedFile);
        model.addAttribute("result",result);
        return "index";
    }

    
}
