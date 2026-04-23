package com.mycompany.labkic_3.repository;

import com.mycompany.labkic_3.entity.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<UploadedFile, Long> {

    List<UploadedFile> findByOwnerId(Long ownerId);

    List<UploadedFile> findByOwnerUsername(String username);

    List<UploadedFile> findByIdInAndOwnerId(List<Long> ids, Long ownerId);

    Optional<UploadedFile> findByIdAndOwnerId(Long id, Long ownerId);

    @Query("SELECT DISTINCT substring(td.geohash,1,:step) from TrackData td where td.uploadedFile.id=:id and td.uploadedFile.owner.id=:ownerId")
    List<String> findUniqueGeohashesByStepAndOwnerId(@Param("id") Long id, @Param("step") int step, @Param("ownerId") Long ownerId);

    @Query("SELECT uf.id FROM UploadedFile uf where uf.owner.id=:ownerId")
    List<Long> findAllUploadFileIdsByOwnerId(@Param("ownerId") Long ownerId);
}
