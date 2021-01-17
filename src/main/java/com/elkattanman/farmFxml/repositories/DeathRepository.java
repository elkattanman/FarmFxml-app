package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Death;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DeathRepository extends JpaRepository<Death, Integer>, JpaSpecificationExecutor<Death> {

}