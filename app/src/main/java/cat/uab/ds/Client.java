package cat.uab.ds;

import java.io.IOException;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.entity.TaskDecorator;
import cat.uab.ds.core.entity.TaskLimited;
import cat.uab.ds.core.entity.TaskScheduled;
import java.util.Date;

/**
 * Test Client class
 */
public class Client {

    public static TimeTracker timeTracker;

    public static void main(String args[]) throws IOException {

        Configuration.MIN_TIME = 2;

        timeTracker = new TimeTracker();

        Project pr1 = new Project("P1");
        timeTracker.addProject(pr1);

        Task task3 = new TaskBasic("T3");
        pr1.addActivity(task3);

        Project pr2 = new Project("P2");
        pr1.addActivity(pr2);

        Task task1 = new TaskBasic("T1");
        Task task2 = new TaskBasic("T2");
        pr2.addActivity(task1);
        pr2.addActivity(task2);

        //Test Secuencial
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

        //Test simultani
        /*task3.start();
        wait(4000);
        task2.start();
        wait(2000);
        task3.stop();
        wait(2000);
        task1.start();
        wait(4000);
        task1.stop();
        wait(2000);
        task2.stop();
        wait(4000);
        task3.start();
        wait(2000);
        task3.stop();*/

        //Save/Load Test
        //timeTracker.load("data.dat");
        //timeTracker.save("data.dat");

        //Test Decorator
        Date now = new Date();
        task1 = new TaskScheduled(task1, new Date(now.getTime()+4000));
        task1 = new TaskLimited(task1, 8);


        //Task task4 = new TaskBasic("T4");
        //Date now = new Date();
        //task4 = new TaskScheduled(task4, new Date(now.getTime()+4000));
        //task4 = new TaskLimited(task4,4);

        //task4 = TaskScheduled
        //((TaskDecorator) task4).removeComponent(TaskScheduled.class);
        //pr2.addActivity(task4);
        //task4.start();*/




    }

    private static void wait(int milis){
        try {
            Thread.sleep(milis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
