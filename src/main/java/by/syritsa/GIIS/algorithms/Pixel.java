package by.syritsa.GIIS.algorithms;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
public class Pixel {
    public int x;
    public int y;
    public float intensity = 1.0f;

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
