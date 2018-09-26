package cat.uab.ds;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.TaskBasic;

/**
 * Test Client class
 */
public class Client {

    public static void main(String args[]){

        TimeTracker tt = new TimeTracker();

        Project root = new Project("P1");
        root.addActivity(new TaskBasic("T3"));

        Project pr2 = new Project("P2");
        pr2.addActivity(new TaskBasic("T1"));
        pr2.addActivity(new TaskBasic("T2"));

        root.addActivity(pr2);

        tt.setRoot(root);

    }

}
