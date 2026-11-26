package backend;

public class RigidBody implements GlobalUpdate{
    private final GameObject gameObject;
    private Vector3 centerOfMass;

    protected double angularVelocity;
    protected Vector3 linearVelocity;
    protected double linearDrag = 0;
    protected double angularDrag = 0;

    private double inverseMass = 1;
    private double inverseInertia = 1;

    private Vector3 forceAccumulator = new Vector3();
    private double torqueAccumulator = 0;

    private double rotationTarget = Double.NaN;

    public RigidBody(GameObject gameObject){
        //Initialize the rigidbody to its parent backend.GameObject
        this.gameObject = gameObject;
        this.linearVelocity = new Vector3();
        this.angularVelocity = 0;
        this.centerOfMass = new Vector3();
    }

    public void update() {
        updateLinear();
        updateAngular();
    }

    /**
     * Updates linear-related physics of this RigidBody.
     */
    private void updateLinear(){
        Vector3 acceleration = forceAccumulator.multiply(inverseMass);

        linearVelocity = linearVelocity.add(acceleration.multiply(Timing.deltaTime()));

        linearVelocity = linearVelocity.multiply(Math.exp(-linearDrag * Timing.deltaTime()));

        gameObject.transform.position = gameObject.transform.position.add(linearVelocity.multiply(Timing.deltaTime()));

        forceAccumulator = new Vector3(); //TODO: This method makes instantaneous forces less impactful at higher frame rates.
    }

    /**
     * Updates the angular-related physics of this RigidBody.
     */
    private void updateAngular(){

        // If the angle for the object was marked this frame, set it to that. Otherwise, proceed with angular physics.
        if (!Double.isNaN(rotationTarget)) {
            gameObject.transform.orientation = rotationTarget;
            angularVelocity = 0;
            torqueAccumulator = 0;
            rotationTarget = Double.NaN;
            return;
        }

        angularVelocity += (torqueAccumulator * inverseInertia) * Timing.deltaTime();
        angularVelocity *= Math.exp(-angularDrag * Timing.deltaTime());

        gameObject.transform.orientation = ((gameObject.transform.orientation + angularVelocity * Timing.deltaTime()) % 360 + 360) % 360;//wrap around to 0-360

        torqueAccumulator = 0; //TODO: This method makes instantaneous forces less impactful at higher frame rates.
    }

    public void setMass(double mass) {
        if (mass <= 0 || Double.isInfinite(mass)) {
            inverseMass = 0;
        } else {
            inverseMass = 1.0 / mass;
        }
    }

    public void setInertia(double inertia) {
        if (inertia <= 0 || Double.isInfinite(inertia)) {
            inverseInertia = 0;
        } else {
            inverseInertia = 1.0 / inertia;
        }
    }

    public double getMass() {
        if (inverseMass == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return 1.0 / inverseMass;
    }

    public void addForce(Vector3 force) {
        forceAccumulator = forceAccumulator.add(force);
    }

    public void addImpulse(Vector3 impulse) {
        linearVelocity = linearVelocity.add(impulse.multiply(inverseMass));
    }

    public void addTorque(double torque) {
        torqueAccumulator += torque;
    }

    public void addAngularImpulse(double impulse) {
        angularVelocity += impulse * inverseInertia;
    }

    /**
     * Directly set the angle of the object in the next frame.
     * @param orientation
     */
    public void setAngle(double orientation) {
        rotationTarget = ((orientation % 360) + 360) % 360;
    }
}
