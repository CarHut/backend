package com.carhutchat.services;

import com.carhutchat.models.Message;
import com.carhutchat.paths.CarHutAPIEndpoints;
import com.carhutchat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.*;
import java.util.List;

@Service
public class ChatCommunicationService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ExternalAPIService externalAPIService;

    public void saveMessageToDatabase(String sender, String recipient, String message, boolean delivered) {
        Message newMessage = new Message(sender, recipient, message, delivered);
        messageRepository.save(newMessage);
    }

    public List<Message> getUndeliveredMessages(String recipient) throws Exception {
        try {
            String id = externalAPIService.getUserIdByName(recipient);
            return messageRepository.getUndeliveredMessages(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public void markMessagesAsDelivered(List<Message> messages) {
        messages.forEach(message -> message.setDelivered(true));
        messageRepository.saveAll(messages);
    }
}


