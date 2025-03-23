package by.syritsa.GIIS.algorithms.lb1;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class WuAlgorithm {
    public static List<Pixel> drawLineWu(int x0, int y0, int x1, int y1) {
        List<Pixel> pixels = new ArrayList<>();

        boolean steep = Math.abs(y1 - y0) > Math.abs(x1 - x0);
        if (steep) {
            int temp = x0;
            x0 = y0;
            y0 = temp;

            temp = x1;
            x1 = y1;
            y1 = temp;
        }

        boolean reverse = x0 > x1;
        if (reverse) {
            int temp = x0;
            x0 = x1;
            x1 = temp;

            temp = y0;
            y0 = y1;
            y1 = temp;
        }

        float dx = x1 - x0;
        float dy = y1 - y0;
        float gradient = dx == 0 ? 1 : dy / dx;

        float xend = Math.round(x0);
        float yend = y0 + gradient * (xend - x0);
        float xgap = rfpart(x0 + 0.5f);
        int xpxl1 = (int) xend;
        int ypxl1 = ipart(yend);

        if (steep) {
            plot(pixels, ypxl1, xpxl1, rfpart(yend) * xgap);
            plot(pixels, ypxl1 + 1, xpxl1, fpart(yend) * xgap);
        } else {
            plot(pixels, xpxl1, ypxl1, rfpart(yend) * xgap);
            plot(pixels, xpxl1, ypxl1 + 1, fpart(yend) * xgap);
        }

        float intery = yend + gradient;

        xend = Math.round(x1);
        yend = y1 + gradient * (xend - x1);
        xgap = fpart(x1 + 0.5f);
        int xpxl2 = (int) xend;
        int ypxl2 = ipart(yend);

        if (steep) {
            plot(pixels, ypxl2, xpxl2, rfpart(yend) * xgap);
            plot(pixels, ypxl2 + 1, xpxl2, fpart(yend) * xgap);
        } else {
            plot(pixels, xpxl2, ypxl2, rfpart(yend) * xgap);
            plot(pixels, xpxl2, ypxl2 + 1, fpart(yend) * xgap);
        }

        if (steep) {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(pixels, ipart(intery), x, rfpart(intery));
                plot(pixels, ipart(intery) + 1, x, fpart(intery));
                intery += gradient;
            }
        } else {
            for (int x = xpxl1 + 1; x < xpxl2; x++) {
                plot(pixels, x, ipart(intery), rfpart(intery));
                plot(pixels, x, ipart(intery) + 1, fpart(intery));
                intery += gradient;
            }
        }

        if (reverse) {
            reverseList(pixels);
        }

        return pixels;
    }


    private static void plot(List<Pixel> pixels, int x, int y, float brightness) {
        pixels.add(new Pixel(x, y, brightness));
    }

    private static int ipart(float x) {
        return (int) x;
    }

    private static float fpart(float x) {
        return x - ipart(x);
    }

    private static float rfpart(float x) {
        return 1 - fpart(x);
    }


    private static void reverseList(List<Pixel> pixels) {
        int i = 0, j = pixels.size() - 1;
        while (i < j) {
            Pixel temp = pixels.get(i);
            pixels.set(i, pixels.get(j));
            pixels.set(j, temp);
            i++;
            j--;
        }
    }
}
