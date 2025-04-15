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
```   
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

<details>
  <summary>Лабораторная работа №2</summary>
  
## Цель
Разработать элементарный графический редактор, реализующий построение линий второго порядка. Вызов способа генерации линии второго порядка задается из пункта меню и доступно через панель инструментов «Линии 2-го порядка». В редакторе кроме режима генерации линий второго порядка в пользовательском окне должен быть предусмотрен отладочный режим, где отображается пошаговое решение на дискретной сетке.
## Описание алгоритмов
### Алгоритм для окружности
Алгоритм Брезенхэма для окружности основан на построении пикселей по восьмисимметрии. Вместо вычисления уравнения окружности, он использует целочисленные вычисления и пошаговое принятие решений.
### Алгоритм для элипса
Алгоритм Брезенхэма для эллипса — это целочисленный алгоритм растеризации эллипса. Он использует инкрементальный метод и основан на уравнении эллипса. Так как эллипс симметричен относительно обеих осей, достаточно вычислить точки только в одной четверти, а затем отразить их по симметрии.
### Алгоритм для гиперболы
Этот алгоритм реализует метод Брезенхема для рисования гиперболы. Он основан на пошаговом приближении гиперболы за счет целочисленных вычислений, что делает его быстрым и эффективным.
### Алгоритм для параболы
Этот алгоритм реализует метод Брезенхема для отрисовки параболы. Он использует дискретные (целочисленные) вычисления, что делает его быстрым и эффективным для растровой графики.
## Интерфейс
![image](https://github.com/user-attachments/assets/1bbc462a-8cad-4b44-84b0-3bbca60f7f4c)


## Реализация
### Алгоритм окружности
```java
public static List<Pixel> generateCircle(int xc, int yc, int x2, int y2) {
        int radius = (int) Math.sqrt(Math.pow(x2 - xc, 2) + Math.pow(y2 - yc, 2));
        List<Pixel> pixels = new ArrayList<>();

        int x = 0, y = radius;
        int d = 3 - 2 * radius;

        while (x <= y) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            if (d < 0) {
                d += 4 * x + 6;
            } else {
                d += 4 * (x - y) + 10;
                y--;
            }
            x++;
        }
        return pixels;
    }
```
### Алгоритм элипса
```java
public static List<Pixel> generateEllipse(int xc, int yc, int rx, int ry) {
        rx = Math.abs(rx - xc);
        ry = Math.abs(ry - yc);
        List<Pixel> pixels = new ArrayList<>();

        int x = 0, y = ry;
        int rxSq = rx * rx;
        int rySq = ry * ry;
        int twoRxSq = 2 * rxSq;
        int twoRySq = 2 * rySq;

        int p;
        int px = 0;
        int py = twoRxSq * y;

        p = Math.round(rySq - (rxSq * ry) + (0.25f * rxSq));
        while (px < py) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            x++;
            px += twoRySq;
            if (p < 0) {
                p += rySq + px;
            } else {
                y--;
                py -= twoRxSq;
                p += rySq + px - py;
            }
        }

        p = Math.round(rySq * (x + 0.5f) * (x + 0.5f) + rxSq * (y - 1) * (y - 1) - rxSq * rySq);
        while (y >= 0) {
            addSymmetricPixels(pixels, xc, yc, x, y);
            y--;
            py -= twoRxSq;
            if (p > 0) {
                p += rxSq - py;
            } else {
                x++;
                px += twoRySq;
                p += rxSq - py + px;
            }
        }

        return pixels;
    }
```
### Алгоритм гиперболы
```java
public static List<Pixel> generateHyperbola(int xc, int yc, int x2, int y2) {
        List<Pixel> pixels = new ArrayList<>();

        int a = Math.abs(x2 - xc);
        int b = Math.abs(y2 - yc);

        int x = a, y = 0;
        int a2 = a * a, b2 = b * b;
        int fx = 2 * b2 * x, fy = 2 * a2 * y;
        float d = b2 - a2 * (1 + 2 * b);

        while (fx > fy) {
            addSymmetricPixelsWithAntialiasing(pixels, xc, yc, x, y, a, b);
            y++;
            fy += 2 * a2;
            if (d < 0) {
                d += b2 + fy;
            } else {
                x++;
                fx -= 2 * b2;
                d += b2 + fy - fx;
            }
        }

        d = b2 * (x + 0.5f) * (x + 0.5f) + a2 * (y - 1) * (y - 1) - a2 * b2;
        while (x < 10 * a) {
            addSymmetricPixelsWithAntialiasing(pixels, xc, yc, x, y, a, b);
            x++;
            fx -= 2 * b2;
            if (d > 0) {
                d += a2 - fx;
            } else {
                y++;
                fy += 2 * a2;
                d += a2 - fx + fy;
            }
        }

        return pixels;
    }
