package cat.uab.ds.core.utils;

import java.util.List;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public class PrintVisitor implements ActivitiyVisitor {

    private String result = "";

    public PrintVisitor() {
        this.result += "\nNom\t\t\tTemps Inici\t\t\t\tTemps final\t\t\t\tDurada (hh:mm:ss)";
        this.result += "\n----------+----------------------+-----------------------+------------------";
    }

    public String getResult() {
        return result;
    }

    @Override
    public void visitActivities(List<Activity> activities) {

    }

    @Override
    public void visitProject(Project p) {
        this.result = "\n"+p.getName()+"\t\t\tEMPTY\t\t\t\t\tEMPTY\t\t\t\t\t00:00:00";
    }

    @Override
    public void visitTask(Task task) {
        this.result = "\n"+task.getName()+"\t\t\tEMPTY\t\t\t\t\tEMPTY\t\t\t\t\t00:00:00";
    }

}
