package com.gmail.safordog.progspringmvc2a;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    Photo findPhotoById(long id);

    void deletePhotoById(long id);
}
