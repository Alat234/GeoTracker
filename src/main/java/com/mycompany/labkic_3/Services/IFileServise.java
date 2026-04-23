package com.mycompany.labkic_3.Services;
import com.mycompany.labkic_3.Model.UploadedFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IFileServise {
    public void UploadedFile(MultipartFile file);
    public List<UploadedFile> GetAllFile();


    public void AddFileDataToDB(String filename);
    public void DeleteAll();



}
