package com.carhutchathistoryservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "messages")
public class Message {

    @Id
    private String id;
    private String senderId;
    private String recipientId;
    private String message;
    private LocalDateTime date;
    private boolean delivered;

    public Message(String senderId, String recipientId, String message, boolean delivered) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.date = LocalDateTime.now();
        this.delivered = delivered;
        this.id = generateId(senderId, recipientId, message, date);
    }

    public Message(String id, String senderId, String recipientId, String message, LocalDateTime date, boolean delivered) {
        this.senderId = senderId;
        this.recipientId = recipientId;
        this.message = message;
        this.date = date;
        this.delivered = delivered;
        this.id = id;
    }

    public Message() {}

    private String generateId(String userFrom, String userTo, String message, LocalDateTime date) {
        String input = userFrom + userTo + message + date;

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
