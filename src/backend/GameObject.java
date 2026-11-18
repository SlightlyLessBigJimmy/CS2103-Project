package backend;

public class GameObject implements GlobalUpdate{
    public boolean visible = true;
    public Transform transform;
    public String spriteId;
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
}
