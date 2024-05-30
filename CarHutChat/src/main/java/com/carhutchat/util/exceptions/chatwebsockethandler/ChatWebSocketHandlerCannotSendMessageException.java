package com.carhutchat.util.exceptions.chatwebsockethandler;

public class ChatWebSocketHandlerCannotSendMessageException extends ChatWebSocketHandlerException {
    public ChatWebSocketHandlerCannotSendMessageException(String errMessage) {
        super(errMessage);
    }
}
