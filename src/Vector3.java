public class Vector3{
    public double x;
    public double y;
    public double z;

    public Vector3(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(){
        new Vector3(0,0,0);
    }

    public double sum(){
        return this.x+this.y+this.z;
    }


    public Vector3 multiply(Vector3 other){
        return new Vector3(this.x * other.x, this.y * other.y, this.z * other.z);
    }
    public Vector3 multiply(double scalar){
        return new Vector3(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public double magnitude(){
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z , 2));
    }

    public double dot(Vector3 other){
        return this.x * other.x + this.y * other.y + this.z * other.z;
    }
}
