package backend;

public class Timing{

    private static long lastFrameTime = System.nanoTime();

    private static float deltaTime; // change in time between rendered frames, in seconds

    public static float deltaTime() {
        return deltaTime;
    }

    public static void updateTime(){
        long now = System.nanoTime();
        deltaTime = (float) ((now-lastFrameTime) / 1000000000);
        lastFrameTime = now;
    }

    public static void updateTime(int frameRate) {
        deltaTime = (float) (1.0 / frameRate);
    }
}
