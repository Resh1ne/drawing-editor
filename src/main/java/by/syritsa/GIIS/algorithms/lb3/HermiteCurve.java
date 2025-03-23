package by.syritsa.GIIS.algorithms.lb3;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class HermiteCurve {
    public static List<Pixel> drawHermiteCurve(Pixel P0, Pixel P1, Pixel T0, Pixel T1, int numPoints) {
        List<Pixel> curve = new ArrayList<>();

        for (int i = 0; i <= numPoints; i++) {
            float t = (float) i / numPoints;

            float h00 = 2 * t * t * t - 3 * t * t + 1;
            float h10 = t * t * t - 2 * t * t + t;
            float h01 = -2 * t * t * t + 3 * t * t;
            float h11 = t * t * t - t * t;

            int x = Math.round(P0.x * h00 + T0.x * h10 + P1.x * h01 + T1.x * h11);
            int y = Math.round(P0.y * h00 + T0.y * h10 + P1.y * h01 + T1.y * h11);

            curve.add(new Pixel(x, y, 1.0f));
        }

        return curve;
    }
}
