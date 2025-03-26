# Графический интерфейс интеллектуальных систем
<details>
  <summary>Лабораторная работа №1</summary>

## Цель
Разработать элементарный графический редактор, реализующий построение отрезков с помощью алгоритма ЦДА, целочисленного алгоритма Брезенхема и алгоритма Ву. Вызов способа генерации отрезка задается из пункта меню и доступно через панель инструментов «Отрезки». В редакторе кроме режима генерации отрезков в пользовательском окне должен быть предусмотрен отладочный режим, где отображается пошаговое решение на дискретной сетке.
## Описание алгоритмов
### Цифровой Дифференциальный Анализатор
  Цифровой дифференциальный анализатор (ЦДА) – это алгоритм, основанный на аппроксимации прямой линии путем равномерного приращения координат. Он разбивает отрезок на равные шаги по одной из координат и вычисляет соответствующие значения другой координаты.
### Алгоритм Брезенхема
  Алгоритм Брезенхема основан на выборе оптимального пикселя с использованием целочисленных вычислений. В отличие от ЦДА, он исключает необходимость работы с дробными числами, используя пошаговое накопление ошибки, чтобы принять решение о том, какой пиксель закрасить на следующем шаге.
### Алгоритм Ву
Алгоритм Ву предназначен для построения сглаженных (антиалиасинговых) линий. В отличие от Брезенхема, который выбирает один пиксель на каждом шаге, Ву использует два соседних пикселя, назначая им разные уровни яркости, чтобы сгладить границы линии и уменьшить эффект "ступенек" (aliasing).
## Интерфейс
![image](https://github.com/user-attachments/assets/6c7a4c50-0ff6-4630-a76a-6684d665c26f)
## Реализация
### Цифровой Дифференциальный Анализатор
```java
  
    public static List<Pixel> generateLine(int x1, int y1, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int dx = x2 - x1;
        int dy = y2 - y1;
        int steps = Math.max(Math.abs(dx), Math.abs(dy));

        float xIncrement = (float) dx / steps;
        float yIncrement = (float) dy / steps;

        float x = x1;
        float y = y1;

        for (int i = 0; i <= steps; i++) {
            pixels.add(new Pixel(Math.round(x), Math.round(y), 255));
            x += xIncrement;
            y += yIncrement;
        }

        return pixels;
    }   
### Алгоритм Брезенхема
```java
  
    public static List<Pixel> generateLine(int x1, int y1, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = (x1 < x2) ? 1 : -1;
        int sy = (y1 < y2) ? 1 : -1;
        int err = dx - dy;

        while (true) {
            pixels.add(new Pixel(x1, y1, 255));

            if (x1 == x2 && y1 == y2) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
            }
        }

        return pixels;
    }
```
### Алгоритм Ву
```java
public static List<Pixel> generateLine(int x0, int y0, int x1, int y1) {
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
```
## Вывод
В результате реализации графического редактора, использующего алгоритмы построения отрезков (ЦДА, Брезенхема и Ву), была создана система, обеспечивающая интерактивное рисование отрезков с возможностью отображения пошагового процесса.
</details>
