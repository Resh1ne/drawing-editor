package by.syritsa.GIIS.algorithms.lb7;

import lombok.Data;
import lombok.Getter;

@Getter
@Data
public class LineSegment {
    private Pixel start;
    private Pixel end;

    public LineSegment(Pixel start, Pixel end) {
        this.start = start;
        this.end = end;
    }

    @Override
    public String toString() {
        return "LineSegment{" + start + " -> " + end + "}";
    }
}