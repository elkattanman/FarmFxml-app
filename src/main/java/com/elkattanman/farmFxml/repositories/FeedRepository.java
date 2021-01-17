package com.elkattanman.farmFxml.repositories;

import com.elkattanman.farmFxml.domain.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FeedRepository extends JpaRepository<Feed, Integer>, JpaSpecificationExecutor<Feed> {

}