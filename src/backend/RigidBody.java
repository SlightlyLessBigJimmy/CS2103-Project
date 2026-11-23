package backend;

import javax.swing.event.EventListenerList;
import java.util.EventObject;

public class RigidBody implements GlobalUpdate{
    protected final GameObject gameObject;
    protected Vector3 linearVelocity;
    protected Vector3 angularVelocity;
    protected Vector3 centerOfMass;
    protected Vector3 pivot;

    protected double mass = 1;
    protected double drag = 0;

    public RigidBody(GameObject gameObject){
        //Initialize the rigidbody to its parent backend.GameObject
        this.gameObject = gameObject;
        this.linearVelocity = new Vector3();
        this.centerOfMass = new Vector3();

    }

    public void update(double deltaTime) {
        //TODO: Make velocity instead of direct transform
        newPosition(deltaTime);
    }

    private void newPosition(double deltaTime){
        linearVelocity = linearVelocity.multiply(1-drag * deltaTime);//Apply drag

        gameObject.transform.position = new Vector3
                (
                gameObject.transform.position.x + linearVelocity.x,
                gameObject.transform.position.y + linearVelocity.y,
                gameObject.transform.position.z + linearVelocity.z
                );
    }

    public void addForce(Vector3 forceVector){
        //ToDo: make add force work
    }

    public void physicsRotate(double orientation){
        //ToDo: applies the required angular velocity to bring the object to a given orientation.
    }
}
