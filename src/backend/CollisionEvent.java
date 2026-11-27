package backend;

import java.util.EventObject;

public class CollisionEvent extends EventObject {
    Hitbox other; //The other hitbox
    Vector3 sourceHitPosition; //The closest vertex to the collision site
    public CollisionEvent(Hitbox source, Vector3 sourceHitPosition, Hitbox other){
        super(source);
        this.sourceHitPosition = sourceHitPosition;
        this.other = other;
    }
}