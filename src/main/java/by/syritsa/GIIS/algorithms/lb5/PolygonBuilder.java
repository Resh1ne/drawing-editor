package by.syritsa.GIIS.algorithms.lb5;

import by.syritsa.GIIS.algorithms.Pixel;

import java.util.Comparator;
import java.util.List;

public class PolygonBuilder {

    /**
     * Строит полигон на основе списка пикселей.
     *
     * @param pixels Список пикселей (точек).
     * @return Список пикселей, представляющих полигон.
     */
    public static List<Pixel> buildPolygon(List<Pixel> pixels) {
        if (pixels.size() < 3) {
            throw new IllegalArgumentException("Для построения полигона нужно минимум 3 точки.");
        }

        // Находим центр масс всех точек
        float centerX = 0, centerY = 0;
        for (Pixel pixel : pixels) {
            centerX += pixel.x;
            centerY += pixel.y;
        }
        centerX /= pixels.size();
        centerY /= pixels.size();

        // Сортируем точки по углу относительно центра масс
        float finalCenterY = centerY;
        float finalCenterX = centerX;
        pixels.sort(Comparator.comparingDouble(pixel -> Math.atan2(pixel.y - finalCenterY, pixel.x - finalCenterX)));

        return pixels; // Возвращаем отсортированный список пикселей
    }
}