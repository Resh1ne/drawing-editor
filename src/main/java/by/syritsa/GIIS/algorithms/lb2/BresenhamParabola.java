package by.syritsa.GIIS.algorithms.lb2;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BresenhamParabola {

    public static List<Pixel> drawParabola(int x0, int y0, int a) {
        List<Pixel> points = new ArrayList<>();
        int signA = Integer.signum(a);
        a = Math.abs(a);

        int x = 0;
        int y = 0;
        int p = 1 - 2 * a;

        while (y <= 500) {
            points.add(new Pixel(x0 + x * signA, y0 + y, 1.0f));
            points.add(new Pixel(x0 + x * signA, y0 - y, 1.0f));

            if (p < 0) {
                p += 2 * y + 3;
            } else {
                x++;
                p += 2 * y + 3 - 4 * a;
            }
            y++;
        }

        if (signA < 0) {
            x = 0;
            y = 0;
            p = 1 - 2 * a;

            while (y <= 1000) {
                points.add(new Pixel(x0 - x, y0 + y, 1.0f));
                points.add(new Pixel(x0 - x, y0 - y, 1.0f));

                if (p < 0) {
                    p += 2 * y + 3;
                } else {
                    x++;
                    p += 2 * y + 3 - 4 * a;
                }
                y++;
            }
        }

        return points;
    }
}
