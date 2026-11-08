public class Transform {
    public Vector position = new Vector3(0,0, 0);
    public Vector size = new Vector3(1,1, 1);
    public double orientation = 0;

    public Transform(){

    }

    public Transform(Vector3 position, Vector3 size, double orientation){
        this.position = position;
        this.size = size;
        this.orientation = orientation;
    }
}
