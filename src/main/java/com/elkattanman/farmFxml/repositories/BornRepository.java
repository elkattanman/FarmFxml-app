package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Born;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BornRepository extends JpaRepository<Born, Integer>, JpaSpecificationExecutor<Born> {

}