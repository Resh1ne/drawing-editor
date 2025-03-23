package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class GrahamBuilder {

    /**
     * Находит точку с минимальной y-координатой (и минимальной x, если таких несколько).
     *
     * @param points Список точек.
     * @return Точка с минимальной y-координатой.
     */
    private static Pixel findMinYPoint(List<Pixel> points) {
        Pixel minYPoint = points.get(0);
        for (Pixel p : points) {
            if (p.y < minYPoint.y || (p.y == minYPoint.y && p.x < minYPoint.x)) {
                minYPoint = p;
            }
        }
        return minYPoint;
    }

    /**
     * Вычисляет полярный угол между двумя точками.
     *
     * @param p0 Опорная точка.
     * @param p1 Точка, для которой вычисляется угол.
     * @return Полярный угол в радианах.
     */
    private static double polarAngle(Pixel p0, Pixel p1) {
        return Math.atan2(p1.y - p0.y, p1.x - p0.x);
    }

    /**
     * Вычисляет квадрат расстояния между двумя точками.
     *
     * @param p1 Первая точка.
     * @param p2 Вторая точка.
     * @return Квадрат расстояния.
     */
    private static int distanceSq(Pixel p1, Pixel p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    /**
     * Определяет ориентацию тройки точек.
     *
     * @param p Первая точка.
     * @param q Вторая точка.
     * @param r Третья точка.
     * @return 0, если точки коллинеарны; 1, если по часовой стрелке; 2, если против часовой стрелки.
     */
    private static int orientation(Pixel p, Pixel q, Pixel r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    /**
     * Находит выпуклую оболочку множества точек.
     *
     * @param points Список точек.
     * @return Список точек, образующих выпуклую оболочку.
     */
    public static List<Pixel> convexHull(List<Pixel> points) {
        if (points.size() < 3) return points;

        // Находим точку с минимальной y-координатой
        Pixel minYPoint = findMinYPoint(points);

        // Сортируем точки по полярному углу относительно minYPoint
        points.sort((p1, p2) -> {
            double angle1 = polarAngle(minYPoint, p1);
            double angle2 = polarAngle(minYPoint, p2);
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Integer.compare(distanceSq(minYPoint, p1), distanceSq(minYPoint, p2));
        });

        // Стек для построения выпуклой оболочки
        Stack<Pixel> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        // Построение выпуклой оболочки
        for (int i = 2; i < points.size(); i++) {
            while (hull.size() > 1 && orientation(hull.get(hull.size() - 2), hull.peek(), points.get(i)) != 2) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }
}