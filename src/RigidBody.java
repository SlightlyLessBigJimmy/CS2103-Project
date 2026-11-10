import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.EventListener;

public class RigidBody implements GlobalUpdate{
    private EventListenerList collisionListeners = new EventListenerList();
    private GameObject gameObject;
    private Vector3 velocity;
    private Vector3 centerOfMass;
    private double mass;


    void addCollisionListener(CollisionListener listener) {collisionListeners.add(CollisionListener.class, listener);}
    void removeCollisionListener(CollisionListener listener) {collisionListeners.remove(CollisionListener.class, listener);}

    public RigidBody(GameObject gameObject){
        //Initialize the rigidbody to its parent GameObject
        this.gameObject = gameObject;
        this.velocity = new Vector3();
        this.centerOfMass = new Vector3();

    }

    public void update() {
        //TODO: Make velocity instead of direct transform
        newPosition();
    }

    private void newPosition(){
        gameObject.transform.position = new Vector3
                (
                gameObject.transform.position.x + velocity.x,
                gameObject.transform.position.y + velocity.y,
                gameObject.transform.position.z + velocity.z
                );
    }

    protected void onCollision(CollisionEvent e) {
        CollisionListener[] listeners = collisionListeners.getListeners(CollisionListener.class);
        for(CollisionListener listener : listeners) listener.collided(e);
    }
}





class CollisionEvent extends EventObject{
    public CollisionEvent(RigidBody source){
        super(source);
    }
}