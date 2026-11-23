package backend;

import java.util.EventObject;

public class CollisionEvent extends EventObject {
    public CollisionEvent(RigidBody source){
        super(source);
    }
}