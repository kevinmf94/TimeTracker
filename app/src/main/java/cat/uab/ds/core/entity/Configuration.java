package cat.uab.ds.core.entity;

/**
 * Class contains configuration variables.
 */
public final class Configuration {

    public static final float SECONDS_TO_MILLISECONDS = 1000;

    private static int minimumTime = 1; //Minimum time in Seconds

    private Configuration() { }

    public static int getMinimumTime() {
        return minimumTime;
    }

    public static void setMinimumTime(final int newMinimumTime) {
        Configuration.minimumTime = newMinimumTime;
    }
}
