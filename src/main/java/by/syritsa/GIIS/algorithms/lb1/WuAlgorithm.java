package by.syritsa.GIIS.algorithms.lb1;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class WuAlgorithm {
    private static int ipart(float x) {
        return (int) x;
    }

    private static float fpart(float x) {
        return x - (float) Math.floor(x);
    }

    private static float rfpart(float x) {
        return 1 - fpart(x);
    }

    public static List<Pixel> generateLine(int x1, int y1, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);
        if (steep) {
            int temp;
            temp = x1;
            x1 = y1;
            y1 = temp;
            temp = x2;
            x2 = y2;
            y2 = temp;
        }

        if (x1 > x2) {
            int temp;
            temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        float dx = x2 - x1;
        float dy = y2 - y1;
        float gradient = (dx == 0) ? 1 : dy / dx;

        int xEnd = x1;
        float yEnd = y1 + gradient * (xEnd - x1);
        int xPixel1 = xEnd;
        int yPixel1 = ipart(yEnd);

        pixels.add(new Pixel(xPixel1, yPixel1, rfpart(yEnd)));
        pixels.add(new Pixel(xPixel1, yPixel1 + 1, fpart(yEnd)));

        float interY = yEnd + gradient;

        for (int x = xPixel1 + 1; x < x2; x++) {
            pixels.add(new Pixel(x, ipart(interY), rfpart(interY)));
            pixels.add(new Pixel(x, ipart(interY) + 1, fpart(interY)));
            interY += gradient;
        }

        xEnd = x2;
        yEnd = y2 + gradient * (xEnd - x2);
        int xPixel2 = xEnd;
        int yPixel2 = ipart(yEnd);

        pixels.add(new Pixel(xPixel2, yPixel2, rfpart(yEnd)));
        pixels.add(new Pixel(xPixel2, yPixel2 + 1, fpart(yEnd)));

        return pixels;
    }
}
