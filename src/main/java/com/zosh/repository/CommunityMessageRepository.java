// src/main/java/com/zosh/repository/CommunityMessageRepository.java
package com.zosh.repository;

import com.zosh.model.CommunityMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityMessageRepository extends JpaRepository<CommunityMessage, Long> {
}