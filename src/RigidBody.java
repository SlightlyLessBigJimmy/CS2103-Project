import javax.swing.event.EventListenerList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.EventListener;

public class RigidBody implements GlobalUpdate{
    protected EventListenerList collisionListeners = new EventListenerList();
    protected GameObject gameObject;
    protected Vector3 velocity;
    protected Vector3 centerOfMass;
    protected Vector3 pivot;

    protected double mass = 1;
    protected double drag = 0;

    void addCollisionListener(CollisionListener listener) {collisionListeners.add(CollisionListener.class, listener);}
    void removeCollisionListener(CollisionListener listener) {collisionListeners.remove(CollisionListener.class, listener);}

    public RigidBody(GameObject gameObject){
        //Initialize the rigidbody to its parent GameObject
        this.gameObject = gameObject;
        this.velocity = new Vector3();
        this.centerOfMass = new Vector3();

    }

    public void update(double deltaTime) {
        //TODO: Make velocity instead of direct transform
        newPosition(deltaTime);
    }

    private void newPosition(double deltaTime){
        velocity = velocity.multiply(1-drag * deltaTime);//Apply drag

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

    public void addForce(Vector3 forceVector){
        //ToDo: make add force work
    }
}





class CollisionEvent extends EventObject{
    public CollisionEvent(RigidBody source){
        super(source);
    }
}