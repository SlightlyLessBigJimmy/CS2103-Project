package backend;

public class Car extends Vehicle {
    //this class is mostly for the player movement, but could be used for NPC cars as well if that's how we want to do things
    public double accelerationFactor = 30;
    public double turnFactor = 3.5;
    public double driftFactor = 0.95;
    public double maxSpeed = 20;
    public double maxReverseSpeed = this.maxSpeed/2;
    public double maxDragResistance = 3;

    private double accelerationInput = 0;
    private double steeringInput = 0;
    private double rotationAngle = 0;

    private RigidBody rigidbody = new RigidBody(this);

    public void updateInputs(double acceleration, double steering){
        this.accelerationInput = acceleration;
        this.steeringInput = steering;
    }

    public void fixedUpdate(double fixedDeltaTime){
        applyEngineForce(fixedDeltaTime);
        applySteering();
    }

    //ToDo figure out where to put Lerp
    double Lerp(double start, double end, double t){
        return start + (end - start) * t;
    }

    void applyEngineForce(double deltaTime){

        double velocityVsUp = this.transform.up().dot(rigidbody.velocity);

        //Limit speed forward
        if (velocityVsUp > maxSpeed && accelerationInput > 0){
            return;
        }

        //limit speed reverse
        if (velocityVsUp < maxReverseSpeed  && accelerationInput < 0){
            return;
        }

        //limit to max speed while accelerating
        if (Math.pow(rigidbody.velocity.magnitude(), 2) > Math.pow(maxSpeed, 2) && accelerationInput > 0){
            return;
        }

        //apply drag to slow down while not accelerating (so you stop when you stop pressing gas)
        if (accelerationInput == 0){
            rigidbody.drag = Lerp(rigidbody.drag, maxDragResistance, deltaTime * maxDragResistance);
        }

        Vector3 engineForce = this.transform.up().multiply(accelerationInput).multiply(accelerationFactor);
        rigidbody.addForce(engineForce);
    }


    void applySteering()
    {
        // get forward and right velocity of the car
        Vector3 forwardVelocity = this.transform.up().multiply(rigidbody.velocity.dot(this.transform.up()));
        Vector3 rightVelocity = this.transform.right().multiply(rigidbody.velocity.dot(this.transform.right()));

        // remove the orthogonal velocity based on the drift factor
        rigidbody.velocity = forwardVelocity.add(rightVelocity.multiply(driftFactor));

        //limit turning with low speed
        double minSpeedBeforeAllowTurningFactor = (rigidbody.velocity.magnitude() / 8);
        minSpeedBeforeAllowTurningFactor = Math.clamp(minSpeedBeforeAllowTurningFactor, 0, 1);

        // invert steering while reversing
        double direction = rigidbody.velocity.dot(this.transform.up()) >= 0 ? 1 : -1;

        // update the rotation angle based on input
        rotationAngle -= steeringInput * turnFactor * minSpeedBeforeAllowTurningFactor * direction;

        // apply steering by rotating the car object
        rigidbody.physicsRotate(rotationAngle);
    }
}
