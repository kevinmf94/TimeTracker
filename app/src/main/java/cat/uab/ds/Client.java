package cat.uab.ds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.utils.Clock;
import cat.uab.ds.core.utils.PrintVisitor;

/**
 * Test Client class
 */
public class Client {

    public static void main(String args[]) throws IOException {

        Configuration.MIN_TIME = 2000;

        TimeTracker tt = new TimeTracker();

        Project root = new Project("P1");
        root.addActivity(new TaskBasic("T3"));

        Project pr2 = new Project("P2");
        pr2.addActivity(new TaskBasic("T1"));
        pr2.addActivity(new TaskBasic("T2"));

        root.addActivity(pr2);

        //Save Serializable
        /*FileOutputStream out = new FileOutputStream("save.dat");
        ObjectOutputStream outob = new ObjectOutputStream(out);
        outob.writeObject(root);*/

        tt.addProject(root);

    }

}
