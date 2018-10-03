package cat.uab.ds;

import java.util.ArrayList;
import java.util.List;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.utils.Clock;
import cat.uab.ds.core.utils.PrintVisitor;

/**
 * Test Client class
 */
public class Client {

    public static void main(String args[]){

        Configuration.MIN_TIME = 2000;

        TimeTracker tt = new TimeTracker();
        Clock clock = Clock.getInstance();

        Project root = new Project("P1");
        root.addActivity(new TaskBasic("T3"));

        Project pr2 = new Project("P2");
        pr2.addActivity(new TaskBasic("T1"));
        pr2.addActivity(new TaskBasic("T2"));

        root.addActivity(pr2);

        List<Activity> aa = new ArrayList<>();
        aa.add(root);

        PrintVisitor print = new PrintVisitor();
        root.aceptar(print);
        System.out.println(print.getResult());
        //tt.startTimeTracker();

    }

}
