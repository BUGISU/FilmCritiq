package com.example.FilmCritiq.repository;

import com.example.FilmCritiq.entity.Photos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PhotosRepository extends JpaRepository<Photos, Long>  {
    @Modifying
    @Query("delete from Photos pt where pt.movies.mno=:mno")
    void deleteByFno(@Param("mno") long mno);

    @Modifying
    @Query("delete from Photos pt where pt.uuid=:uuid")
    void deleteByUuid(@Param("uuid")String uuid);

    @Query("select pt from Photos pt where pt.movies.mno=:mno")
    List<Photos> findByFno(@Param("mno") Long mno);
}
