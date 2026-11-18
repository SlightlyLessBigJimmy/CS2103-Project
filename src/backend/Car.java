package backend;

public class Car extends Vehicle {
    //this class is mostly for the player movement, but could be used for NPC cars as well if that's how we want to do things

    //region car specifications
    public double accelerationFactor;
    public double driftFactor;
    public double maxDragResistance;
    public double maxReverseSpeed;
    public double maxSpeed;
    public double turnFactor;

    private double accelerationInput;
    private double rotationAngle;
    private double steeringInput;

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

    public void update(double deltaTime){
        applyEngineForce(deltaTime);
        applySteering();
    }

    //ToDo figure out where to put Lerp
    double lerp(double start, double end, double t){
        return start + (end - start) * t;
    }

    void applyEngineForce(double deltaTime){

        double velocityVsUp = this.transform.up().dot(rigidBody.velocity);

        //Limit speed forward
        if (velocityVsUp > maxSpeed && accelerationInput > 0){
            return;
        }

        //limit speed reverse
        if (velocityVsUp < maxReverseSpeed  && accelerationInput < 0){
            return;
        }

        //limit to max speed while accelerating
        if (Math.pow(rigidBody.velocity.magnitude(), 2) > Math.pow(maxSpeed, 2) && accelerationInput > 0){
            return;
        }

        //apply drag to slow down while not accelerating (so you stop when you stop pressing gas)
        if (accelerationInput == 0){
            rigidBody.drag = lerp(rigidBody.drag, maxDragResistance, deltaTime * maxDragResistance);
        }

        Vector3 engineForce = this.transform.up().multiply(accelerationInput).multiply(accelerationFactor);
        rigidBody.addForce(engineForce);
    }


    void applySteering()
    {
        // get forward and right velocity of the car
        Vector3 forwardVelocity = this.transform.up().multiply(rigidBody.velocity.dot(this.transform.up()));
        Vector3 rightVelocity = this.transform.right().multiply(rigidBody.velocity.dot(this.transform.right()));

        // remove the orthogonal velocity based on the drift factor
        rigidBody.velocity = forwardVelocity.add(rightVelocity.multiply(driftFactor));

        //limit turning with low speed
        double minSpeedBeforeAllowTurningFactor = (rigidBody.velocity.magnitude() / 8);
        minSpeedBeforeAllowTurningFactor = Math.clamp(minSpeedBeforeAllowTurningFactor, 0, 1);

        // invert steering while reversing
        double direction = rigidBody.velocity.dot(this.transform.up()) >= 0 ? 1 : -1;

        // update the rotation angle based on input
        rotationAngle -= steeringInput * turnFactor * minSpeedBeforeAllowTurningFactor * direction;

        // apply steering by rotating the car object
        rigidBody.physicsRotate(rotationAngle);
    }
}
