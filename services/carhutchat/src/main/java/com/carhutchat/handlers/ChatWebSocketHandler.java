package com.carhutchat.handlers;

import com.carhutchat.models.Message;
import com.carhutchat.services.ChatCommunicationService;
import com.carhutchat.services.ExternalAPIService;
import com.carhutchat.util.exceptions.chatcommunicationservice.ChatCommunicationServiceCannotSaveMessageException;
import com.carhutchat.util.exceptions.chatcommunicationservice.ChatCommunicationServiceException;
import com.carhutchat.util.exceptions.chatwebsockethandler.ChatWebSocketHandlerCannotParseJSONException;
import com.carhutchat.util.exceptions.chatwebsockethandler.ChatWebSocketHandlerCannotSendMessageException;
import com.carhutchat.util.exceptions.externalapiservice.ExternalAPIServiceException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ChatCommunicationService chatCommunicationService;
    @Autowired
    private ExternalAPIService externalAPIService;
    private Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String user = getUserFromSession(session);
        sessions.put(user, session);
        sendUndeliveredMessages(user);
    }

    private void sendUndeliveredMessages(String user) throws ChatCommunicationServiceException, ChatWebSocketHandlerCannotSendMessageException {
        List<Message> undeliveredMessages = null;
        try {
            undeliveredMessages = chatCommunicationService.getUndeliveredMessages(user);
        } catch (ExternalAPIServiceException | ChatCommunicationServiceException e) {
            throw new ChatCommunicationServiceException(e.getMessage());
        }

        for (Message message : undeliveredMessages) {
            WebSocketSession session = sessions.get(user);
            if (session != null && session.isOpen()) {
                sendMessageToSessionWithUser(user, new TextMessage(message.getMessage()), session);
            }
        }
        chatCommunicationService.markMessagesAsDelivered(undeliveredMessages);
    }

    private void sendMessageToSessionWithUser(String user, TextMessage message, WebSocketSession session) throws ChatWebSocketHandlerCannotSendMessageException {
        try {
            session.sendMessage(message);
        }
        catch (Exception e) {
            throw new ChatWebSocketHandlerCannotSendMessageException("[ChatWebSocketHandler][ERROR] - Cannot send message to session with user: " + user + ". Stack trace message: " + e.getMessage());
        }
    }

    private String getUserFromSession(WebSocketSession session) {
        return (String) session.getAttributes().get("username");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws ExternalAPIServiceException, ChatCommunicationServiceCannotSaveMessageException, ChatWebSocketHandlerCannotSendMessageException, ChatWebSocketHandlerCannotParseJSONException {
        String user = getUserFromSession(session);
        String userId = externalAPIService.getUserIdByName(user);
        String recipient = parseRecipientFromTextMessage(message);
        String recipientId = externalAPIService.getUserIdByName(recipient);
        String content = parseMessageFromTextMessage(message);

        WebSocketSession recipientSession = sessions.get(recipient);
        if (recipientSession != null && recipientSession.isOpen()) {
            sendMessageToSessionWithUser(user, message, recipientSession);
            chatCommunicationService.saveMessageToDatabase(userId, recipientId, content, true);
        } else {
            chatCommunicationService.saveMessageToDatabase(userId, recipientId, content, false);
        }
    }

    private String parseRecipientFromTextMessage(TextMessage message) throws ChatWebSocketHandlerCannotParseJSONException {
        try {
            JSONObject jo = new JSONObject(message.getPayload());
            return jo.getString("recipient");
        }
        catch (Exception e) {
            throw new ChatWebSocketHandlerCannotParseJSONException("[ChatWebSocketHandler][ERROR] - Cannot parse recipient from message payload. Stack trace message: " + e.getMessage());
        }
    }

    private String parseMessageFromTextMessage(TextMessage message) throws ChatWebSocketHandlerCannotParseJSONException {
        try {
            JSONObject jo = new JSONObject(message.getPayload());
            return jo.getString("content");
        }
        catch (Exception e) {
            throw new ChatWebSocketHandlerCannotParseJSONException("[ChatWebSocketHandler][ERROR] - Cannot parse content from message payload. Stack trace message: " + e.getMessage());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String user = getUserFromSession(session); // Implement this method to extract the user info from session
        sessions.remove(user);
    }

}
