package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.ArrayList;
import java.util.List;

public class IntersectionFinder {

    /**
     * Находит точки пересечения отрезка со сторонами полигона.
     *
     * @param lineStart Начальная точка отрезка.
     * @param lineEnd   Конечная точка отрезка.
     * @param polygon   Список точек полигона.
     * @return Список точек пересечения.
     */
    public static List<Pixel> findIntersections(Pixel lineStart, Pixel lineEnd, List<Pixel> polygon) {
        List<Pixel> intersections = new ArrayList<>();

        for (int i = 0; i < polygon.size(); i++) {
            // Текущая и следующая точки полигона
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % polygon.size());

            // Проверяем пересечение отрезка с текущей стороной полигона
            Pixel intersection = findIntersection(lineStart, lineEnd, a, b);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }

        return intersections;
    }

    /**
     * Проверяет пересечение двух отрезков и возвращает точку пересечения.
     *
     * @param p1 Начальная точка первого отрезка.
     * @param p2 Конечная точка первого отрезка.
     * @param p3 Начальная точка второго отрезка.
     * @param p4 Конечная точка второго отрезка.
     * @return Точка пересечения или null, если отрезки не пересекаются.
     */
    private static Pixel findIntersection(Pixel p1, Pixel p2, Pixel p3, Pixel p4) {
        // Векторные произведения для проверки взаимного расположения
        int d1 = direction(p3, p4, p1);
        int d2 = direction(p3, p4, p2);
        int d3 = direction(p1, p2, p3);
        int d4 = direction(p1, p2, p4);

        // Отрезки пересекаются, если концы одного отрезка лежат по разные стороны от другого
        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
            // Вычисляем точку пересечения
            double a1 = p2.y - p1.y;
            double b1 = p1.x - p2.x;
            double c1 = a1 * p1.x + b1 * p1.y;

            double a2 = p4.y - p3.y;
            double b2 = p3.x - p4.x;
            double c2 = a2 * p3.x + b2 * p3.y;

            double determinant = a1 * b2 - a2 * b1;

            if (determinant != 0) {
                double x = (b2 * c1 - b1 * c2) / determinant;
                double y = (a1 * c2 - a2 * c1) / determinant;
                return new Pixel((int) Math.round(x), (int) Math.round(y), 1.0f); // intensity = 1.0f
            }
        }

        return null; // Отрезки не пересекаются
    }

    /**
     * Вычисляет векторное произведение для определения взаимного расположения точек.
     *
     * @param a Начальная точка отрезка.
     * @param b Конечная точка отрезка.
     * @param c Точка для проверки.
     * @return Значение векторного произведения.
     */
    private static int direction(Pixel a, Pixel b, Pixel c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }
}