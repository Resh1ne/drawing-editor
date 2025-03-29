package by.syritsa.GIIS.algorithms.lb7;

import lombok.Getter;

@Getter
public class Rectangle {
    private final int x;
    private final int y;
    private final int width;
    private final int height;

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public String toString() {
        return "Rectangle{" + "x=" + x + ", y=" + y +
                ", width=" + width + ", height=" + height + '}';
    }
}