package com.carhutchat.services;

import com.carhutchat.models.Message;
import com.carhutchat.models.requests.MessageRequestBody;
import com.carhutchat.repositories.MessageRepository;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceException;
import com.carhutchat.util.exceptions.messageservice.MessageServiceCannotGetChatsExceptions;
import com.carhutchat.util.exceptions.messageservice.MessageServiceCannotGetMessagesException;
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

    public List<Message> getLastTenMessagesWithUser(MessageRequestBody messageRequestBody) throws MessageServiceCannotGetMessagesException {
        try {
            return messageRepository.getLastTenMessagesWithUser(messageRequestBody.getSenderId(), messageRequestBody.getRecipientId());
        }
        catch (Exception e) {
            throw new MessageServiceCannotGetMessagesException("[MessageService][ERROR] - Cannot get last ten messages between users ids (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ") - Stack trace error: " + e.getMessage());
        }
    }

    public List<Message> getLastFiftyMessagesWithUser(MessageRequestBody messageRequestBody) throws MessageServiceCannotGetMessagesException {
        try {
            return messageRepository.getLastFiftyMessagesWithUser(messageRequestBody.getSenderId(), messageRequestBody.getRecipientId());
        }
        catch (Exception e) {
            throw new MessageServiceCannotGetMessagesException("[MessageService][ERROR] - Cannot get last ten messages between users ids (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ") - Stack trace error: " + e.getMessage());
        }
    }

    public List<Message> getAllMyChatsByDateDesc(String myUsername) throws MessageServiceCannotGetChatsExceptions, ExternalAPIServiceException {
        String myId = null;
        try {
            myId = externalAPIService.getUserIdByName(myUsername);
        } catch (ExternalAPIServiceException e) {
            throw new ExternalAPIServiceException(e.getMessage());
        }

        try {
            return messageRepository.findLastMessagesFromUniqueChats(myId);
        }
        catch (Exception e) {
            throw new MessageServiceCannotGetChatsExceptions("[MessageService][ERROR] - Cannot get all chats for user: " + myUsername + "(id=" + myId + ") - Stack trace message: " + e.getMessage());
        }
    }
}
