package com.mycompany.labkic_3.Services;
import com.mycompany.labkic_3.ExtraUtilities.Convertor;
import com.mycompany.labkic_3.ExtraUtilities.TrackParser;
import com.mycompany.labkic_3.ExtraUtilities.TrackValidator;
import com.mycompany.labkic_3.Model.TrackData;
import com.mycompany.labkic_3.Model.UploadedFile;
import com.mycompany.labkic_3.Repositories.FileRepository;
import com.mycompany.labkic_3.Storages.FileStorage;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


import static com.mycompany.labkic_3.ExtraUtilities.Convertor.convertPointsToString;


@Service
public class FileService implements IFileServise {
    private final Path rootLocation;
    private final FileRepository fileRepository;
    public  FileService(FileStorage storage, FileRepository repository) {
        this.rootLocation= Paths.get(storage.getLocation());
        fileRepository = repository;

    }
    @Override
    public void UploadedFile(MultipartFile file) {
        String filename= StringUtils.cleanPath(file.getOriginalFilename());

        try {

            if (file.isEmpty()) {
                throw new RuntimeException("Failed to store empty file " + filename);
            }

            if(TrackValidator.FormatCheck(filename)==null){
                throw new RuntimeException(
                        "Valid format of file: "
                                + filename);
            }

                try (InputStream saveStream = file.getInputStream()) {
                    Files.copy(saveStream, this.rootLocation.resolve(filename), StandardCopyOption.REPLACE_EXISTING);

                    AddFileDataToDB(filename);
                }

        }
        catch (Exception e) {
            throw new RuntimeException("Failed to store file " + filename, e);
        }
    }

    @Override
    public List<UploadedFile> GetAllFile() {
       return fileRepository.findAll();

    }
    @Override
    public void AddFileDataToDB(String filename) {

        UploadedFile fileDataToRecord =new UploadedFile();
        fileDataToRecord.setFileName(filename);
        List<TrackData> ListWithData=TrackParser.ParseFile(filename);
        fileDataToRecord.setTrackCoordinatesJson(Convertor.ConvertTrackDataToJSON(ListWithData));
        for(TrackData PointData: ListWithData){
            String geohashString= convertPointsToString(PointData.getLatitude(), PointData.getLongitude(), 30).toString();
            TrackData ObToRecord= new TrackData(
                    PointData.getLatitude(),
                    PointData.getLongitude(),
                    geohashString

            );

                fileDataToRecord.addTrackData(ObToRecord);

        }

        fileRepository.save(fileDataToRecord);

    }

    @Override
    public void DeleteAll() {
        fileRepository.deleteAll();
    }


}
