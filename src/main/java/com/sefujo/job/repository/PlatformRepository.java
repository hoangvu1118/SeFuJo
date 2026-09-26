package com.sefujo.job.repository;

import com.sefujo.job.entity.Platform;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlatformRepository extends JpaRepository<Platform,Long> {
    Optional<Platform> findByName(String name);
}
