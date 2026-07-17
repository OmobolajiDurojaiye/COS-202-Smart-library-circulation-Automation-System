package utils;

public class IDGenerator {
    private static int counter = 1000;

    public static synchronized String generateItemId() {
        return "ITM" + (counter++);
    }

    public static synchronized String generateUserId() {
        return "USR" + (counter++);
    }
}
