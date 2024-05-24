package com.carhutchat.repositories;

import com.carhutchat.models.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, String> {

    @Query(value = "SELECT * FROM messages WHERE recipient_id = :recipientId AND delivered = false", nativeQuery = true)
    List<Message> getUndeliveredMessages(@Param("recipientId") String recipientId);

    @Query(value = "SELECT * FROM messages " +
            "WHERE sender_id = :senderId AND recipient_id = :recipientId OR sender_id = :recipientId AND recipient_id = :senderId ORDER BY date DESC LIMIT 10", nativeQuery = true)
    List<Message> getLastTenMessagesWithUser(@Param("senderId") String senderId, @Param("recipientId") String recipientId);
}
