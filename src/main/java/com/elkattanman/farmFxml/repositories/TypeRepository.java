package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Type;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface TypeRepository extends JpaRepository<Type, Integer>, JpaSpecificationExecutor<Type> {
    List<Type> findAllByBornsTrue();
    List<Type> findAllByBuyTrue();
    List<Type> findAllByDeathTrue();
    List<Type> findAllByFeedTrue();
    List<Type> findAllByReserveTrue();
    List<Type> findAllBySaleTrue();
    List<Type> findAllBySpendingTrue();
    List<Type> findAllByTreatmentTrue();
}