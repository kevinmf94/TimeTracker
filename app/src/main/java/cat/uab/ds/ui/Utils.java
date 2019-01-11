package cat.uab.ds.ui;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import cat.uab.ds.core.entity.Activity;

public class Utils {
    public static DateFormat dfDateTime = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static DateFormat dfDateTimeSeconds = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static DateFormat dfDate = new SimpleDateFormat("dd/MM/yyyy");

    /**
     * Converts a number in milliseconds to readable duration string (Hours,
     * Minutes and Seconds).
     * @param time Duration of activity in milliseconds
     * @return Duration string
     */
    public static String durationToStr(final int time) {
        long hours = time / 60 / 60;
        long minutes = time / 60;
        long seconds = time % 60;

        return String.format(new Locale("en"),
                "%dh %dm %ds", hours, minutes, seconds);
    }

    public static String MakeBreadCrumb(Activity activity){
        String breadcrumb = "";
        Activity actual = activity;
        while(actual.getLevel() != 0){
            breadcrumb = "> " + actual.getName() + breadcrumb;
            actual = actual.getParent();
        }

        return breadcrumb;
    }
}
