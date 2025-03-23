package by.syritsa.GIIS.algorithms.lb3;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    public static Pixel calculateBezierPoint(List<Pixel> controlPoints, float t) {
        int n = controlPoints.size() - 1;
        float x = 0, y = 0;

        for (int i = 0; i <= n; i++) {
            float coefficient = binomialCoefficient(n, i) * (float) Math.pow(1 - t, n - i) * (float) Math.pow(t, i);
            x += coefficient * controlPoints.get(i).x;
            y += coefficient * controlPoints.get(i).y;
        }

        return new Pixel((int) x, (int) y, 0);
    }

    private static int binomialCoefficient(int n, int k) {
        if (k < 0 || k > n) {
            return 0;
        }
        if (k == 0 || k == n) {
            return 1;
        }
        k = Math.min(k, n - k);
        int result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }
        return result;
    }

    public static List<Pixel> generateBezierCurve(List<Pixel> controlPoints, int numPoints) {
        List<Pixel> curve = new ArrayList<>();
        for (int i = 0; i <= numPoints; i++) {
            float t = i / (float) numPoints;
            curve.add(calculateBezierPoint(controlPoints, t));
        }
        return curve;
    }
}