package by.syritsa.GIIS.algorithms.lb6;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class FloodFillAlgorithm {

    public static List<Point> fillPolygon(List<Point> polygon, Point seed) {
        List<Point> filledPixels = new ArrayList<>();
        if (polygon.isEmpty() || seed == null) {
            return filledPixels;
        }

        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.x < xMin) xMin = p.x;
            if (p.x > xMax) xMax = p.x;
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        boolean[][] visited = new boolean[yMax - yMin + 1][xMax - xMin + 1];

        Stack<Point> stack = new Stack<>();
        stack.push(seed);

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            int x = current.x;
            int y = current.y;

            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && !visited[y - yMin][x - xMin]) {
                if (isPointInsidePolygon(polygon, current)) {
                    visited[y - yMin][x - xMin] = true;
                    filledPixels.add(new Point(x, y));

                    stack.push(new Point(x + 1, y));
                    stack.push(new Point(x - 1, y));
                    stack.push(new Point(x, y + 1));
                    stack.push(new Point(x, y - 1));
                }
            }
        }

        return filledPixels;
    }

    private static boolean isPointInsidePolygon(List<Point> polygon, Point point) {
        int intersections = 0;
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % n);

            if (p1.y == p2.y) continue;

            if (point.y > Math.min(p1.y, p2.y) && point.y <= Math.max(p1.y, p2.y)) {
                double xIntersection = (double) ((point.y - p1.y) * (p2.x - p1.x)) / (p2.y - p1.y) + p1.x;

                if (p1.x == p2.x || point.x <= xIntersection) {
                    intersections++;
                }
            }
        }

        return intersections % 2 != 0;
    }
}