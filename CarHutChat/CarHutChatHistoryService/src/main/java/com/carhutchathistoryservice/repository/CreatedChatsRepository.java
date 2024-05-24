package com.carhutchathistoryservice.repository;

import com.carhutchathistoryservice.models.CreatedChats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CreatedChatsRepository extends JpaRepository<CreatedChats, String> {
}
