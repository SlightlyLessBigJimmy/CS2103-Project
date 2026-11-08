public class Timing {
    private static float deltaTime; // change in time between rendered frames
    private static float fixedDeltaTime; // 1/framerate, if framerate is fixed

    public static float deltaTime() {
        return deltaTime;
    }

    public static float fixedDeltaTime() {
        return fixedDeltaTime;
    }

    //TODO: Calculate deltaTime and fixedDeltaTime
}
