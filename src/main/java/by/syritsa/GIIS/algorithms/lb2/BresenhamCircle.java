package by.syritsa.GIIS.algorithms.lb2;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BresenhamCircle {
    public static List<Pixel> generateCircle(int xc, int yc, int x2, int y2) {
        int radius = (int) Math.sqrt(Math.pow(x2 - xc, 2) + Math.pow(y2 - yc, 2));
        List<Pixel> pixels = new ArrayList<>();

        int x = 0, y = radius;
        int d = 3 - 2 * radius;

        while (x <= y) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            if (d < 0) {
                d += 4 * x + 6;
            } else {
                d += 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
        return pixels;
    }

    private static void addSymmetricPixels(List<Pixel> pixels, int xc, int yc, int x, int y) {
        pixels.add(new Pixel(xc + x, yc + y, 1.0f));
        pixels.add(new Pixel(xc - x, yc + y, 1.0f));
        pixels.add(new Pixel(xc + x, yc - y, 1.0f));
        pixels.add(new Pixel(xc - x, yc - y, 1.0f));
        pixels.add(new Pixel(xc + y, yc + x, 1.0f));
        pixels.add(new Pixel(xc - y, yc + x, 1.0f));
        pixels.add(new Pixel(xc + y, yc - x, 1.0f));
        pixels.add(new Pixel(xc - y, yc - x, 1.0f));
    }
}



