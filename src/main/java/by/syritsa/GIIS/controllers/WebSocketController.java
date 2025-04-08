package by.syritsa.GIIS.controllers;

import by.syritsa.GIIS.algorithms.Pixel;
import by.syritsa.GIIS.algorithms.lb1.BresenhamAlgorithm;
import by.syritsa.GIIS.algorithms.lb1.DDAAlgorithm;
import by.syritsa.GIIS.algorithms.lb1.WuAlgorithm;
import by.syritsa.GIIS.algorithms.lb2.BresenhamCircle;
import by.syritsa.GIIS.algorithms.lb2.BresenhamEllipse;
import by.syritsa.GIIS.algorithms.lb2.BresenhamHyperbola;
import by.syritsa.GIIS.algorithms.lb2.BresenhamParabola;
import by.syritsa.GIIS.algorithms.lb3.BSplineCurve;
import by.syritsa.GIIS.algorithms.lb3.BezierCurve;
import by.syritsa.GIIS.algorithms.lb3.HermiteCurve;
import by.syritsa.GIIS.algorithms.lb5.ConvexityChecker;
import by.syritsa.GIIS.algorithms.lb5.GrahamBuilder;
import by.syritsa.GIIS.algorithms.lb5.JarvisBuilder;
import by.syritsa.GIIS.algorithms.lb5.PointInPolygonChecker;
import by.syritsa.GIIS.algorithms.lb5.PolygonBuilder;
import by.syritsa.GIIS.algorithms.lb4.ThreeDTransformationService;
import by.syritsa.GIIS.algorithms.lb4.TransformationRequest;
import by.syritsa.GIIS.algorithms.lb4.TransformationResponse;
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
    private ThreeDTransformationService threeDTransformationService;

    @Autowired
    public WebSocketController(SimpMessagingTemplate messagingTemplate, ThreeDTransformationService threeDTransformationService) {
        this.messagingTemplate = messagingTemplate;
        this.threeDTransformationService = threeDTransformationService;
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

    @MessageMapping("/lab3")
    public void receiveCurvePoints(@RequestBody JsonNode jsonData) {
        List<Pixel> pixels = new ArrayList<Pixel>();
        for (JsonNode point : jsonData.get("points")) {
            int x = point.get("x").asInt();
            int y = point.get("y").asInt();
            Pixel pixel = new Pixel(x, y);
            pixels.add(pixel);
            System.out.println("Point: x=" + x + ", y=" + y);
        }

        if (jsonData.has("algorithm")) {
            if (jsonData.get("algorithm").asText().equals("BezierCurve")) {
                List<Pixel> newPixels = BezierCurve.generateBezierCurve(pixels, 100);
                messagingTemplate.convertAndSend("/topic/line5", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("HermiteCurve")) {
                List<Pixel> newPixels = HermiteCurve.drawHermiteCurve(pixels.get(0), pixels.get(1), pixels.get(2), pixels.get(3), 100);
                messagingTemplate.convertAndSend("/topic/line5", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("BSplineCurve")) {
                int degree = jsonData.get("degree").asInt();
                List<Pixel> newPixels = BSplineCurve.generateBSpline(pixels, generateKnotVector(pixels.size(), degree), degree, 100);
                messagingTemplate.convertAndSend("/topic/line5", newPixels);
            }
        }


    }

    private float[] generateKnotVector(int numControlPoints, int degree) {
        int numKnots = numControlPoints + degree + 1;
        float[] knots = new float[numKnots];

        // Равномерный узловой вектор
        for (int i = 0; i < numKnots; i++) {
            knots[i] = i;
        }

        return knots;
    }

    @MessageMapping("/lab5")
    public void receivePolygonPoints(@RequestBody JsonNode jsonData) {
        List<Pixel> pixels = new ArrayList<Pixel>();
        for (JsonNode point : jsonData.get("points")) {
            int x = point.get("x").asInt();
            int y = point.get("y").asInt();
            Pixel pixel = new Pixel(x, y);
            pixels.add(pixel);
            System.out.println("Point: x=" + x + ", y=" + y);
        }

        if (jsonData.has("algorithm")) {
            if (jsonData.get("algorithm").asText().equals("isConvex")) {
                Boolean isConvex = ConvexityChecker.isConvex(pixels);
                messagingTemplate.convertAndSend("/topic/line7", isConvex);
            } else if (jsonData.get("algorithm").asText().equals("JarvisBuilder")) {
                List<Pixel> newPixels = JarvisBuilder.convexHull(pixels);
                messagingTemplate.convertAndSend("/topic/line6", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("GrahamBuilder")) {
                List<Pixel> newPixels = GrahamBuilder.convexHull(pixels);
                messagingTemplate.convertAndSend("/topic/line6", newPixels);
            } else if (jsonData.get("algorithm").asText().equals("PointDefinition")) {
                int x = jsonData.get("x").asInt();
                int y = jsonData.get("y").asInt();
                Pixel pixel = new Pixel(x, y);
                Boolean isInPolygon = PointInPolygonChecker.isPointInPolygon(pixel,pixels);
                messagingTemplate.convertAndSend("/topic/line8", isInPolygon);
            }
        }
    }


    @MessageMapping("/transform3D")
    public void handleTransformation(TransformationRequest request) {
        TransformationResponse response = threeDTransformationService.applyTransformation(request);
        messagingTemplate.convertAndSend("/topic/drawings3d", response);
    }

}
