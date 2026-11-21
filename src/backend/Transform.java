package backend;

public class Transform {
    public Vector3 position;
    public Vector3 size;
    public double orientation;

    public Transform(){
        position = new Vector3(0,0, 0);
        size = new Vector3(1,1, 1);
        orientation = 0;
    }

    public Transform(Vector3 position, Vector3 size, double orientation){
        this.position = position;
        this.size = size;
        this.orientation = orientation;
    }

    public Transform(Transform other){
        this.position = other.position;
        this.size = other.position;
        orientation = other.orientation;
    }

    public Vector3 up(){
        double rad = Math.toRadians(orientation);
        double x = -Math.sin(rad);
        double y = Math.cos(rad);
        return new Vector3(x,y,0);
    }

    public Vector3 right(){
        double rad = Math.toRadians(orientation);
        double x = Math.cos(rad);
        double y = Math.sin(rad);
        return new Vector3(x,y,0);
    }

}
