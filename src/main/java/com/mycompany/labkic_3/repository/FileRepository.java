package com.mycompany.labkic_3.repository;

import com.mycompany.labkic_3.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends JpaRepository<UploadedFile, Long> {

    @Query("SELECT DISTINCT substring(td.geohash,1,:step) from TrackData td where td.uploadedFile.id=:id")
    List<String> findUniqueGeohashesByStep(@Param("id") Long id, @Param("step") int step);

    @Query("SELECT uf.id FROM UploadedFile uf")
    List<Long> findAllUploadFileIds();
}
