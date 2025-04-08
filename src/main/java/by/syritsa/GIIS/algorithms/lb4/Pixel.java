package by.syritsa.GIIS.algorithms.lb4;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@Builder
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Pixel {
    @NonNull
    private int x;
    @NonNull
    private int y;
    private String color = "rgba(0, 0, 0, 255)";

    public static int getDistance(Pixel p0, Pixel p1) {
        return ((int) Math.sqrt(Math.pow(p0.getX()-Math.abs(p0.getX()-p1.getX()),2)+Math.pow(p0.getY()-Math.abs(p0.getY()-p1.getY()),2)));
    }

}
