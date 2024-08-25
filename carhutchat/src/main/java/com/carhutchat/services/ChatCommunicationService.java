package com.carhutchat.services;

import com.carhutchat.models.Message;
import com.carhutchat.paths.CarHutAPIEndpoints;
import com.carhutchat.repositories.MessageRepository;
import com.carhutchat.util.exceptions.chatcommunicationservice.ChatCommunicationServiceCannotGetMessagesException;
import com.carhutchat.util.exceptions.chatcommunicationservice.ChatCommunicationServiceCannotSaveMessageException;
import com.carhutchat.util.exceptions.chatcommunicationservice.ChatCommunicationServiceException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotFindUserIdException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotOpenHttpConnectionException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotReadResponseStreamException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceCannotSetConnectionPropertyException;
import com.carhutchat.util.exceptions.messageservice.MessageServiceCannotGetMessagesException;
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

    public void saveMessageToDatabase(String sender, String recipient, String message, boolean delivered) throws ChatCommunicationServiceCannotSaveMessageException {
        try {
            if (sender.equals(recipient)) {
                return;
            }

            Message newMessage = new Message(sender, recipient, message, delivered);
            messageRepository.save(newMessage);
        }
        catch (Exception e) {
            throw new ChatCommunicationServiceCannotSaveMessageException("[ChatCommunicationService][ERROR] - Cannot save message to repository. Stack trace message: " + e.getMessage());
        }
    }

    public List<Message> getUndeliveredMessages(String recipient) throws ExternalAPIServiceCannotReadResponseStreamException, ExternalAPIServiceCannotOpenHttpConnectionException, ExternalAPIServiceCannotSetConnectionPropertyException,  ChatCommunicationServiceCannotGetMessagesException {
        String id = externalAPIService.getUserIdByName(recipient);
        try {
            return messageRepository.getUndeliveredMessages(id);
        }
        catch (Exception e) {
            throw new ChatCommunicationServiceCannotGetMessagesException("[ChatCommunicationService][ERROR] - Cannot get undelivered messages of user: " + recipient + ". Stack trace message: " + e.getMessage());
        }
    }

    public void markMessagesAsDelivered(List<Message> messages) throws ChatCommunicationServiceCannotSaveMessageException {
        messages.forEach(message -> message.setDelivered(true));
        try {
            messageRepository.saveAll(messages);
        }
        catch (Exception e) {
            throw new ChatCommunicationServiceCannotSaveMessageException("[ChatCommunicationService][ERROR] - Cannot update messages that were delivered. Stack trace message: " + e.getMessage());
        }
    }
}


