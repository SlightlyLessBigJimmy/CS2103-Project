package backend;
import Main.*;

public class GameObject implements GlobalUpdate{
    public boolean active = true;
    public Transform transform;
    private String spriteId;
    //message from brody
    //TODO can we get a public String spriteID; that on creation gets generated a unique ID that frontend can hashmap our own collection of sprites for rendering

    public GameObject(){
        this.transform = new Transform();
        //can we insantiate a unique id here?
    }

    public GameObject(Vector3 position, Vector3 size, double orientation){
        this.transform = new Transform(position, size, orientation);
        //can we insantiate a unique id here?
    }

    public GameObject(Transform transform){
        this.transform = transform;
        //can we insantiate a unique id here?
    }

    protected void destroy(){
        Main.removeFromUpdateListeners(this);
    }

    public String getSpriteId() {return this.spriteId;}
}
