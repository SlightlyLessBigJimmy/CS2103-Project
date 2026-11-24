package backend;

import java.util.EventObject;

public class CollisionEvent extends EventObject {
    Hitbox other; //The other hitbox
    Vector3 sourceHitPosition; //The closest vertex to the collision site
    Vector3 otherHitPosition; //The closest vertex to the collision on the other hitbox
    public CollisionEvent(Hitbox source, Vector3 sourceHitPosition, Hitbox other, Vector3 otherHitPosition){
        super(source);
        this.other = other;
    }
}