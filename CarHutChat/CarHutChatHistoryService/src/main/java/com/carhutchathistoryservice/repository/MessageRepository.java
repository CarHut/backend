package com.carhutchathistoryservice.repository;

import com.carhutchathistoryservice.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query(value = "SELECT * FROM messages WHERE recipient_id = :recipientId AND delivered = false", nativeQuery = true)
    List<Message> getUndeliveredMessages(@Param("recipientId") String recipientId);

    @Query(value = "SELECT * FROM messages", nativeQuery = true)
    List<Message> getAllMessages();

}
