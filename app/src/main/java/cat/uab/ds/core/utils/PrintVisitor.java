package cat.uab.ds.core.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public class PrintVisitor implements ActivitiyVisitor {

    private static SimpleDateFormat format = new SimpleDateFormat("uu-MMM-YYYY HH:mm:ss");
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
     * @param project
     */
    @Override
    public void visitProject(Project project) {
        Date start = project.getStart();
        Date end = project.getEnd();
        int duration = project.getDuration();
        String startStr = "\t\t\t\t\t";
        String endStr = "\t\t\t\t\t";

        if(start != null)
            startStr = format.format(start);

        if(end != null)
            endStr = format.format(end);

        this.result += "\n"+project.getName()
                +"\t\t\t"+startStr
                +"\t"+endStr
                +"\t"+this.durationToStr(duration);
    }

    /**
     * Generate String with task info
     * @param task
     */
    @Override
    public void visitTask(Task task) {
        Date start = task.getStart();
        Date end = task.getEnd();
        int duration = task.getDuration();
        String startStr = "\t\t\t\t\t";
        String endStr = "\t\t\t\t\t";

        if(start != null)
            startStr = format.format(start);

        if(end != null)
            endStr = format.format(end);

        this.result += "\n"+task.getName()+"\t\t\t"
                +startStr+"\t"
                +endStr+"\t"
                +this.durationToStr(duration);
    }

    private String durationToStr(int milis){
        long original = Math.round(milis/1000.0);
        long hours = original/60/60;
        long minutes = original/60;
        long seconds = original%60;

        return milis+"";
        //return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

}
