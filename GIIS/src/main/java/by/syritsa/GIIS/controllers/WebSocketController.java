package by.syritsa.GIIS.controllers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class WebSocketController {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/lab1")
    @SendTo("/topic/line")
    public void receivePoints(@RequestBody JsonNode jsonData) {
        System.out.println(jsonData.get("x").asText());
        System.out.println(jsonData.get("y").asText());
        System.out.println("WebSocket correctly work");
    }
}
