public class VehicleState {
    String Panic; //Erratic movements. Will no longer signal and is just trying to stay alive
    String Breakdown; //Parked in the breakdown lane. Get picked up by tow truck when this state is reached
    String Damaged; //my leg!
    String Destroyed; //MY LEG!
    String Driving; //Having a grand ol' time.
}

/*
Driving -> Panic, Damaged
Panic -> Driving, Damaged
Damaged -> Breakdown, Destroyed
*/