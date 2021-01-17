package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.TypeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TypeInfoRepository extends JpaRepository<TypeInfo, Integer>, JpaSpecificationExecutor<TypeInfo> {

}