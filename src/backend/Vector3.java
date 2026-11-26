package backend;

public class Vector3{
    public double x;
    public double y;
    public double z;

    public Vector3(){
        new Vector3(0,0,0);
    }
    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    //adds up all dimensions of the vector
    public double sum(){
        return this.x+this.y+this.z;
    }

    //multiplies by another vector or a scalar
    public Vector3 multiply(Vector3 other){
        return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
    }
    public Vector3 multiply(double scalar){
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    //divides by another vector (component-wise) or a scalar
    public Vector3 divide(Vector3 other){
        if (other.x == 0 || other.y == 0 || other.z == 0){
            throw new IllegalStateException("no noo divide by zero");
        }
        return new Vector3(this.x / other.x, this.y / other.y, this.z / other.z);
    }
    public Vector3 divide(double scalar){
        if (scalar == 0){
            throw new IllegalStateException("no noo divide by zero");
        }
        return new Vector3(this.x / scalar, this.y / scalar, this.z / scalar);
    }

    //adds another vector or scalar to this
    public Vector3 add(Vector3 other){
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }
    public Vector3 add(double scalar){
        return new Vector3(this.x + scalar, this.y + scalar, this.z + scalar);
    }

    //subtracts another vector or scalar from this
    public Vector3 subtract(Vector3 other){
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }
    public Vector3 subtract(double scalar){
        return new Vector3(this.x - scalar, this.y - scalar, this.z - scalar);
    }

    public double magnitude(){
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z , 2));
    }

    public double dot(Vector3 other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }

    public double distance(Vector3 other) {return line(other).magnitude();}

    /**
     * Create a line to another point, originating from this Vector3. Assumes both Vector3s are points.
     * @param other the point to draw a line to
     * @return a Vector3 with magnitude and direction that represents a line from the position of the caller to the other Vector3 position.
     */
    public Vector3 line(Vector3 other) {return new Vector3(other.x-this.x, other.y-this.y, other.z-this.z);}

    public Vector3 normalized() {
        double magnitude = magnitude();
        return new Vector3(this.x / magnitude, this.y / magnitude, this.z / magnitude);
    }

    /**
     * Rotates a Vector3 around (0,0) on the (x,y) plane.
     * @param orientation The angle, in degrees, to rotate by.
     * @return A new Vector3 which represents the caller rotated.
     */
    public Vector3 rotate2D(double orientation) {
        double radians = Math.toRadians(orientation);
        return new Vector3(
                this.x * Math.cos(radians) - this.y * Math.sin(radians),
                this.x * Math.sin(radians) + this.y * Math.cos(radians),
                this.z
                );
    }

    @Override
    public String toString(){
        return "(" + x + ", " + y + ", " + z + ")";
    }
}
