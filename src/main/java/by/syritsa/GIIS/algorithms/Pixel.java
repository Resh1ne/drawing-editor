package by.syritsa.GIIS.algorithms;

public class Pixel {
    public int x;
    public int y;
    public float intensity;

    public Pixel(int x, int y, float intensity) {
        this.x = x;
        this.y = y;
        this.intensity = intensity;
    }

    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
