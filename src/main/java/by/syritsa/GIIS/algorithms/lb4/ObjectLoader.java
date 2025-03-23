package by.syritsa.GIIS.algorithms.lb4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ObjectLoader {
    public static Object3D loadFromFile(String filename) throws IOException {
        Object3D object = new Object3D();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) continue;

                    try {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);
                        object.addVertex(x, y, z);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка чтения координат: " + line);
                    }
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    if (parts.length < 4) continue;

                    try {
                        int[] vertexIndices = new int[parts.length - 1];
                        for (int i = 1; i < parts.length; i++) {
                            vertexIndices[i - 1] = Integer.parseInt(parts[i].split("/")[0]) - 1;
                        }
                        object.addFace(vertexIndices);
                    } catch (NumberFormatException e) {
                        System.err.println("Ошибка чтения индексов граней: " + line);
                    }
                }
            }
        }
        return object;
    }
}