package by.syritsa.GIIS.algorithms.lb4;

import java.util.ArrayList;
import java.util.List;

public class Object3D {
    private final List<double[]> vertices = new ArrayList<>();
    private final List<int[]> faces = new ArrayList<>();

    public void addVertex(double x, double y, double z) {
        vertices.add(new double[]{x, y, z, 1});
    }

    public void addFace(int[] vertexIndices) {
        faces.add(vertexIndices);
    }

    public List<double[]> getVertices() {
        return vertices;
    }

    public List<int[]> getFaces() {
        return faces;
    }

    public void transform(Matrix4x4 matrix) {
        for (double[] vertex : vertices) {
            double[] transformed = matrix.transform(vertex);
            System.arraycopy(transformed, 0, vertex, 0, 4);
        }
    }

    public void rotateY(double angle) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.rotationY(angle));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    public void rotateX(double angle) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.rotationX(angle));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    public void scale(double factor) {
        double[] center = findCenter();
        transform(Matrix4x4.translation(-center[0], -center[1], -center[2]));
        transform(Matrix4x4.scaling(factor, factor, factor));
        transform(Matrix4x4.translation(center[0], center[1], center[2]));
    }

    private double[] findCenter() {
        double x = 0, y = 0, z = 0;
        for (double[] vertex : vertices) {
            x += vertex[0];
            y += vertex[1];
            z += vertex[2];
        }
        int count = vertices.size();
        return new double[]{x / count, y / count, z / count};
    }
}