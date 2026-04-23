package com.mycompany.labkic_3.Repositories;
import com.mycompany.labkic_3.Model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FileRepository extends  JpaRepository<UploadedFile,Long> {

     UploadedFile findByFileName(String fileName);

     @Query("SELECT DISTINCT substring(td.geohash,1,:step)  from TrackData td where td.uploadedFile.id=:ID")
    List<String> findUniqueGeohashesByStep(@Param("ID")Long ID,@Param("step") int step);
    @Query("SELECT uf.id FROM UploadedFile uf")
     List<Long> findAllIdUploadFile();
}
