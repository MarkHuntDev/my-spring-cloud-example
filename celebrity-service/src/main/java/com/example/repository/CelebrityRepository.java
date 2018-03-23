package com.example.repository;

import com.example.entity.Celebrity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CelebrityRepository extends JpaRepository<Celebrity, Long> {
}
