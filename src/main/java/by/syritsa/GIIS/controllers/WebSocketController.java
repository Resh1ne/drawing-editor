package by.syritsa.GIIS.controllers;

import by.syritsa.GIIS.algorithms.Pixel;
import by.syritsa.GIIS.algorithms.lb1.BresenhamAlgorithm;
import by.syritsa.GIIS.algorithms.lb1.DDAAlgorithm;
import by.syritsa.GIIS.algorithms.lb1.WuAlgorithm;
import by.syritsa.GIIS.algorithms.lb2.BresenhamCircle;
import by.syritsa.GIIS.algorithms.lb2.BresenhamEllipse;
import by.syritsa.GIIS.algorithms.lb2.BresenhamHyperbola;
import by.syritsa.GIIS.algorithms.lb2.BresenhamParabola;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class WebSocketController {

    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/lab1")
    public void receivePoints(@RequestBody JsonNode jsonData) {
        List<Pixel> pixels = new ArrayList<Pixel>();
        for (JsonNode point : jsonData.get("points")) {
            int x = point.get("x").asInt();
            int y = point.get("y").asInt();
            Pixel pixel = new Pixel(x, y);
            pixels.add(pixel);
            System.out.println("Point: x=" + x + ", y=" + y);
        }

        if (jsonData.has("lineAlgorithm")) {
            if (jsonData.get("lineAlgorithm").asText().equals("Bresenham")) {
                List<Pixel> newPixels = BresenhamAlgorithm.generateLine(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line1", newPixels);
            } else if (jsonData.get("lineAlgorithm").asText().equals("WU")) {
                List<Pixel> newPixels = WuAlgorithm.generateLine(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line2", newPixels);
            } else {
                List<Pixel> newPixels = DDAAlgorithm.generateLine(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line1", newPixels);
            }
        }
    }

    @MessageMapping("/lab2")
    public void receiveCirclePoints(@RequestBody JsonNode jsonData) {
        List<Pixel> pixels = new ArrayList<Pixel>();
        for (JsonNode point : jsonData.get("points")) {
            int x = point.get("x").asInt();
            int y = point.get("y").asInt();
            Pixel pixel = new Pixel(x, y);
            pixels.add(pixel);
            System.out.println("Point: x=" + x + ", y=" + y);
        }

        if (jsonData.has("algorithm")) {
            if (jsonData.get("algorithm").asText().equals("BresenhamCircle")) {
                List<Pixel> newPixels = BresenhamCircle.generateCircle(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line1", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("BresenhamEllipse")) {
                List<Pixel> newPixels = BresenhamEllipse.generateEllipse(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line1", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("BresenhamHyperbola")) {
                List<Pixel> newPixels = BresenhamHyperbola.generateHyperbola(pixels.get(0).x, pixels.get(0).y, pixels.get(1).x, pixels.get(1).y);
                messagingTemplate.convertAndSend("/topic/line2", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("BresenhamParabola")) {
                int parabolaPar = jsonData.get("parabolaPar").asInt();
                List<Pixel> newPixels = BresenhamParabola.drawParabola(pixels.get(0).x, pixels.get(0).y, parabolaPar);
                messagingTemplate.convertAndSend("/topic/line1", newPixels);
            }
        }
    }

}
