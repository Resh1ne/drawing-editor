package by.syritsa.GIIS.algorithms.lb6;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScanlineFillAlgorithm {
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.size() < 3) {
            return filledPixels;
        }

        int minY = polygon.get(0).y;
        int maxY = polygon.get(0).y;
        for (Point p : polygon) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y != p2.y) {
                Edge edge = new Edge(p1, p2);
                edges.add(edge);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (Edge edge : edges) {
                if (y >= edge.minY && y < edge.maxY) {
                    int x = (int) (edge.x + (y - edge.y1) * edge.slope);
                    intersections.add(x);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }
        }

        return filledPixels;
    }

    private static class Edge {
        int y1, y2;
        int x;
        double slope;
        int minY, maxY;

        public Edge(Point p1, Point p2) {
            if (p1.y < p2.y) {
                y1 = p1.y;
                y2 = p2.y;
                x = p1.x;
            } else {
                y1 = p2.y;
                y2 = p1.y;
                x = p2.x;
            }
            minY = y1;
            maxY = y2;
            slope = (double) (p2.x - p1.x) / (p2.y - p1.y);
        }
    }
}