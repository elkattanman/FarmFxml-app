package com.elkattanman.farmFxml.repositories;


import com.elkattanman.farmFxml.domain.Capital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CapitalRepository extends JpaRepository<Capital, Integer>, JpaSpecificationExecutor<Capital> {

}