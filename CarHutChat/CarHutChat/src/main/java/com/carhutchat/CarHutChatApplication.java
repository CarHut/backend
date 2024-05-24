package com.carhutchat;

import com.carhutchat.handlers.ChatWebSocketHandler;
import com.carhutchat.repositories.MessageRepository;
import com.carhutchat.services.ChatCommunicationService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class CarHutChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarHutChatApplication.class, args);
    }

}
