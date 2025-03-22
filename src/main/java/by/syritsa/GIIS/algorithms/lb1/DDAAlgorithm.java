package by.syritsa.GIIS.algorithms.lb1;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

class DDAAlgorithm {
    public static List<Pixel> generateLine(int x1, int y1, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            pixels.add(new Pixel(Math.round(x), Math.round(y), 255));
            x += xIncrement;
            y += yIncrement;
        }

        return pixels;
    }
}

