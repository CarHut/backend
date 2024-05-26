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

    @Query(value = "SELECT * FROM messages WHERE recipient_id = :myId OR sender_id = :myId", nativeQuery = true)
    List<Message> getAllMyChats(@Param("myId") String myId);

    @Query("SELECT m FROM Message m WHERE m.date IN (" +
            "SELECT MAX(m2.date) FROM Message m2 " +
            "WHERE ((m2.senderId = :myId OR m2.recipientId = :myId) AND " +
            "((m2.senderId = m.senderId AND m2.recipientId = m.recipientId) " +
            "OR (m2.senderId = m.recipientId AND m2.recipientId = m.senderId))) " +
            "GROUP BY CASE WHEN m2.senderId < m2.recipientId THEN m2.senderId ELSE m2.recipientId END, " +
            "CASE WHEN m2.senderId < m2.recipientId THEN m2.recipientId ELSE m2.senderId END) " +
            "ORDER BY m.date DESC")
    List<Message> findLastMessagesFromUniqueChats(@Param("myId") String myId);
}
