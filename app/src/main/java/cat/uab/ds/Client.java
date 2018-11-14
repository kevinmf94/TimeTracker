package cat.uab.ds;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

import cat.uab.ds.core.TimeTracker;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.utils.Clock;

/**
 * Test Client class.
 */
final class Client {

    private Client() { }

    public static void main(final String[] args) {

        //provesFita1();
        TimeTracker tt = provesFita2();
        //TimeTracker tt = new TimeTracker();
        //tt.load("data.dat");

    }

    private static TimeTracker provesFita2() {

        Logger log = LoggerFactory.getLogger("cat.uab.ds.Client");
        log.info("JAJA");

        Configuration.setMinimumTime(2);
        TimeTracker timeTracker = new TimeTracker();
        Date start;
        Date end;


        Project p1 = new Project("P1");
        Project p12 = new Project("P1.2");
        Project p2 = new Project("P2");

        Task t1 = new TaskBasic("T1");
        Task t2 = new TaskBasic("T2");
        Task t3 = new TaskBasic("T3");
        Task t4 = new TaskBasic("T4");

        //Add projects P1, P2 and subprojects
        timeTracker.addProject(p1);
        timeTracker.addProject(p2);

        p1.addActivity(t1);
        p1.addActivity(t2);
        p1.addActivity(p12);
        p12.addActivity(t4);

        p2.addActivity(t3);

        //Time
        start = new Date();
        t1.start();
        t4.start();
        wait(4000);

        t1.stop();
        t2.start();
        wait(6000);
        t2.stop();
        t4.stop();
        t3.start();
        wait(4000);
        t3.stop();
        t2.start();

        wait(2000);
        t3.start();
        wait(4000);
        t3.stop();
        t2.stop();

        end = new Date();

        timeTracker.save("data.dat");

        wait(1000);
        Clock.getInstance().deleteObserver(timeTracker);

        //timeTracker.generateDetailedReportAscii(start, end);
        //timeTracker.generateShortReportAscii(start, end);
        //timeTracker.generateDetailedReportHTML(start, end);
        //timeTracker.generateShortReportHTML(start, end);

        return timeTracker;
    }

    private static void provesFita1() {
        Configuration.setMinimumTime(2);

        TimeTracker timeTracker = new TimeTracker();

        /*Project pr1 = new Project("P1");
        timeTracker.addProject(pr1);

        Task task3 = new TaskBasic("T3");
        pr1.addActivity(task3);

        Project pr2 = new Project("P2");
        pr1.addActivity(pr2);

        Task task1 = new TaskBasic("T1");
        Task task2 = new TaskBasic("T2");
        pr2.addActivity(task1);
        pr2.addActivity(task2);*/

        //Test Sequential
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

        //Test simultaneous
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
        timeTracker.load("data.dat");
        //timeTracker.save("data.dat");

        //Test Decorator
        //Date now = new Date();
        //task1 = new TaskScheduled(task1, new Date(now.getTime()+4000));
        //task1 = new TaskLimited(task1, 8);


        //Task task4 = new TaskBasic("T4");
        //Date now = new Date();
        //task4 = new TaskScheduled(task4, new Date(now.getTime()+4000));
        //task4 = new TaskLimited(task4,4);

        //task4 = TaskScheduled
        //((TaskDecorator) task4).removeComponent(TaskScheduled.class);
        //pr2.addActivity(task4);
        //task4.start();*/
    }

    private static void wait(final int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
