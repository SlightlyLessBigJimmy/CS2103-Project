import java.util.HashMap;

public class Cars {
    private static final HashMap<String, CarStats> carStatsMap = new HashMap<>();

    public static CarStats getCarStats(String carName) {
        try {
            return carStatsMap.get(carName);
        }
        catch (Exception e) {
            throw new RuntimeException("Threw an exception. Tried to get stats from a car whose name does not exist.");
        }
    }

    public static void populateCarStats() {
        //TODO: Call this function on startup, give front end an easy way to pass in cars. This should only run once at startup.
        carStatsMap.put("Temp", new CarStats(100, new Hitbox())); //I just wanted the warning to go away
    }
}