```
### Алгоритм параболы
```java
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
```
## Вывод
В результате разработки графического редактора, были добавлены возможности отрисовки таких объектов как: **окружность**, **элипс**, **парабола** и **гипербола**.
</details>

<details>
  <summary>Лабораторная работа №3</summary>

## Цель
Разработать элементарный графический редактор, реализующий построение параметрических кривых, используя форму Эрмита, форму Безье и B-сплайн.
## Описание алгоритмов
### Кривая Эрмита 
Метод построения кривых, использующий начальные и конечные точки, а также касательные в этих точках.
### Кривая Безье 
Параметрическая кривая, определяемая опорными точками, с использованием полиномиальных функций.
### B-сплайн
Гибкий метод построения кривых, который позволяет более плавно контролировать форму кривой за счет весовых коэффициентов.
## Интерфейс
![image](https://github.com/user-attachments/assets/99e4c066-85c3-43b8-9418-49ab0eb62047)

## Реализация
### Кривая Эрмита
```java
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
```
### Кривая Безье
```java
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
```
### B-сплайн
```java
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
```
## Вывод
Разработанный графический редактор успешно реализует построение параметрических кривых Эрмита, Безье и B-сплайнов. Добавлена возможность корректировки опорных точек и состыковки сегментов. Реализованы базовые функции матричных вычислений для работы с кривыми.
</details>

<details>
  <summary>Лабораторная работа №4</summary>

## Цель
Разработать графическую программу, выполняющую следующие геометрические преобразования над трехмерным объектом: перемещение, поворот, скалирование, отображение, перспектива. В программе должно быть предусмотрено считывание координат 3D объекта из текстового файла, обработка клавиатуры и выполнение геометрических преобразований в зависимости от нажатых клавиш. Все преобразования следует производить с использованием матричного аппарата и представления координат в однородных координатах.
## Описание алгоритмов
### Аффинные преобразования
Это класс геометрических преобразований в пространстве, которые сохраняют прямые линии и параллельность. Они широко используются в компьютерной графике, машинном обучении, физике и других областях для изменения положения, размера, ориентации и формы объектов.
### Однородные координаты
Это система координат, которая позволяет выполнять преобразования, такие как перемещение, поворот и масштабирование, с использованием матриц.
### Матрицы преобразования
Это инструменты, используемые для осуществления различных геометрических изменений объектов в пространстве.
## Интерфейс
![image](https://github.com/user-attachments/assets/f0ae2920-e053-4c5c-9b15-3894b83dc317)

## Вывод
В процессе выполнения лабораторной работы были изучены основные методы графической визуализации и трансформации трехмерных объектов. Практическая реализация графического редактора на основе матричных преобразований предоставила ценные навыки в области компьютерной графики и геометрии. Основное внимание было уделено использованию однородных координат и их преобразованию, что является ключевым для работы с трехмерной графикой.
</details>

<details>
  <summary>Лабораторная работа №5</summary>

## Цель
Разработать элементарный графический редактор, реализующий построение полигонов. Реализованная программа должна уметь проверять полигон на выпуклость, находить его внутренние нормали. Программа должна выполнять построение выпуклых оболочек методом обхода Грэхема и методом Джарвиса. Выбор метода задается из пункта меню и должен быть доступен через панель инструментов «Построение полигонов». Графический редактор должен позволять рисовать линии первого порядка (лабораторная работа №1) и определять точки пересечения отрезка со стороной полигона, также программа должна определять принадлежность введенной точки полигону.
## Описание алгоритмов
Для проверки полигона на выпуклость используется алгоритм, основанный на определении направления поворота для каждой тройки последовательных вершин полигона. Если все тройки вершин имеют одинаковое направление поворота, то полигон является выпуклым.
### Метод обхода Грэхема
Алгоритм, который строит выпуклую оболочку, обходя точки в порядке увеличения угла относительно начальной точки.
### Метод Джарвиса
Алгоритм, который строит выпуклую оболочку, последовательно находя точки с наименьшим углом относительно предыдущей точки.
## Интерфейс
![image](https://github.com/user-attachments/assets/05119966-68cb-45f7-a21f-afa647cb7d1e)

## Реализация
### Метод обхода Грэхема
```java
    private static Pixel findMinYPoint(List<Pixel> points) {
        Pixel minYPoint = points.get(0);
        for (Pixel p : points) {
            if (p.y < minYPoint.y || (p.y == minYPoint.y && p.x < minYPoint.x)) {
                minYPoint = p;
            }
        }
        return minYPoint;
    }

    private static double polarAngle(Pixel p0, Pixel p1) {
        return Math.atan2(p1.y - p0.y, p1.x - p0.x);
    }

    private static int distanceSq(Pixel p1, Pixel p2) {
        return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
    }

    private static int orientation(Pixel p, Pixel q, Pixel r) {
        int val = (q.y - p.y) * (r.x - q.x) - (q.x - p.x) * (r.y - q.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : 2;
    }

    public static List<Pixel> convexHull(List<Pixel> points) {
        if (points.size() < 3) return points;

        Pixel minYPoint = findMinYPoint(points);

        points.sort((p1, p2) -> {
            double angle1 = polarAngle(minYPoint, p1);
            double angle2 = polarAngle(minYPoint, p2);
            if (angle1 < angle2) return -1;
            if (angle1 > angle2) return 1;
            return Integer.compare(distanceSq(minYPoint, p1), distanceSq(minYPoint, p2));
        });

        Stack<Pixel> hull = new Stack<>();
        hull.push(points.get(0));
        hull.push(points.get(1));

        for (int i = 2; i < points.size(); i++) {
            while (hull.size() > 1 && orientation(hull.get(hull.size() - 2), hull.peek(), points.get(i)) != 2) {
                hull.pop();
            }
            hull.push(points.get(i));
        }

        return new ArrayList<>(hull);
    }
```   
### Метод Джарвиса
```java
public static List<Pixel> convexHull(List<Pixel> points) {
        if (points.size() < 3) {
            throw new IllegalArgumentException("Для построения выпуклой оболочки нужно минимум 3 точки.");
        }

        List<Pixel> hull = new ArrayList<>();

        Pixel leftmost = points.get(0);
        for (Pixel p : points) {
            if (p.x < leftmost.x || (p.x == leftmost.x && p.y < leftmost.y)) {
                leftmost = p;
            }
        }

        Pixel current = leftmost;
        do {
            hull.add(current);
            Pixel next = points.get(0);

            for (Pixel p : points) {
                if (p == current) continue;
                int cross = orientation(current, next, p);
                if (next == current || cross == -1 || (cross == 0 && distance(current, p) > distance(current, next))) {
                    next = p;
                }
            }

            current = next;
        } while (current != leftmost);

        return hull;
    }

    private static int orientation(Pixel a, Pixel b, Pixel c) {
        int val = (b.y - a.y) * (c.x - b.x) - (b.x - a.x) * (c.y - b.y);
        if (val == 0) return 0;
        return (val > 0) ? 1 : -1;
    }

    private static int distance(Pixel a, Pixel b) {
        return (a.x - b.x) * (a.x - b.x) + (a.y - b.y) * (a.y - b.y);
    }
```
### Проверка выпуклости
```java
public static boolean isConvex(List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        int n = polygon.size();
        int sign = 0;

        for (int i = 0; i < n; i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % n);
            Pixel c = polygon.get((i + 2) % n);

            float crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);

            if (sign == 0) {
                if (crossProduct > 0) {
                    sign = 1;
                } else if (crossProduct < 0) {
                    sign = -1;
                }
            } else {
                if ((crossProduct > 0 && sign == -1) || (crossProduct < 0 && sign == 1)) {
                    return false;
                }
            }
        }

        return true;
    }
