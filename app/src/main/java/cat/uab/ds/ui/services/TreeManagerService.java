package cat.uab.ds.ui.services;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Configuration;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.utils.Clock;
import cat.uab.ds.core.utils.DetailedReportVisitor;
import cat.uab.ds.core.utils.ReportAscii;
import cat.uab.ds.core.utils.ReportFormat;
import cat.uab.ds.core.utils.ReportHTML;
import cat.uab.ds.core.utils.ReportVisitor;
import cat.uab.ds.core.utils.ShortReportVisitor;
import cat.uab.ds.ui.GenerateReportActivity;
import cat.uab.ds.ui.IntervalsActivity;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.adapters.IntervalHolder;

public class TreeManagerService extends Service implements Observer {

    //Constants
    public static final String TAG = "TreeManagerService";

    public static final String RECEIVE_CHILDREN = "ReceiveChildren";
    public static final String RECEIVE_INTERVALS = "ReceiveIntervals";

    private Receiver receiver;
    private Project root = new Project("root");
    private Project actual;
    private ArrayList<Task> pausedTask = new ArrayList<>();
    private int posIntervals = -1;

    @Override
    public void onCreate() {
        Log.d(TAG, "Created TreeManagerService");

        Configuration.setMinimumTime(2);
        Clock clock = Clock.getInstance();
        clock.addObserver(this);
        root.setRoot(true);
        actual = root;
        this.generateTree();

        //Receiver declaration
        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(MainActivity.DOWN_TREE);
        filter.addAction(MainActivity.UP_TREE);
        filter.addAction(MainActivity.CREATE_ACTIVITY);
        filter.addAction(MainActivity.REMOVE_ACTIVITY);
        filter.addAction(MainActivity.START_TASK);
        filter.addAction(MainActivity.STOP_TASK);
        filter.addAction(MainActivity.PAUSE_ALL);
        filter.addAction(MainActivity.RESUME_ALL);
        filter.addAction(IntervalsActivity.GET_INTERVALS);
        filter.addAction(IntervalsActivity.REMOVE_INTERVAL);
        filter.addAction(GenerateReportActivity.GENERATE_REPORT);
        registerReceiver(receiver, filter);

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand TreeManagerService");
        sendChilds();

        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    public void generateTree(){
        Project p1 = new Project("P1");
        p1.addActivity(new TaskBasic("T1"));
        root.addActivity(p1);
        root.addActivity(new Project("P2"));
        root.addActivity(new TaskBasic("TP"));
    }

    private void sendChilds(){
        Intent intent = new Intent(RECEIVE_CHILDREN);
        ArrayList<ActivityHolder> activities = new ArrayList<>();
        for(Activity activity: actual.getActivities()){
            activities.add(new ActivityHolder(activity));
        }
        intent.putExtra("childs", activities);
        intent.putExtra("isRoot", actual.isRoot());
        intent.putExtra("rootRunning", root.isRunning());
        intent.putExtra("isPaused", (pausedTask.size() > 0));
        sendBroadcast(intent);
    }

    private void sendIntervals(int pos){
        if(pos > -1){
            Intent intent = new Intent(RECEIVE_INTERVALS);
            ArrayList<IntervalHolder> intervals = new ArrayList<>();
            Task task = (Task) actual.getActivities().toArray()[pos];
            Log.d(TAG, "nIntervals "+task.getIntervals().size());
            for(Interval interval: task.getIntervals()){
                intervals.add(new IntervalHolder(interval));
            }

            intent.putExtra("activityData", new ActivityHolder(task));
            intent.putExtra("intervals", intervals);
            sendBroadcast(intent);
        }
    }

    private void removeInterval(int pos){
        if(pos > -1){
            Task task = (Task) actual.getActivities().toArray()[posIntervals];
            task.getIntervals().remove(pos);
        }

        sendIntervals(posIntervals);
    }

    @Override
    public void update(Observable o, Object arg) {
        //Log.d(TAG, "Updated Clock");
        sendChilds();
        sendIntervals(posIntervals);
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(MainActivity.DOWN_TREE)) {
                Log.d(TAG, "Open Activity ");
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    actual = (Project) activities.get(pos);
                }
            }
            else if (action.equals(MainActivity.UP_TREE)) {
                actual = (Project) actual.getParent();
            }
            else if (action.equals(MainActivity.CREATE_ACTIVITY)) {
                actual.addActivity((Activity) intent.getSerializableExtra("activity"));
            }
            else if (action.equals(MainActivity.REMOVE_ACTIVITY)) {
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    activities.remove(activities.get(pos));
                }
            }
            else if (action.equals(MainActivity.START_TASK)) {
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    Task task = (Task) activities.get(pos);
                    task.start();
                }
            }
            else if (action.equals(MainActivity.STOP_TASK)) {
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    Task task = (Task) activities.get(pos);
                    task.stop();
                }
            }
            else if (action.equals(MainActivity.PAUSE_ALL)) {
                pauseAll(root);
            }
            else if (action.equals(MainActivity.RESUME_ALL)) {
                resumeAll();
            }
            else if (action.equals(IntervalsActivity.GET_INTERVALS)) {
                posIntervals = intent.getIntExtra("taskPos", -1);
                sendIntervals(posIntervals);
            }
            else if (action.equals(IntervalsActivity.REMOVE_INTERVAL)) {
                int pos = intent.getIntExtra("pos", -1);
                removeInterval(pos);
            }else if(action.equals(GenerateReportActivity.GENERATE_REPORT)){
                int type = intent.getIntExtra("type", -1);
                int format = intent.getIntExtra("format", -1);
                Date from = (Date)intent.getSerializableExtra("from");
                Date to = (Date)intent.getSerializableExtra("to");

                generateReport(type, format, from, to);
            }

            sendChilds();
        }
    }

    public void pauseAll(Project root){
        for(Activity activity : root.getActivities()){
            if(activity.isTask() && activity.isRunning()){
                ((Task) activity).stop();
                pausedTask.add((Task) activity);
            }
            else if (activity.isProject()) {
                pauseAll((Project) activity);
            }
        }
    }

    public void resumeAll(){
        for(Task task : pausedTask){
            task.start();
        }
        pausedTask.clear();
    }


    public final void generateReport(int type, int format, final Date startDate, final Date endDate) {

        ReportFormat iFormat = null;
        switch (format){
            case 0:
                iFormat = new ReportAscii();
                break;
            case 1:
                iFormat = new ReportHTML();
                break;
        }
        String type_name = "";
        ReportVisitor iVisitor = null;
        switch (type){
            case 0:
                type_name = "short";
                iVisitor = new ShortReportVisitor(startDate, endDate, iFormat);
                break;
            case 1:
                type_name = "detailed";
                iVisitor = new DetailedReportVisitor(startDate, endDate, iFormat);
                break;
        }

        this.root.accept(iVisitor);
        String res = iVisitor.generate();

        File outputDir =   Environment.getExternalStoragePublicDirectory("/TimeTracker/Reports");
        outputDir.mkdirs();

        File outputFile = null;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

        try {
            switch (format){
                case 0:
                    outputFile = new File(outputDir, "report-" + type_name + "_" + df.format(cal.getTime()) + ".txt");
                    break;
                case 1:
                    outputFile = new File(outputDir, "report-" + type_name + "_"+ df.format(cal.getTime()) +".html");
                    break;
            }

            FileOutputStream stream = new FileOutputStream(outputFile);
            stream.write(res.getBytes());
            stream.close();

            Toast.makeText(getApplicationContext(), "Report generated in " + outputDir.getPath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
