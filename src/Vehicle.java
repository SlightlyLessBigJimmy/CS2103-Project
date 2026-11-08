public abstract class Vehicle extends GameObject{
    private Transform transform;
    private boolean isCrashed; //So it can be towed

    protected void centerOnLane(int lane) {
        //TODO: Default implementation for self-centering on a lane.
    }

    protected void changeState() {
        //Pass events that could cause a state change and have this decide based on the behavior type of the given vehicle maybe?
        //By default, panic if a nearby car crashed, damaged if touched by another car, etc
    }
}