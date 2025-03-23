package by.syritsa.GIIS.algorithms.lb2;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BresenhamEllipse {
    public static List<Pixel> generateEllipse(int xc, int yc, int rx, int ry) {
        rx = Math.abs(rx - xc);
        ry = Math.abs(ry - yc);
        List<Pixel> pixels = new ArrayList<>();

        int x = 0, y = ry;
        int rxSq = rx * rx;
        int rySq = ry * ry;
        int twoRxSq = 2 * rxSq;
        int twoRySq = 2 * rySq;

        int p;
        int px = 0;
        int py = twoRxSq * y;

        p = Math.round(rySq - (rxSq * ry) + (0.25f * rxSq));
        while (px < py) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            x++;
            px += twoRySq;
            if (p < 0) {
                p += rySq + px;
            } else {
                y--;
                py -= twoRxSq;
                p += rySq + px - py;
            }
        }

        p = Math.round(rySq * (x + 0.5f) * (x + 0.5f) + rxSq * (y - 1) * (y - 1) - rxSq * rySq);
        while (y >= 0) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            y--;
            py -= twoRxSq;
            if (p > 0) {
                p += rxSq - py;
            } else {
                x++;
                px += twoRySq;
                p += rxSq - py + px;
            }
        }

        return pixels;
    }

    private static void addSymmetricPixels(List<Pixel> pixels, int xc, int yc, int x, int y) {
        pixels.add(new Pixel(xc + x, yc + y, 1.0f));
        pixels.add(new Pixel(xc - x, yc + y, 1.0f));
        pixels.add(new Pixel(xc + x, yc - y, 1.0f));
        pixels.add(new Pixel(xc - x, yc - y, 1.0f));
    }
}
