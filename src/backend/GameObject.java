package backend;
import Main.*;

public class GameObject implements GlobalUpdate{
    public boolean active = false;
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

    protected void destroy(){
        Main.removeFromUpdateListeners(this);
    }
}
