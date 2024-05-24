package com.carhutchathistoryservice.services;

import com.carhutchathistoryservice.models.Message;
import com.carhutchathistoryservice.repository.CreatedChatsRepository;
import com.carhutchathistoryservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreatedChatsService {

    @Autowired
    private CreatedChatsRepository createdChatsRepository;
    @Autowired
    private MessageRepository messageRepository;

    public void saveAllMessagesToRespectiveArchives() {
        List<Message> messages = getAllMessages();
        
        
    }

    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }


}
