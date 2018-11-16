package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in
 * console.
 */
public class PrintVisitor implements ActivityVisitor {

    private static final int POS_START = 12;
    private static final int POS_END = 35;
    private static final int POS_DURATION = 60;
    private static final int MINUTE = 60;

    private static final SimpleDateFormat FORMAT =
            new SimpleDateFormat("dd/MM/YYYY HH:mm:ss",
                    new Locale("en"));
    private String result;


    /**
     * Initialize basic menu info with table header.
     */
    public PrintVisitor() {
        this.result = "";
        this.result += "\nName\t\t\tStart Time\t\t\t\tEnd time\t\t\t\t"
                + "Duration (hh:mm:ss)";
        this.result += "\n----------+----------------------+-----------------"
                + "------+------------------";
    }

    /**
     * Result of PrintVisitor.
     * @return String with result
     */
    public String getResult() {
        return result;
    }

    /**
     * Generate String with activity info (Name, Start, End and Duration).
     * @param activity Project or Task to print
     */
    private void generateActivity(final Activity activity) {
        Date start = activity.getStart();
        Date end = activity.getEnd();
        int duration = activity.getDuration();

        StringBuilder sb = new StringBuilder("                               "
                + "                                             ");

        insertInLine(sb, 0, levelLineStr(activity.getLevel())
                + activity.getName());

        if (start != null) {
            insertInLine(sb, POS_START, FORMAT.format(start));
        }
        if (end != null) {
            insertInLine(sb, POS_END, FORMAT.format(end));
        }
        insertInLine(sb, POS_DURATION, this.durationToStr(duration));

        this.result += "\n" + sb.toString();
    }

    /**
     * Converts a number in milliseconds to readable duration string (Hours,
     * Minutes and Seconds).
     * @param time Duration of activity in milliseconds
     * @return Duration string
     */
    private String durationToStr(final int time) {
        long hours = time / MINUTE / MINUTE;
        long minutes = time / MINUTE;
        long seconds = time % MINUTE;

        return String.format(new Locale("en"),
                "%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Inserts string in StringBuilder line.
     * @param sb StringBuilder instance
     * @param pos Position to insert
     * @param word String to insert
     */
    private void insertInLine(final StringBuilder sb, final int pos,
                              final String word) {
        sb.replace(pos, pos + word.length(), word);
    }

    /**
     * Generates the characters for the tree hierarchy.
     * @param level The level of activity (Task or Project)
     * @return String with first characters of activity print
     */
    private String levelLineStr(final int level) {
        StringBuilder str = new StringBuilder();
        for (int i = 1; i < level; i++) {
            if (i == level - 1) {
                str.append("└");
            } else {
                str.append(" ");
            }
        }
        return str.toString();
    }

    /**
     * Visit the project.
     * @param project The project
     */
    @Override
    public final void visit(final Project project) {
        if (!project.isRoot()) {
            generateActivity(project);
        }

        for (Activity activity : project.getActivities()) {
            activity.accept(this);
        }
    }

    @Override
    public final void visit(final Task task) {
        generateActivity(task);
    }

    @Override
    public void visit(final Interval interval) {

    }
}