```
### Проверка пересечений
```java
public static List<Pixel> findIntersections(Pixel lineStart, Pixel lineEnd, List<Pixel> polygon) {
        List<Pixel> intersections = new ArrayList<>();

        for (int i = 0; i < polygon.size(); i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % polygon.size());

            Pixel intersection = findIntersection(lineStart, lineEnd, a, b);
            if (intersection != null) {
                intersections.add(intersection);
            }
        }

        return intersections;
    }

    private static Pixel findIntersection(Pixel p1, Pixel p2, Pixel p3, Pixel p4) {
        int d1 = direction(p3, p4, p1);
        int d2 = direction(p3, p4, p2);
        int d3 = direction(p1, p2, p3);
        int d4 = direction(p1, p2, p4);

        if (((d1 > 0 && d2 < 0) || (d1 < 0 && d2 > 0)) && ((d3 > 0 && d4 < 0) || (d3 < 0 && d4 > 0))) {
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
                return new Pixel((int) Math.round(x), (int) Math.round(y), 1.0f);
            }
        }

        return null;
    }

    private static int direction(Pixel a, Pixel b, Pixel c) {
        return (b.x - a.x) * (c.y - a.y) - (b.y - a.y) * (c.x - a.x);
    }
```
### Проверка нормалей
```java
public static List<Pixel> calculateInnerNormals(List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        List<Pixel> normals = new ArrayList<>();
        int n = polygon.size();

        for (int i = 0; i < n; i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get((i + 1) % n);

            int dx = b.x - a.x;
            int dy = b.y - a.y;

            int nx = -dy;
            int ny = dx;

            Pixel c = polygon.get((i + 2) % n);
            int crossProduct = (b.x - a.x) * (c.y - b.y) - (b.y - a.y) * (c.x - b.x);

            if (crossProduct < 0) {
                nx = -nx;
                ny = -ny;
            }

            double length = Math.sqrt(nx * nx + ny * ny);
            if (length > 0) {
                nx = (int) (nx / length * 100);
                ny = (int) (ny / length * 100);
            }

            normals.add(new Pixel(nx, ny, 0));
        }

        return normals;
    }
