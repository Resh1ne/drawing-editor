package by.syritsa.GIIS.algorithms.lb1;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BresenhamAlgorithm {
    public static List<Pixel> generateLine(int x1, int y1, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            pixels.add(new Pixel(x1, y1, 255));

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }

        return pixels;
    }
}

