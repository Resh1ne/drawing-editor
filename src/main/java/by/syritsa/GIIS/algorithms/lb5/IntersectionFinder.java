package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class IntersectionFinder {

    public static List<Pixel> findIntersections(Pixel lineStart, Pixel lineEnd, List<Pixel> polygon) {
        List<Pixel> intersections = new ArrayList<>();

        for (int i = 0; i < polygon.size(); i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % polygon.size());

            Pixel intersection = findIntersection(lineStart, lineEnd, a, b);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }

        return intersections;
    }

    private static Pixel findIntersection(Pixel p1, Pixel p2, Pixel p3, Pixel p4) {
        int d1 = direction(p3, p4, p1);
        int d2 = direction(p3, p4, p2);
        int d3 = direction(p1, p2, p3);
        int d4 = direction(p1, p2, p4);

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            double a1 = p2.y - p1.y;
            double b1 = p1.x - p2.x;
            double c1 = a1 * p1.x + b1 * p1.y;

            double a2 = p4.y - p3.y;
            double b2 = p3.x - p4.x;
            double c2 = a2 * p3.x + b2 * p3.y;

            double determinant = a1 * b2 - a2 * b1;

            if (determinant != 0) {
                double x = (b2 * c1 - b1 * c2) / determinant;
                double y = (a1 * c2 - a2 * c1) / determinant;
                return new Pixel((int) Math.round(x), (int) Math.round(y), 1.0f);
            }
        }

        return null;
    }

    private static int direction(Pixel a, Pixel b, Pixel c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }
}