package by.syritsa.GIIS.algorithms.lb4;

public class Matrix4x4 {
    private final double[][] matrix = new double[4][4];

    public Matrix4x4() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = (i == j) ? 1 : 0;
            }
        }
    }

    public Matrix4x4 multiply(Matrix4x4 other) {
        Matrix4x4 result = new Matrix4x4();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                result.matrix[i][j] = 0;
                for (int k = 0; k < 4; k++) {
                    result.matrix[i][j] += this.matrix[i][k] * other.matrix[k][j];
                }
            }
        }
        return result;
    }

    public double[] transform(double[] point) {
        double[] result = new double[4];
        for (int i = 0; i < 4; i++) {
            result[i] = 0;
            for (int j = 0; j < 4; j++) {
                result[i] += matrix[i][j] * point[j];
            }
        }
        return result;
    }

    public static Matrix4x4 translation(double tx, double ty, double tz) {
        Matrix4x4 m = new Matrix4x4();
        m.matrix[0][3] = tx;
        m.matrix[1][3] = ty;
        m.matrix[2][3] = tz;
        return m;
    }

    public static Matrix4x4 scaling(double sx, double sy, double sz) {
        Matrix4x4 m = new Matrix4x4();
        m.matrix[0][0] = sx;
        m.matrix[1][1] = sy;
        m.matrix[2][2] = sz;
        return m;
    }

    public static Matrix4x4 rotationX(double angle) {
        Matrix4x4 m = new Matrix4x4();
        double rad = Math.toRadians(angle);
        m.matrix[1][1] = Math.cos(rad);
        m.matrix[1][2] = -Math.sin(rad);
        m.matrix[2][1] = Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix4x4 rotationY(double angle) {
        Matrix4x4 m = new Matrix4x4();
        double rad = Math.toRadians(angle);
        m.matrix[0][0] = Math.cos(rad);
        m.matrix[0][2] = Math.sin(rad);
        m.matrix[2][0] = -Math.sin(rad);
        m.matrix[2][2] = Math.cos(rad);
        return m;
    }

    public static Matrix4x4 perspective(double fov, double aspect, double near, double far) {
        Matrix4x4 m = new Matrix4x4();
        double f = 1.0 / Math.tan(Math.toRadians(fov) / 2);
        m.matrix[0][0] = f / aspect;
        m.matrix[1][1] = f;
        m.matrix[2][2] = (far + near) / (near - far);
        m.matrix[2][3] = (2 * far * near) / (near - far);
        m.matrix[3][2] = -1;
        m.matrix[3][3] = 0;
        return m;
    }
}