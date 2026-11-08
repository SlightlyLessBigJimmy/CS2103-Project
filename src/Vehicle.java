public abstract class Vehicle {
    //TODO: transform and position things
    private boolean isCrashed; //So it can be towed

    public boolean onCollision() {
        //TODO: Assumedly, use the GameObject implementation of onCollision.
        //TODO: Raise an event for the front end. Pass collision data (i.e. where the collision happened on the model).

        return true; //TODO: Return true if there actually was a collision. Return false otherwise. For ease of error handling
    }

    protected void centerOnLane(int lane) {
        //TODO: Default implementation for self-centering on a lane.
    }

    protected void changeState() {
        //Pass events that could cause a state change and have this decide based on the behavior type of the given vehicle maybe?
        //By default, panic if a nearby car crashed, damaged if touched by another car, etc
    }
}