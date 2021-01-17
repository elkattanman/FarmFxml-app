package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Spending;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SpendingRepository extends JpaRepository<Spending, Integer>, JpaSpecificationExecutor<Spending> {

}