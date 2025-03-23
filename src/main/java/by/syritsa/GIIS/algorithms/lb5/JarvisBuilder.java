package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class JarvisBuilder {

    public static List<Pixel> convexHull(List<Pixel> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("Для построения выпуклой оболочки нужно минимум 3 точки.");
        }

        List<Pixel> hull = new ArrayList<>();

        Pixel leftmost = points.get(0);
        for (Pixel p : points) {
            if (p.x < leftmost.x || (p.x == leftmost.x && p.y < leftmost.y)) {
                leftmost = p;
            }
        }

        Pixel current = leftmost;
        do {
            hull.add(current);
            Pixel next = points.get(0);

            for (Pixel p : points) {
                if (p == current) continue;
                int cross = orientation(current, next, p);
                if (next == current || cross == -1 || (cross == 0 && distance(current, p) > distance(current, next))) {
                    next = p;
                }
            }

            current = next;
        } while (current != leftmost);

        return hull;
    }

    private static int orientation(Pixel a, Pixel b, Pixel c) {
        int val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : -1;
    }

    private static int distance(Pixel a, Pixel b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
}