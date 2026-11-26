package backend;

public class Car extends Vehicle {

    //TODO: What actually needs to be in this class, and what can we move to the Vehicle class?

    //this class is mostly for the player movement, but could be used for NPC cars as well if that's how we want to do things

    //region car specifications
    private final double accelerationFactor;
    private final double driftFactor;
    private final double maxDragResistance;
    private final double maxReverseSpeed;
    private final double maxSpeed;
    private final double turnFactor;
    private double rotationAngle;

    protected double accelerationInput;
    protected double steeringInput;

    private RigidBody rigidBody = new RigidBody(this);

    //endregion

    //region Constructors

    public Car() {
        turnFactor = 3.5;
        accelerationFactor = 30;
        driftFactor = 0.95;
        maxSpeed = 20;
        maxReverseSpeed = this.maxSpeed/2;
        maxDragResistance = 3;

        accelerationInput = 0;
        steeringInput = 0;
        rotationAngle = 0;
    }

    public Car(double turnFactor, double accelerationFactor, double driftFactor, double maxSpeed, double maxReverseSpeed, double maxDragResistance,
               double accelerationInput, double steeringInput, double rotationAngle) {
        //TODO: Improve this because it's horrible to use
        this.turnFactor = turnFactor;
        this.accelerationFactor = accelerationFactor;
        this.driftFactor = driftFactor;
        this.maxSpeed = maxSpeed;
        this.maxReverseSpeed = maxReverseSpeed;
        this.maxDragResistance = maxDragResistance;

        this.accelerationInput = accelerationInput;
        this.steeringInput = steeringInput;
        this.rotationAngle = rotationAngle;
    }

    //endregion

    public void updateInputs(double acceleration, double steering){
        this.accelerationInput = acceleration;
        this.steeringInput = steering;
    }
    /* TODO: Rather than directly modifying transform, use the physics we added to RigidBody.java. Follows encapsulation better.
    public void update(double deltaTime){
        applyEngineForce(deltaTime);
        applySteering();
    }

    void applyEngineForce(double deltaTime){

        double velocityVsUp = this.transform.up().dot(rigidBody.linearVelocity);

        //Limit speed forward
        if (velocityVsUp > maxSpeed && accelerationInput > 0){
            return;
        }

        //limit speed reverse
        if (velocityVsUp < maxReverseSpeed  && accelerationInput < 0){
            return;
        }

        //limit to max speed while accelerating
        if (Math.pow(rigidBody.linearVelocity.magnitude(), 2) > Math.pow(maxSpeed, 2) && accelerationInput > 0){
            return;
        }

        //apply drag to slow down while not accelerating (so you stop when you stop pressing gas)
        if (accelerationInput == 0){
            rigidBody.linearDrag = lerp(rigidBody.linearDrag, maxDragResistance, deltaTime * maxDragResistance);
        }

        Vector3 engineForce = this.transform.up().multiply(accelerationInput).multiply(accelerationFactor);
        rigidBody.addForce(engineForce);
    }


    void applySteering()
    {
        // get forward and right velocity of the car
        Vector3 forwardVelocity = this.transform.up().multiply(rigidBody.linearVelocity.dot(this.transform.up()));
        Vector3 rightVelocity = this.transform.right().multiply(rigidBody.linearVelocity.dot(this.transform.right()));

        // remove the orthogonal velocity based on the drift factor
        rigidBody.linearVelocity = forwardVelocity.add(rightVelocity.multiply(driftFactor));

        //limit turning with low speed
        double minSpeedBeforeAllowTurningFactor = (rigidBody.linearVelocity.magnitude() / 8);
        minSpeedBeforeAllowTurningFactor = Math.clamp(minSpeedBeforeAllowTurningFactor, 0, 1);

        // invert steering while reversing
        double direction = rigidBody.linearVelocity.dot(this.transform.up()) >= 0 ? 1 : -1;

        // update the rotation angle based on input
        rotationAngle -= steeringInput * turnFactor * minSpeedBeforeAllowTurningFactor * direction;

        // apply steering by rotating the car object
        rigidBody.moveRotation(rotationAngle);
    }
         */
}