```
### Проверка точек
```java
 public static boolean isPointInPolygon(Pixel point, List<Pixel> polygon) {
        if (polygon.size() < 3) {
            throw new IllegalArgumentException("Полигон должен содержать минимум 3 точки.");
        }

        boolean inside = false;
        int n = polygon.size();

        for (int i = 0, j = n - 1; i < n; j = i++) {
            Pixel a = polygon.get(i);
            Pixel b = polygon.get(j);

            if (isPointOnSegment(a, b, point)) {
                return true;
            }

            if ((a.y > point.y) != (b.y > point.y)) {
                double intersectX = (double) ((b.x - a.x) * (point.y - a.y)) / (b.y - a.y) + a.x;
                if (point.x <= intersectX) {
                    inside = !inside;
                }
            }
        }

        return inside;
    }

    private static boolean isPointOnSegment(Pixel a, Pixel b, Pixel p) {
        int crossProduct = (p.y - a.y) * (b.x - a.x) - (p.x - a.x) * (b.y - a.y);
        if (Math.abs(crossProduct) != 0) {
            return false;
        }

        int dotProduct = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y);
        if (dotProduct < 0) {
            return false;
        }

        int squaredLength = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y);
        return dotProduct <= squaredLength;
    }
```

## Вывод
В ходе работы были реализованы алгоритмы Джарвиса и Грэхэма для построения выпуклой оболочки, а также алгоритм поиска пересечения полигона с прямой. Программа предоставляет графический интерфейс для визуализации работы алгоритмов и взаимодействия с пользователем.
</details>

<details>
  <summary>Лабораторная работа №6</summary>
  
## Цель
Целью данной лабораторной работы является разработка элементарного графического редактора, который позволяет выполнять построение полигонов и их заполнение с использованием различных алгоритмов растровой развертки и заполнения с затравкой. Программа должна поддерживать режим отладки для визуализации пошагового выполнения алгоритмов.
## Алгоритмы
### Алгоритм растровой развертки с упорядоченным списком рёбер
Сначала строится список рёбер (Edge Table, ET), отсортированный по y-координате нижнего конца рёбер.

Каждое ребро содержит:
- Верхнюю и нижнюю y-координаты,
- x-координату начальной точки,
- Обратную величину наклона (dx/dy).
- 
Далее выполняется проход построчно, начиная от минимального y к максимальному:
1. Добавляются рёбра из списка, если их нижний конец достигнут.
2. Удаляются рёбра, если их верхний конец достигнут.
3. Сортируется текущий список активных рёбер.
4. Выполняется заливка между парами пересечений.
5. x-координаты рёбер обновляются (x += dx/dy).
### Алгоритм растровой развертки с использованием списка активных рёбер
Вместо хранения всех рёбер сразу, ведётся только список активных рёбер.

Алгоритм работы:
1. Рёбра, начинающиеся на текущей строке, добавляются в AET.
2. Все рёбра, у которых ymax совпадает с текущим y, удаляются.
3. В AET рёбра сортируются по x-координате.
4. Выполняется заливка между парами пересечений.
5. Обновляются x-координаты активных рёбер (x += dx/dy).
6. Повторяется, пока не будет обработан весь многоугольник.
Этот метод динамически обновляет список активных рёбер, что делает его эффективным.
### Простой алгоритм заполнения с затравкой
Применяется для заливки замкнутых областей.

Выбирается затравочная точка внутри области. Затем рекурсивно или с помощью стека проверяются соседние пиксели:
- Если они имеют исходный цвет, то перекрашиваются в новый.
- Для каждого изменённого пикселя проверяются его соседи.
- Алгоритм продолжается, пока вся область не будет закрашена.
  
Недостатки:
- Рекурсивный вариант может привести к переполнению стека.
- Медленно работает на сложных формах.
### Построчный алгоритм заполнения с затравкой
Оптимизированная версия Flood Fill, использующая построчную заливку.

Алгоритм работы:
1. Выбирается затравочная точка.
2. Определяется горизонтальный отрезок пикселей в этой строке, который можно закрасить (до границы).
3. Заполняется найденный отрезок.
4. В стек добавляются затравочные точки соседних строк (над и под текущей).
5. Повторяется, пока не будут обработаны все пиксели.

Преимущества:
- Избегает переполнения стека.
- Работает быстрее обычного Flood Fill.
## Интерфейс
![image](https://github.com/user-attachments/assets/2c6e776f-8f96-4c23-ac06-98cca210f975)

## Реализация
### Алгоритм растровой развертки с упорядоченным списком рёбер
```java
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.size() < 3) {
            return filledPixels;
        }

        int minY = polygon.get(0).y;
        int maxY = polygon.get(0).y;
        for (Point p : polygon) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y != p2.y) {
                Edge edge = new Edge(p1, p2);
                edges.add(edge);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (Edge edge : edges) {
                if (y >= edge.minY && y < edge.maxY) {
                    int x = (int) (edge.x + (y - edge.y1) * edge.slope);
                    intersections.add(x);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }
        }

        return filledPixels;
    }
