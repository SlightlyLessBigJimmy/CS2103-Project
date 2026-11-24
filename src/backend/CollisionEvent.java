package backend;

import java.util.EventObject;

public class CollisionEvent extends EventObject {
    public CollisionEvent(Hitbox source){
        super(source);
    }
}