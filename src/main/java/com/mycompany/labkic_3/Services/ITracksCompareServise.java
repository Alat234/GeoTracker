package com.mycompany.labkic_3.Services;

import com.mycompany.labkic_3.Model.CompareResult;

import java.util.List;

public interface ITracksCompareServise {
    CompareResult COMPARE_TRACKS_WITH_AUTOMATICALLY_СHOICE_METHOOD(List<Long> FileID,int step, String mode);
    CompareResult CompareOneFileWithOter(Long FileID, int step);
    CompareResult CompareTwoFile(List<Long> FilesID,int step);
    CompareResult PrepareFileToShow(List<Long> FilesID);
}
