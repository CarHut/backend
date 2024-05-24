package com.carhutchathistoryservice.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Date;

@Entity
@Table(name = "created_chats")
public class CreatedChats {

    @Id
    private String id;
    private String userChatInitiatorId;
    private String userChatReceiverId;
    private Date dateCreated;
    private Integer size;
    private String content;

    public CreatedChats() {}

    public CreatedChats(String id, String userChatInitiatorId, String userChatReceiverId, Date dateCreated, Integer size, String content) {
        this.id = id;
        this.userChatInitiatorId = userChatInitiatorId;
        this.userChatReceiverId = userChatReceiverId;
        this.dateCreated = dateCreated;
        this.size = size;
        this.content = content;
    }

    public CreatedChats(String userChatInitiatorId, String userChatReceiverId, Date dateCreated, Integer size, String content) {
        this.id = generateId(userChatInitiatorId, userChatReceiverId, dateCreated, size, content);
        this.userChatInitiatorId = userChatInitiatorId;
        this.userChatReceiverId = userChatReceiverId;
        this.dateCreated = dateCreated;
        this.size = size;
        this.content = content;
    }

    private String generateId(String userChatInitiatorId, String userChatReceiverId, Date dateCreated, Integer size, String content) {
        String input = userChatInitiatorId + userChatReceiverId + dateCreated;

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

    public String getId() {
        return id;
    }

    public String getUserChatInitiatorId() {
        return userChatInitiatorId;
    }

    public String getUserChatReceiverId() {
        return userChatReceiverId;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Integer getSize() {
        return size;
    }

    public String getContent() {
        return content;
    }
}
