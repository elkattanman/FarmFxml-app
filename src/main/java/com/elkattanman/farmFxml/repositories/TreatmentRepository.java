package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Treatment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TreatmentRepository extends JpaRepository<Treatment, Integer>, JpaSpecificationExecutor<Treatment> {

}