```
### Алгоритм растровой развертки с использованием списка активных рёбер
```java
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.isEmpty()) {
            return filledPixels;
        }

        int yMin = Integer.MAX_VALUE;
        int yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        Map<Integer, List<Edge>> edgeTable = new HashMap<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y == p2.y) continue;

            Edge edge = new Edge(Math.max(p1.y, p2.y), p1.y < p2.y ? p1.x : p2.x, (float) (p2.x - p1.x) / (p2.y - p1.y));
            int yStart = Math.min(p1.y, p2.y);

            if (!edgeTable.containsKey(yStart)) {
                edgeTable.put(yStart, new ArrayList<>());
            }
            edgeTable.get(yStart).add(edge);
        }

        List<Edge> activeEdges = new ArrayList<>();

        for (int y = yMin; y <= yMax; y++) {
            if (edgeTable.containsKey(y)) {
                activeEdges.addAll(edgeTable.get(y));
            }

            int finalY = y;
            activeEdges.removeIf(edge -> edge.yMax <= finalY);

            activeEdges.sort(Comparator.comparing(edge -> edge.x));

            for (int i = 0; i < activeEdges.size(); i += 2) {
                int xStart = (int) Math.ceil(activeEdges.get(i).x);
                int xEnd = (int) Math.floor(activeEdges.get(i + 1).x);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }

            for (Edge edge : activeEdges) {
                edge.x += edge.slope;
            }
        }

        return filledPixels;
    }
```
### Простой алгоритм заполнения с затравкой
```java
    public static List<Point> fillPolygon(List<Point> polygon, Point seed) {
        List<Point> filledPixels = new ArrayList<>();
        if (polygon.isEmpty() || seed == null) {
            return filledPixels;
        }

        int xMin = Integer.MAX_VALUE, xMax = Integer.MIN_VALUE;
        int yMin = Integer.MAX_VALUE, yMax = Integer.MIN_VALUE;
        for (Point p : polygon) {
            if (p.x < xMin) xMin = p.x;
            if (p.x > xMax) xMax = p.x;
            if (p.y < yMin) yMin = p.y;
            if (p.y > yMax) yMax = p.y;
        }

        boolean[][] visited = new boolean[yMax - yMin + 1][xMax - xMin + 1];

        Stack<Point> stack = new Stack<>();
        stack.push(seed);

        while (!stack.isEmpty()) {
            Point current = stack.pop();
            int x = current.x;
            int y = current.y;

            if (x >= xMin && x <= xMax && y >= yMin && y <= yMax && !visited[y - yMin][x - xMin]) {
                if (isPointInsidePolygon(polygon, current)) {
                    visited[y - yMin][x - xMin] = true;
                    filledPixels.add(new Point(x, y));

                    stack.push(new Point(x + 1, y));
                    stack.push(new Point(x - 1, y));
                    stack.push(new Point(x, y + 1));
                    stack.push(new Point(x, y - 1));
                }
            }
        }

        return filledPixels;
    }
