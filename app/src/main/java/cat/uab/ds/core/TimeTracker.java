package cat.uab.ds.core;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;

public class TimeTracker {

    private ArrayList<Activity> activities = new ArrayList<>();
    private Timer timer;

    public TimeTracker() {}

    public void addProject(Project project){
        this.activities.add(project);
    }

    public void startTimeTracker() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                printMenu();
            }
        }, 0, Configuration.MIN_TIME);
    }

    private void printMenu(){
        System.out.print("\033[H\033[2J");
        System.out.flush();
        System.out.println("\nNom\t\t\tTemps Inici\t\t\t\tTemps final\t\t\t\tDurada (hh:mm:ss)");
        System.out.println("----------+----------------------+-----------------------+------------------");
        printActivities(this.activities);
    }

    private void printActivities(List<Activity> activities) {
        for (Activity activity: activities) {
            System.out.println(activity.getName()+"\t\t\tEMPTY\t\t\t\t\tEMPTY\t\t\t\t\t00:00:00");

            if(activity instanceof Project)
                this.printActivities(((Project) activity).getActivities());
        }
    }
}
