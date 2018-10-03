package cat.uab.ds.core.utils;

import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public class PrintVisitor implements ActivitiyVisitor {

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
        this.result += "\n"+project.getName()+"\t\t\tEMPTY\t\t\t\t\tEMPTY\t\t\t\t\t00:00:00";
    }

    /**
     * Generate String with task info
     * @param task
     */
    @Override
    public void visitTask(Task task) {
        this.result += "\n"+task.getName()+"\t\t\tEMPTY\t\t\t\t\tEMPTY\t\t\t\t\t00:00:00";
    }

}
