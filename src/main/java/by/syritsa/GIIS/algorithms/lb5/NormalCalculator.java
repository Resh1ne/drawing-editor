package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class NormalCalculator {

    public static List<Pixel> calculateInnerNormals(List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        List<Pixel> normals = new ArrayList<>();
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % n);

            int dx = b.x - a.x;
            int dy = b.y - a.y;

            int nx = -dy;
            int ny = dx;

            Pixel c = polygon.get((i + 2) % n);
            int crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);

            if (crossProduct < 0) {
                nx = -nx;
                ny = -ny;
            }

            double length = Math.sqrt(nx * nx + ny * ny);
            if (length > 0) {
                nx = (int) (nx / length * 100);
                ny = (int) (ny / length * 100);
            }

            normals.add(new Pixel(nx, ny, 0));
        }

        return normals;
    }
}