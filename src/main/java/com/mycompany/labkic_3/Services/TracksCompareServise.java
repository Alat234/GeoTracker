package com.mycompany.labkic_3.Services;

import com.mycompany.labkic_3.Model.CompareResult;
import com.mycompany.labkic_3.Model.UploadedFile;
import com.mycompany.labkic_3.Repositories.FileRepository;
import org.springframework.stereotype.Service;

import java.sql.Array;
import java.util.*;

@Service
public class TracksCompareServise implements  ITracksCompareServise {
    private final FileRepository fileRepository;

    public TracksCompareServise(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public CompareResult COMPARE_TRACKS_WITH_AUTOMATICALLY_СHOICE_METHOOD(List<Long> FileID, int step, String mode) {
         CompareResult result=new CompareResult();



        if(Objects.equals(mode, "compare")){
            if (FileID.size() ==1) {
                 result= CompareOneFileWithOter(FileID.get(0),step);


            }
            else if (FileID.size() == 2) {
                 result=CompareTwoFile(FileID,step);


            }
            else  if (FileID.size() > 2) {
                 result=PrepareFileToShow(FileID);

            }


        }
        else if(Objects.equals(mode, "view")){
            result= PrepareFileToShow(FileID);

        }

        result.setHighlightedIds(FileID);
        result.setTrackListForHtml(fileRepository.findAll());
        return result;
    }

    @Override
    public CompareResult CompareOneFileWithOter(Long FileID,int step) {
        CompareResult compareResult = new CompareResult();
        Map<Long, Double> Map_result = new HashMap<>();


        List<String> temp = fileRepository.findUniqueGeohashesByStep(FileID, step);
        Set<String> coordinateTrackTCompare = new HashSet<>(temp); // Це "Множина А"


        List<Long> allFileIDs = fileRepository.findAllIdUploadFile();

        for (Long currentID : allFileIDs) {


            if (currentID.equals(FileID)) {

                continue;
            }


            Set<String> currentTrackSet = new HashSet<>(
                    fileRepository.findUniqueGeohashesByStep(currentID, step)
            );


            Set<String> intersection = new HashSet<>(coordinateTrackTCompare);
            Set<String> union = new HashSet<>(coordinateTrackTCompare);


            intersection.retainAll(currentTrackSet); // {A} & {Б} = {A & Б}


            union.addAll(currentTrackSet); // {A} U {Б} = {A U Б}


            double result = 0.0;
            if (union.size() > 0) {
                result = (double) intersection.size() / union.size();
            }

            Map_result.put(currentID, (result * 100));
        }

        compareResult.setSimilarityOfTrack(Map_result);

        return compareResult;
    }

    @Override
    public CompareResult CompareTwoFile(List<Long> FileID, int step) {

        CompareResult compareResult = new CompareResult();

        List<String> temp=fileRepository.findUniqueGeohashesByStep(FileID.get(0),step);
        Set<String> coordinateTrackTCompare = new HashSet<>(temp);
        Set<String> intersection = new HashSet<>(coordinateTrackTCompare);
        Set<String> union = new HashSet<>(coordinateTrackTCompare);
        union.addAll(fileRepository.findUniqueGeohashesByStep(FileID.get(1),step));
        intersection.retainAll(fileRepository.findUniqueGeohashesByStep(FileID.get(1),step));
        double result= (double) intersection.size() /union.size();

        compareResult.setPairSimilarityPercentage(result*100);


        List<String> names = new ArrayList<>();
        for (UploadedFile file : fileRepository.findAll()) {
            if (FileID.contains(file.getId())) {
                names.add(file.getFileName());
            }
            if (names.size() == 2) break;
        }
        compareResult.setComparedFileNames(names);

     return compareResult;
    }

    @Override
    public CompareResult PrepareFileToShow(List<Long> FilesID) {
        List<UploadedFile> FileToDrow=new ArrayList<>();
        for(Long Id:FilesID){
            fileRepository.findById(Id).ifPresent(FileToDrow::add);

        }

        return new CompareResult(FileToDrow,FileToDrow);
    }
}
