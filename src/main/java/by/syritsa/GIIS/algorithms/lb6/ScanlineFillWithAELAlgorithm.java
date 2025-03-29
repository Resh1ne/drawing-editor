package by.syritsa.GIIS.algorithms.lb6;

import java.awt.*;
import java.util.List;
import java.util.*;

public class ScanlineFillWithAELAlgorithm {

    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.isEmpty()) {
            return filledPixels;
        }

        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        Map<Integer, List<Edge>> edgeTable = new HashMap<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y == p2.y) continue;

            Edge edge = new Edge(Math.max(p1.y, p2.y), p1.y < p2.y ? p1.x : p2.x, (float) (p2.x - p1.x) / (p2.y - p1.y));
            int yStart = Math.min(p1.y, p2.y);

            if (!edgeTable.containsKey(yStart)) {
                edgeTable.put(yStart, new ArrayList<>());
            }
            edgeTable.get(yStart).add(edge);
        }

        List<Edge> activeEdges = new ArrayList<>();

        for (int y = yMin; y <= yMax; y++) {
            if (edgeTable.containsKey(y)) {
                activeEdges.addAll(edgeTable.get(y));
            }

            int finalY = y;
            activeEdges.removeIf(edge -> edge.yMax <= finalY);

            activeEdges.sort(Comparator.comparing(edge -> edge.x));

            for (int i = 0; i < activeEdges.size(); i += 2) {
                int xStart = (int) Math.ceil(activeEdges.get(i).x);
                int xEnd = (int) Math.floor(activeEdges.get(i + 1).x);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }

            for (Edge edge : activeEdges) {
                edge.x += edge.slope;
            }
        }

        return filledPixels;
    }
}