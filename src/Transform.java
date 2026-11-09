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
}
