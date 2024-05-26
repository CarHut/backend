package com.carhutchat.controllers;

import com.carhutchat.models.Message;
import com.carhutchat.models.requests.MessageRequestBody;
import com.carhutchat.services.MessageService;
import org.aspectj.weaver.ResolvedPointcutDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/chat")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @PostMapping("/getLastTenMessagesWithUser")
    @ResponseBody
    public ResponseEntity<List<Message>> getLastTenMessagesWithUser(@RequestBody MessageRequestBody messageRequestBody) {
        try {
            List<Message> lastMessages = messageService.getLastTenMessagesWithUser(messageRequestBody);
            if (lastMessages != null) {
                return ResponseEntity.ok(lastMessages);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getLastFiftyMessagesWithUser")
    @ResponseBody
    public ResponseEntity<List<Message>> getLastFiftyMessagesWithUser(@RequestBody MessageRequestBody messageRequestBody) {
        try {
            List<Message> lastMessages = messageService.getLastFiftyMessagesWithUser(messageRequestBody);
            if (lastMessages != null) {
                return ResponseEntity.ok(lastMessages);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getAllMyChatsByDateDesc")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMyChatsByDateDesc(@RequestParam String myUsername) {
        try {
            List<Message> messages = messageService.getAllMyChatsByDateDesc(myUsername);

            if (messages != null) {
                return ResponseEntity.ok(messages);
            } else {
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }


}
