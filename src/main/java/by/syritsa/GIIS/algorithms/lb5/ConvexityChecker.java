package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.List;

public class ConvexityChecker {

    /**
     * Проверяет, является ли полигон выпуклым.
     *
     * @param polygon Список точек полигона.
     * @return true, если полигон выпуклый, иначе false.
     */
    public static boolean isConvex(List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        int n = polygon.size();
        int sign = 0; // Знак векторного произведения

        for (int i = 0; i < n; i++) {
            // Текущая, следующая и через одну точку
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % n);
            Pixel c = polygon.get((i + 2) % n);

            // Вычисляем векторное произведение
            float crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);

            // Если знак ещё не был установлен, устанавливаем его
            if (sign == 0) {
                if (crossProduct > 0) {
                    sign = 1;
                } else if (crossProduct < 0) {
                    sign = -1;
                }
            } else {
                // Если знак уже установлен, проверяем его
                if ((crossProduct > 0 && sign == -1) || (crossProduct < 0 && sign == 1)) {
                    return false; // Знак изменился, полигон невыпуклый
                }
            }
        }

        return true; // Знак не изменился, полигон выпуклый
    }
}