package com.carhutchat.services;

import com.carhutchat.models.Message;
import com.carhutchat.models.requests.MessageRequestBody;
import com.carhutchat.repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public List<Message> getLastTenMessagesWithUser(MessageRequestBody messageRequestBody) {
        return messageRepository.getLastTenMessagesWithUser(messageRequestBody.getSenderId(), messageRequestBody.getRecipientId());
    }
}
