package backend;

public class CustomMath {//this name is really stupid but idk what to call this
    //this class is just a spot to put all our math functions that might be needed anywhere and dont fit in one class.

    public static double lerp(double start, double end, double t){
        return start + (end - start) * t;
    }

}
