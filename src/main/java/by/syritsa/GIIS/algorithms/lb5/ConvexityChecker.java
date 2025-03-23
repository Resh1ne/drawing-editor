package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.List;

public class ConvexityChecker {

    public static boolean isConvex(List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        int n = polygon.size();
        int sign = 0;

        for (int i = 0; i < n; i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % n);
            Pixel c = polygon.get((i + 2) % n);

            float crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);

            if (sign == 0) {
                if (crossProduct > 0) {
                    sign = 1;
                } else if (crossProduct < 0) {
                    sign = -1;
                }
            } else {
                if ((crossProduct > 0 && sign == -1) || (crossProduct < 0 && sign == 1)) {
                    return false;
                }
            }
        }

        return true;
    }
}