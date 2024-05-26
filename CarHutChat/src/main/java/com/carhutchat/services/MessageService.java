package com.carhutchat.services;

import com.carhutchat.models.Message;
import com.carhutchat.models.requests.MessageRequestBody;
import com.carhutchat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private ExternalAPIService externalAPIService;

    public List<Message> getLastTenMessagesWithUser(MessageRequestBody messageRequestBody) {
        return messageRepository.getLastTenMessagesWithUser(messageRequestBody.getSenderId(), messageRequestBody.getRecipientId());
    }

    public List<Message> getAllMyChatsByDateDesc(String myUsername) {
        String myId = null;
        try {
            myId = externalAPIService.getUserIdByName(myUsername);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return messageRepository.findLastMessagesFromUniqueChats(myId);
    }
}
