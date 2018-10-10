package cat.uab.ds;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;
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

        Project pr1 = new Project("P1");
        tt.addProject(pr1);

        final Task task3 = new TaskBasic("T3");
        pr1.addActivity(task3);

        Project pr2 = new Project("P2");
        pr1.addActivity(pr2);

        final Task task1 = new TaskBasic("T1");
        final Task task2 = new TaskBasic("T2");
        pr2.addActivity(task1);
        pr2.addActivity(task2);

        //Save Serializable
        /*FileOutputStream out = new FileOutputStream("save.dat");
        ObjectOutputStream outob = new ObjectOutputStream(out);
        outob.writeObject(root);*/



        task3.start();
        wait(4000);
        task3.stop();
        wait(2000);
        task3.start();
        wait(2000);
        task3.stop();
        /*task3.start();
        wait(3000);
        task3.stop();
        wait(7000);
        task2.start();
        wait(10000);
        task2.stop();
        task3.start();
        wait(2000);
        task3.stop();*/

    }

    private static void wait(int milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
