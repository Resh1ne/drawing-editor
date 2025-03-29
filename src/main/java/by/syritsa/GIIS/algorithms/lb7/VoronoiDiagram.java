package by.syritsa.GIIS.algorithms.lb7;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.min;
import static java.lang.Math.sqrt;

public class VoronoiDiagram {
    public List<LineSegment> getVoronoiEdges(List<Triangle> triangles, Rectangle boundingBox) {
        Map<Edge, List<Triangle>> edgeTriangleMap = new HashMap<>();
        for (Triangle t : triangles) {
            for (Edge edge : t.getEdges()) {
                edgeTriangleMap.computeIfAbsent(edge, k -> new ArrayList<>()).add(t);
            }
        }

        List<LineSegment> voronoiEdges = new ArrayList<>();

        for (Map.Entry<Edge, List<Triangle>> entry : edgeTriangleMap.entrySet()) {
            List<Triangle> adjacentTriangles = entry.getValue();
            if (adjacentTriangles.size() == 2) {
                Pixel cc1 = adjacentTriangles.get(0).getCircumcenter();
                Pixel cc2 = adjacentTriangles.get(1).getCircumcenter();
                voronoiEdges.add(new LineSegment(cc1, cc2));
            } else if (adjacentTriangles.size() == 1) {
                Triangle t = adjacentTriangles.get(0);
                Pixel cc = t.getCircumcenter();

                Pixel p1 = entry.getKey().getA();
                Pixel p2 = entry.getKey().getB();

                double ex = p2.getX() - p1.getX();
                double ey = p2.getY() - p1.getY();

                double cand1X = -ey;
                double cand2Y = -ex;

                Pixel p3 = t.getThirdVertex(entry.getKey());
                double dot1 = cand1X * (p3.getX() - cc.getX()) + ex * (p3.getY() - cc.getY());
                double dot2 = ey * (p3.getX() - cc.getX()) + cand2Y * (p3.getY() - cc.getY());
                double chosenDx, chosenDy;
                if (dot1 < dot2) {
                    chosenDx = cand1X;
                    chosenDy = ex;
                } else {
                    chosenDx = ey;
                    chosenDy = cand2Y;
                }
                double len = sqrt(chosenDx * chosenDx + chosenDy * chosenDy);
                if (len != 0) {
                    chosenDx /= len;
                    chosenDy /= len;
                }
                Pixel ccExtended = intersectRayWithRectangle(cc, chosenDx, chosenDy, boundingBox);
                voronoiEdges.add(new LineSegment(cc, ccExtended));
            }
        }
        return voronoiEdges;
    }

    private Pixel intersectRayWithRectangle(Pixel origin, double dx, double dy, Rectangle rect) {
        double xMin = rect.getX();
        double yMin = rect.getY();
        double xMax = rect.getX() + rect.getWidth();
        double yMax = rect.getY() + rect.getHeight();
        double tMin = Double.MAX_VALUE;

        if (dx != 0) {
            double t1 = (xMin - origin.getX()) / dx;
            double t2 = (xMax - origin.getX()) / dx;
            if (t1 > 0) tMin = min(tMin, t1);
            if (t2 > 0) tMin = min(tMin, t2);
        }
        if (dy != 0) {
            double t3 = (yMin - origin.getY()) / dy;
            double t4 = (yMax - origin.getY()) / dy;
            if (t3 > 0) tMin = min(tMin, t3);
            if (t4 > 0) tMin = min(tMin, t4);
        }
        if (tMin == Double.MAX_VALUE) {
            return origin;
        }
        int ix = (int) Math.round(origin.getX() + dx * tMin);
        int iy = (int) Math.round(origin.getY() + dy * tMin);
        return new Pixel(ix, iy);
    }
}