package by.syritsa.GIIS.algorithms.lb2;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BresenhamHyperbola {
    public static List<Pixel> generateHyperbola(int xc, int yc, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int a = Math.abs(x2 - xc);
        int b = Math.abs(y2 - yc);

        int x = a, y = 0;
        int a2 = a * a, b2 = b * b;
        int fx = 2 * b2 * x, fy = 2 * a2 * y;
        float d = b2 - a2 * (1 + 2 * b);

        while (fx > fy) {
            addSymmetricPixelsWithAntialiasing(pixels, xc, yc, x, y, a, b);
            y++;
            fy += 2 * a2;
            if (d < 0) {
                d += b2 + fy;
            } else {
                x++;
                fx -= 2 * b2;
                d += b2 + fy - fx;
            }
        }

        d = b2 * (x + 0.5f) * (x + 0.5f) + a2 * (y - 1) * (y - 1) - a2 * b2;
        while (x < 10 * a) {
            addSymmetricPixelsWithAntialiasing(pixels, xc, yc, x, y, a, b);
            x++;
            fx -= 2 * b2;
            if (d > 0) {
                d += a2 - fx;
            } else {
                y++;
                fy += 2 * a2;
                d += a2 - fx + fy;
            }
        }

        return pixels;
    }

    private static void addSymmetricPixelsWithAntialiasing(List<Pixel> pixels, int xc, int yc, int x, int y, int a, int b) {
        float distance = Math.abs((x * x) / (float) (a * a) - (y * y) / (float) (b * b) - 1);
        float intensity = 1.0f - Math.min(distance, 1.0f);

        pixels.add(new Pixel(xc + x, yc + y, intensity));
        pixels.add(new Pixel(xc + x, yc - y, intensity));
        pixels.add(new Pixel(xc - x, yc + y, intensity));
        pixels.add(new Pixel(xc - x, yc - y, intensity));
    }
}