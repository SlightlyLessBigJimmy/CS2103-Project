public class GameObject {
    public boolean visible = true;
    public boolean canCollide = false;
    public Transform transform;

    public GameObject(){
        this.transform = new Transform();
    }

    public GameObject(Vector3 position, Vector3 size, double orientation){
        this.transform = new Transform(position, size, orientation);
    }

    public GameObject(Transform transform){
        this.transform = transform;
    }
}
