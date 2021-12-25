package OGL;

public class Vector {
    public double x;
    public double y;
    public double z;

    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }

    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double scalarMult(Vector c) {
        return x * c.x + y * c.y + z * c.z;
    }

    public Vector vectorMult(Vector c) {
        double tmpX = y * c.z - z * c.y;
        double tmpY = -(x * c.z - z * c.x);
        double tmpZ = z * c.y - y * c.x;
        return new Vector(tmpX, tmpY, tmpZ);
    }

    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}