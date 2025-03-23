package by.syritsa.GIIS.algorithms.lb3;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class BSplineCurve {
    public static Pixel calculateBSplinePoint(List<Pixel> controlPoints, float[] knots, float t, int degree) {
        int n = controlPoints.size() - 1;
        int[] span = findSpan(n, degree, t, knots);
        float[] basis = calculateBasisFunctions(span[0], t, degree, knots);

        float x = 0, y = 0;
        for (int i = 0; i <= degree; i++) {
            x += basis[i] * controlPoints.get(span[0] - degree + i).x;
            y += basis[i] * controlPoints.get(span[0] - degree + i).y;
        }

        return new Pixel((int) x, (int) y, 0);
    }

    private static int[] findSpan(int n, int degree, float t, float[] knots) {
        if (t >= knots[n + 1]) {
            return new int[]{n};
        }
        if (t <= knots[degree]) {
            return new int[]{degree};
        }

        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;

        while (t < knots[mid] || t >= knots[mid + 1]) {
            if (t < knots[mid]) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }

        return new int[]{mid};
    }

    private static float[] calculateBasisFunctions(int span, float t, int degree, float[] knots) {
        float[] basis = new float[degree + 1];
        float[] left = new float[degree + 1];
        float[] right = new float[degree + 1];

        basis[0] = 1.0f;

        for (int j = 1; j <= degree; j++) {
            left[j] = t - knots[span + 1 - j];
            right[j] = knots[span + j] - t;

            float saved = 0.0f;

            for (int r = 0; r < j; r++) {
                float temp = basis[r] / (right[r + 1] + left[j - r]);
                basis[r] = saved + right[r + 1] * temp;
                saved = left[j - r] * temp;
            }

            basis[j] = saved;
        }

        return basis;
    }

    public static List<Pixel> generateBSpline(List<Pixel> controlPoints, float[] knots, int degree, int numPoints) {
        List<Pixel> curve = new ArrayList<>();
        float tMin = knots[degree];
        float tMax = knots[knots.length - degree - 1];

        for (int i = 0; i <= numPoints; i++) {
            float t = tMin + (tMax - tMin) * i / numPoints;
            curve.add(calculateBSplinePoint(controlPoints, knots, t, degree));
        }

        return curve;
    }
}