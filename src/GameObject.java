public class GameObject {
    public boolean visible = true;
    public Transform transform;

    public GameObject(){
        this.transform = new Transform();
    }

    public GameObject(Vector3 position, Vector3 size, double orientation){
        this.transform = new Transform(position, size, orientation);
    }

    public GameObject(Transform transform){
        this.transform = transform;
    }

    public boolean onCollision() {
        //TODO: Raise an event for the front end. Pass collision data (i.e. where the collision happened on the model).

        return true; //TODO: Return true if there actually was a collision. Return false otherwise. For ease of error handling
    }

    public boolean onCollision(GameObject other) {
        //TODO: Raise an event for the front end. Pass collision data (i.e. where the collision happened on the model).

        return true; //TODO: Return true if there actually was a collision. Return false otherwise. For ease of error handling
    }
}
