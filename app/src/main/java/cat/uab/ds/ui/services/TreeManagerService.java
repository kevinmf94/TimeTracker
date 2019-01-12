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
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import cat.uab.ds.comparators.SortActivityByDate;
import cat.uab.ds.comparators.SortActivityByDuration;
import cat.uab.ds.comparators.SortActivityByName;
import cat.uab.ds.comparators.SortIntervalByDate;
import cat.uab.ds.comparators.SortIntervalByDuration;
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
import cat.uab.ds.ui.EditProjectActivity;
import cat.uab.ds.ui.EditTaskActivity;
import cat.uab.ds.ui.GenerateReportActivity;
import cat.uab.ds.ui.IntervalsActivity;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.adapters.IntervalHolder;

/**
 * The Manager Class to store the data tree with all Projects, Tasks and Intervals.
 * Is in charge of send data (in list), add elements, remove elements, pause tasks, resume tasks, generate reports...
 */
public class TreeManagerService extends Service implements Observer {

    //Constants
    public static final String TAG = "TreeManagerService";

    public static final String RECEIVE_CHILDREN = "ReceiveChildren";
    public static final String RECEIVE_INTERVALS = "ReceiveIntervals";
    public static final String SEND_CHILD = "SendChild";
    public static final String SORT = "Sort";

    private Receiver receiver;
    private Project root = new Project("root");
    private Project actual;
    private ArrayList<Task> pausedTask = new ArrayList<>();
    private int posIntervals = -1;

    Comparator<Activity> compActivities = null;
    Comparator<Interval> compIntervals = null;

