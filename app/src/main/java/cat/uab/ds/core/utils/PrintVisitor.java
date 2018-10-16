package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;

/**
 * Visitor in charge of read an Activity (Task or Project) and print it in console
 */
public class PrintVisitor implements ActivitiyVisitor {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
    private String result = "";

    /**
     * Initialize basic menu info with table header.
     */
    public PrintVisitor() {
        this.result += "\nNom\t\t\tTemps Inici\t\t\t\tTemps final\t\t\t\tDurada (hh:mm:ss)";
        this.result += "\n----------+----------------------+-----------------------+------------------";
    }

    /**
     * Result of PrintVisitor
     * @return String with result
     */
    public String getResult() {
        return result;
    }

    /**
     * Generate String with activity info (Name, Start, End and Duration)
     * @param activity Project or Task to print
     */
    @Override
    public void visitActivity(Activity activity) {
        Date start = activity.getStart();
        Date end = activity.getEnd();
        int duration = activity.getDuration();

        StringBuilder sb = new StringBuilder("                                                                            ");

        insertInLine(sb, 0, LevelLineStr(activity.getLevel()) + activity.getName());

        if(start != null)
            insertInLine(sb,12, format.format(start));

        if(end != null)
            insertInLine(sb,35,format.format(end));

        insertInLine(sb,60, this.durationToStr(duration));

        this.result += "\n"+sb.toString();
    }

    /**
     * Converts a number in miliseconds to readable duration string (Hours, Minutes and Seconds)
     * @param time Duration of activity in milliseconds
     * @return Duration string
     */
    private String durationToStr(int time){
        long hours = time/60/60;
        long minutes = time/60;
        long seconds = time%60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    /**
     * Inserts string in StringBuilder line.
     * @param sb StringBuilder instance
     * @param pos Position to insert
     * @param word String to insert
     */
    private void insertInLine(StringBuilder sb, int pos, String word){
        sb.replace(pos,pos+word.length(), word);
    }

    /**
     * Generates the characters for the tree hierarchy
     * @param level The level of activity (Task or Project)
     * @return String with first characters of activity print
     */
    private String LevelLineStr(int level){
        String str = "";
        for (int i = 1; i<level;i++){
            if(i == level-1){
                str += "└";
            }else{
                str += " ";
            }
        }
        return  str;
    }
}
