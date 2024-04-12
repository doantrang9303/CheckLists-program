package com.ya3k.checklist.service.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendMessage(String message) {

        messagingTemplate.convertAndSend("/importMess", message);
    }

}
