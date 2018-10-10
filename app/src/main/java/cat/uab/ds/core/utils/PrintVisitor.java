package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public class PrintVisitor implements ActivitiyVisitor {

    private static SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY HH:mm:ss");
    private String result = "";

    /**
     * Initialize basic menu info
     */
    public PrintVisitor() {
        this.result += "\nNom\t\t\tTemps Inici\t\t\t\tTemps final\t\t\t\tDurada (hh:mm:ss)";
        this.result += "\n----------+----------------------+-----------------------+------------------";
    }

    public String getResult() {
        return result;
    }

    /**
     * Generate String with project info
     * @param activity
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

    private String durationToStr(int milis){
        long original = Math.round(milis/1000.0);
        long hours = original/60/60;
        long minutes = original/60;
        long seconds = original%60;

        //return milis+"";
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private void insertInLine(StringBuilder sb, int pos, String word){
        sb.replace(pos,pos+word.length(), word);
    }

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
