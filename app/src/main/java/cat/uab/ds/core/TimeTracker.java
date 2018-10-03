package cat.uab.ds.core;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.utils.Clock;

public class TimeTracker implements Observer {

    Clock clock;
    private ArrayList<Activity> activities = new ArrayList<>();

    public TimeTracker() {
        clock = Clock.newInstance();
        clock.addObserver(this);
    }

    public void addProject(Project project){
        this.activities.add(project);
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

    @Override
    public void update(Observable observable, Object o) {
        //System.out.println((Date)o);
        printMenu();
    }
}
