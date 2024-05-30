package com.carhutchat.controllers;

import com.carhutchat.models.Message;
import com.carhutchat.models.requests.MessageRequestBody;
import com.carhutchat.services.MessageService;
import com.carhutchat.util.loggers.ControllerLogger;
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
    private final ControllerLogger controllerLogger = ControllerLogger.getControllerLogger();

    @PostMapping("/getLastTenMessagesWithUser")
    @ResponseBody
    public ResponseEntity<List<Message>> getLastTenMessagesWithUser(@RequestBody MessageRequestBody messageRequestBody) {
        try {
            List<Message> lastMessages = messageService.getLastTenMessagesWithUser(messageRequestBody);
            if (lastMessages != null) {
                controllerLogger.saveToFile("[MessageController][OK]: /getLastTenMessagesWithUser - successfully fetched last ten messages. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ")");
                return ResponseEntity.ok(lastMessages);
            } else {

                controllerLogger.saveToFile("[MessageController][WARN]: /getLastTenMessagesWithUser - cannot fetch last ten messages. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ")");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[MessageController][ERROR]: /getLastTenMessagesWithUser - something went wrong internally. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ") - Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @PostMapping("/getLastFiftyMessagesWithUser")
    @ResponseBody
    public ResponseEntity<List<Message>> getLastFiftyMessagesWithUser(@RequestBody MessageRequestBody messageRequestBody) {
        try {
            List<Message> lastMessages = messageService.getLastFiftyMessagesWithUser(messageRequestBody);
            if (lastMessages != null) {
                controllerLogger.saveToFile("[MessageController][OK]: /getLastFiftyMessagesWithUser - successfully fetched last fifty messages. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ")");
                return ResponseEntity.ok(lastMessages);
            } else {
                controllerLogger.saveToFile("[MessageController][WARN]: /getLastFiftyMessagesWithUser - cannot fetch last fifty messages. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ")");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[MessageController][ERROR]: /getLastFiftyMessagesWithUser - something went wrong internally. (sender=" + messageRequestBody.getSenderId() + ", recipient=" + messageRequestBody.getRecipientId() + ") - Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/getAllMyChatsByDateDesc")
    @ResponseBody
    public ResponseEntity<List<Message>> getAllMyChatsByDateDesc(@RequestParam String myUsername) {
        try {
            List<Message> messages = messageService.getAllMyChatsByDateDesc(myUsername);

            if (messages != null) {
                controllerLogger.saveToFile("[MessageController][OK]: /getAllMyChatsByDateDesc - successfully fetched chats by date (descending). (username=" + myUsername + ")");
                return ResponseEntity.ok(messages);
            } else {
                controllerLogger.saveToFile("[MessageController][WARN]: /getAllMyChatsByDateDesc - cannot fetch chats. (username=" + myUsername + ")");
                return ResponseEntity.status(404).body(null);
            }
        }
        catch (Exception e) {
            controllerLogger.saveToFile("[MessageController][ERROR]: /getAllMyChatsByDateDesc - something went wrong internally. (username=" + myUsername + ") - Message: " + e.getMessage());
            return ResponseEntity.internalServerError().body(null);
        }
    }


}