```
### Построчный алгоритм заполнения с затравкой
```java
    public static List<Point> fillPolygon(List<Point> polygon) {
        List<Point> filledPixels = new ArrayList<>();

        if (polygon.size() < 3) {
            return filledPixels;
        }

        int minY = polygon.get(0).y;
        int maxY = polygon.get(0).y;
        for (Point p : polygon) {
            if (p.y < minY) minY = p.y;
            if (p.y > maxY) maxY = p.y;
        }

        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < polygon.size(); i++) {
            Point p1 = polygon.get(i);
            Point p2 = polygon.get((i + 1) % polygon.size());

            if (p1.y != p2.y) {
                Edge edge = new Edge(p1, p2);
                edges.add(edge);
            }
        }

        for (int y = minY; y <= maxY; y++) {
            List<Integer> intersections = new ArrayList<>();

            for (Edge edge : edges) {
                if (y >= edge.minY && y < edge.maxY) {
                    int x = (int) (edge.x + (y - edge.y1) * edge.slope);
                    intersections.add(x);
                }
            }

            Collections.sort(intersections);

            for (int i = 0; i < intersections.size(); i += 2) {
                int xStart = intersections.get(i);
                int xEnd = intersections.get(i + 1);

                for (int x = xStart; x <= xEnd; x++) {
                    filledPixels.add(new Point(x, y));
                }
            }
        }

        return filledPixels;
    }
```
</details>

<details>
  <summary>Лабораторная работа №7</summary>
  
## Цель
Целью данной лабораторной работы является разработка графической программы, которая выполняет триангуляцию Делоне и строит диаграмму Вороного по заданному набору точек.
## Алгоритмы
### Триангуляция Делоне
Триангуляция Делоне — это разбиение множества точек на плоскости на треугольники таким образом, что ни одна точка не попадает внутрь описанной окружности любого треугольника. Это обеспечивает максимальную равномерность треугольников и минимизирует "острые" углы.
### Диаграмма Вороного
Диаграмма Вороного — это разбиение плоскости на области (ячейки), где каждая ячейка соответствует одной точке из заданного множества. Все точки внутри ячейки ближе к соответствующей точке, чем к любой другой точке из множества.
## Интерфейс
![image](https://github.com/user-attachments/assets/9e3a16ac-5fc7-4442-9760-1790169d0ee9)

![image](https://github.com/user-attachments/assets/60480613-430f-4cf5-95d0-bd8020ba6f19)

## Реализация
### Триангуляция Делоне
```java
private void performTriangulation(List<Pixel> points) {
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (Pixel p : points) {
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }

        int dx = maxX - minX;
        int dy = maxY - minY;
        int deltaMax = Math.max(dx, dy) * 10;

        Pixel p1 = new Pixel(minX - deltaMax, minY - deltaMax);
        Pixel p2 = new Pixel(minX + deltaMax, minY - deltaMax);
        Pixel p3 = new Pixel(minX, minY + deltaMax * 2);

        Triangle superTriangle = new Triangle(p1, p2, p3);
        triangles.add(superTriangle);

        for (Pixel p : points) {
            List<Triangle> badTriangles = new ArrayList<>();
            List<Edge> edges = new ArrayList<>();

            for (Triangle t : triangles) {
                if (t.containsInCircumcircle(p)) {
                    badTriangles.add(t);
                    edges.addAll(t.getEdges());
                }
            }

            triangles.removeAll(badTriangles);

            edges = removeDuplicateEdges(edges);

            for (Edge edge : edges) {
                triangles.add(new Triangle(edge.getA(), edge.getB(), p));
            }
        }

        triangles.removeIf(t -> t.hasVertex(p1) || t.hasVertex(p2) || t.hasVertex(p3));
    }
```
### Диаграмма Вороного
```java
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
```
</details>

# Технологии
- Java
- Spring Boot
- Spring Web
- WebSocket
- JavaScript
- HTML5
- CSS