    @Override
    public void onCreate() {
        Log.d(TAG, "Created TreeManagerService");

        Configuration.setMinimumTime(1);
        Clock clock = Clock.getInstance();
        clock.addObserver(this);
        root.setRoot(true);
        actual = root;
        this.generateTree(); // Generate dummy tree

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
        filter.addAction(EditProjectActivity.GET_CHILD);
        filter.addAction(EditProjectActivity.UPDATE_PROJECT);
        filter.addAction(EditTaskActivity.GET_CHILD);
        filter.addAction(EditTaskActivity.UPDATE_TASK);
        filter.addAction(SORT);
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

    /**
     * Function to generate dummy data tree
     */
    public void generateTree(){
        Project p1 = new Project("P1");
        p1.addActivity(new TaskBasic("T1"));
        root.addActivity(p1);
        root.addActivity(new Project("P2"));
        root.addActivity(new TaskBasic("TP"));
    }

    /**
     * Fucntion to send a list of projects/tasks to the receivers. With comparator
     */
    private void sendChilds(){
        Intent intent = new Intent(RECEIVE_CHILDREN);
        ArrayList<ActivityHolder> holders = new ArrayList<>();

        ArrayList<Activity> activities = (ArrayList)actual.getActivities();

        if(compActivities != null){
            Collections.sort(activities, compActivities);
        }

        for(Activity activity: actual.getActivities()){
            holders.add(new ActivityHolder(activity));
        }
        intent.putExtra("childs", holders);
        intent.putExtra("isRoot", actual.isRoot());
        intent.putExtra("rootRunning", root.isRunning());
        intent.putExtra("isPaused", (pausedTask.size() > 0));
        intent.putExtra("parent", actual);
        sendBroadcast(intent);
    }

    /**
     * Fucntion to send an intervals list to the receivers. With comparator
     */
    private void sendIntervals(int pos){
        if(pos > -1){
            Intent intent = new Intent(RECEIVE_INTERVALS);
            ArrayList<IntervalHolder> holders = new ArrayList<>();
            Task task = (Task) actual.getActivities().toArray()[pos];
            Log.d(TAG, "nIntervals "+task.getIntervals().size());

            ArrayList<Interval> intervals = task.getIntervals();

            if(compIntervals != null){
                Collections.sort(intervals, compIntervals);
            }

            for(Interval interval: intervals){
                holders.add(new IntervalHolder(interval));
            }

            intent.putExtra("activityData", new ActivityHolder(task));
            intent.putExtra("parent", task);
            intent.putExtra("intervals", holders);
            sendBroadcast(intent);
        }
    }

    /**
     * Removes an interval of the tree.
     * @param pos
     */
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

    /**
     * Receiver class of broadcast intents.
     * In this case, receive the action and data of the others activities requests.
     */
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(MainActivity.DOWN_TREE)) { // Get down in tree in specific child
                Log.d(TAG, "Open Activity ");
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    actual = (Project) activities.get(pos);
                }
            }
            else if (action.equals(MainActivity.UP_TREE)) { // Get up in tree
                actual = (Project) actual.getParent();
            }
            else if (action.equals(MainActivity.CREATE_ACTIVITY)) { // Add activity
                actual.addActivity((Activity) intent.getSerializableExtra("activity"));
            }
            else if (action.equals(MainActivity.REMOVE_ACTIVITY)) { // Remove activity
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    activities.remove(activities.get(pos));
                }
            }
            else if (action.equals(MainActivity.START_TASK)) { // Start running task
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    Task task = (Task) activities.get(pos);
                    task.start();
                }
            }
            else if (action.equals(MainActivity.STOP_TASK)) { // Stop running task
                int pos = intent.getIntExtra("pos", -1);
                if (pos > -1) {
                    ArrayList<Activity> activities =
                            (ArrayList<Activity>) actual.getActivities();
                    Task task = (Task) activities.get(pos);
                    task.stop();
                }
            }
            else if (action.equals(MainActivity.PAUSE_ALL)) { // Pause all
                pauseAll(root);
            }
            else if (action.equals(MainActivity.RESUME_ALL)) {// Resume all
                resumeAll();
            }
            else if (action.equals(IntervalsActivity.GET_INTERVALS)) { // Send intervals
                posIntervals = intent.getIntExtra("taskPos", -1);
                sendIntervals(posIntervals);
            }
            else if (action.equals(IntervalsActivity.REMOVE_INTERVAL)) { // Remove interval
                int pos = intent.getIntExtra("pos", -1);
                removeInterval(pos);
            }else if(action.equals(GenerateReportActivity.GENERATE_REPORT)){ // Generate report
                int type = intent.getIntExtra("type", -1);
                int format = intent.getIntExtra("format", -1);
                Date from = (Date)intent.getSerializableExtra("from");
                Date to = (Date)intent.getSerializableExtra("to");

                generateReport(type, format, from, to);
            } else if(action.equals(EditProjectActivity.GET_CHILD) || action.equals(EditTaskActivity.GET_CHILD)){

                int pos = intent.getIntExtra("pos", -1);
                if(pos == -1)
                    return;

                Log.d(TAG, "GetChild "+pos);
                ArrayList<Activity> activities =
                        (ArrayList<Activity>) actual.getActivities();
                Intent send = new Intent(SEND_CHILD);
                send.putExtra("activity", activities.get(pos));
                sendBroadcast(send);
            } else if(action.equals(EditProjectActivity.UPDATE_PROJECT)){
                int pos = intent.getIntExtra("pos", -1);
                if(pos == -1)
                    return;

                Log.d(TAG, "Update project child "+pos);
                String name = intent.getStringExtra("name");
                String description = intent.getStringExtra("description");

                ArrayList<Activity> activities =
                        (ArrayList<Activity>) actual.getActivities();
                Activity activity = activities.get(pos);
                activity.setName(name);
                activity.setDescription(description);

            } else if(action.equals(EditTaskActivity.UPDATE_TASK)){
                int pos = intent.getIntExtra("pos", -1);
                if(pos == -1)
                    return;

                Log.d(TAG, "Update task child "+pos);
                String name = intent.getStringExtra("name");
                String description = intent.getStringExtra("description");

                ArrayList<Activity> activities =
                        (ArrayList<Activity>) actual.getActivities();
                Activity activity = activities.get(pos);
                activity.setName(name);
                activity.setDescription(description);
            } else if(action.equals(SORT)){ // Sort and send activities/intervals
                String by = intent.getStringExtra("by");

                switch (by){
                    case "date":
                        compActivities = new SortActivityByDate();
                        compIntervals = new SortIntervalByDate();
                        break;
                    case "name":
                        compActivities = new SortActivityByName();
                        compIntervals = null;
                        break;
                    case "duration":
                        compActivities = new SortActivityByDuration();
                        compIntervals = new SortIntervalByDuration();
                        break;
                }
            }

            sendChilds();
        }
    }

    /**
     * Recursive function to pause all activities running in a tree
     * @param root The root project of the tree
     */
    public void pauseAll(Project root){
        for(Activity activity : root.getActivities()){
            if(activity.isTask() && activity.isRunning()){ // if is running task
                ((Task) activity).stop();
                pausedTask.add((Task) activity);
            }
            else if (activity.isProject()) { // if is project
                pauseAll((Project) activity); // Recursive call
            }
        }
    }

    /**
     * Function to resume all paused tasks
     */
    public void resumeAll(){
        for(Task task : pausedTask){
            task.start();
        }
        pausedTask.clear();
    }

    /**
     * Function to generate report with in specified format.
     * Generates the file and writes it on device storage.
     * Then send an intent to open the generated file with another reader app.
     * @param type The type of the report (Short/Detailed)
     * @param format The format of the report (Ascii/HTML)
     * @param startDate Start date of the report data
     * @param endDate End date of the report data
     */
    public final void generateReport(int type, int format, final Date startDate, final Date endDate) {

        ReportFormat iFormat = null;
        switch (format){
            case 0: // Ascii
                iFormat = new ReportAscii();
                break;
            case 1: // HTML
                iFormat = new ReportHTML();
                break;
        }
        String type_name = "";
        ReportVisitor iVisitor = null;
        switch (type){
            case 0: // Short
                type_name = "short";
                iVisitor = new ShortReportVisitor(startDate, endDate, iFormat);
                break;
            case 1: // Detailed
                type_name = "detailed";
                iVisitor = new DetailedReportVisitor(startDate, endDate, iFormat);
                break;
        }

        //Visit all tree
        this.root.accept(iVisitor);

        String result = iVisitor.generate();

        //Output file directory
        File outputDir =   Environment.getExternalStoragePublicDirectory("/TimeTracker/Reports");
        outputDir.mkdirs(); // if not exists

        File outputFile = null;

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss");

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        //Write file in storage
        try {
            switch (format){
                case 0:
                    outputFile = new File(outputDir, "report-" + type_name + "_" + df.format(cal.getTime()) + ".txt");

                    Uri apkURI = FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext()
                                    .getPackageName() + ".provider", outputFile);
                    intent.setDataAndType(apkURI, "text/plain");

                    break;
                case 1:
                    outputFile = new File(outputDir, "report-" + type_name + "_"+ df.format(cal.getTime()) +".html");

                    Uri apkURI2 = FileProvider.getUriForFile(
                            getApplicationContext(),
                            getApplicationContext()
                                    .getPackageName() + ".provider", outputFile);
                    intent.setDataAndType(apkURI2, "text/html");

                    break;
            }

            FileOutputStream stream = new FileOutputStream(outputFile);
            stream.write(result.getBytes());
            stream.close();

            //Toast.makeText(getApplicationContext(), "Report generated in " + outputDir.getPath(), Toast.LENGTH_LONG).show();

        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getApplicationContext().startActivity(intent); // Intent to open the generated file (with another app)

    }
}
