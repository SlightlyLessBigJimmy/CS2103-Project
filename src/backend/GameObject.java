package backend;
import Main.*;
import frontend.Sprite;

import java.awt.*;

public class GameObject implements GlobalUpdate{
    public boolean active = true;
    public Transform transform;
    public Sprite sprite;
    private String spriteId;
    private RigidBody rb;
    private Hitbox hitbox;

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

    public String getSpriteId() {return this.spriteId;}
}
