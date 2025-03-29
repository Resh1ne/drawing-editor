package by.syritsa.GIIS.algorithms.lb7;

import lombok.*;
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

}