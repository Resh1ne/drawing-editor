package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.List;

public class PointInPolygonChecker {
    public static boolean isPointInPolygon(Pixel point, List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        boolean inside = false;
        int n = polygon.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get(j);

            if (isPointOnSegment(a, b, point)) {
                return true;
            }

            if ((a.y > point.y) != (b.y > point.y)) {
                double intersectX = (double) ((b.x - a.x) * (point.y - a.y)) / (b.y - a.y) + a.x;
                if (point.x <= intersectX) {
                    inside = !inside;
                }
            }
        }

        return inside;
    }

    private static boolean isPointOnSegment(Pixel a, Pixel b, Pixel p) {
        int crossProduct = (p.y - a.y) * (b.x - a.x) - (p.x - a.x) * (b.y - a.y);
        if (Math.abs(crossProduct) != 0) {
            return false;
        }

        int dotProduct = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y);
        if (dotProduct < 0) {
            return false;
        }

        int squaredLength = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
        return dotProduct <= squaredLength;
    }
}