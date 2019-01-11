package cat.uab.ds.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.Locale;

import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.fragments.ActivitiesListFragment;
import cat.uab.ds.ui.fragments.SplashFragment;
import cat.uab.ds.ui.services.TreeManagerService;

/**
 * The MainActivity of TimeTracker app.
 */
public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    //Constants
    private final static String TAG = "MainActivity";

    public final static String DOWN_TREE = "DownTree";
    public final static String UP_TREE = "UpTree";
    public final static String CREATE_ACTIVITY = "CreateActivity";
    public final static String REMOVE_ACTIVITY = "RemoveActivity";
    public final static String START_TASK = "StartTask";
    public final static String STOP_TASK = "StopTask";
    public final static String PAUSE_ALL = "PauseAll";
    public final static String RESUME_ALL = "ResumeAll";

    public final static int REQUEST_PROJECT = 0;
    public final static int REQUEST_TASK = 1;

    //UI and Fragments
    private FloatingActionButton floatingActionButton;
    private ActivitiesListFragment activitiesListFragment;
    private MenuItem playPauseItem;

    private Receiver receiver;
    private boolean isRoot = true;
    private boolean rootRunning = false;
    private boolean isPaused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.addBtn);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addActivity(v);
            }
        });

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFragment, new SplashFragment());
        transaction.commit();
    }

    public void loadActivity(){
        activitiesListFragment = new ActivitiesListFragment();
        receiver = new Receiver();

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFragment, activitiesListFragment);
        transaction.commit();

        startService(new Intent(this, TreeManagerService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.RECEIVE_CHILDREN);
        registerReceiver(receiver, filter);
        floatingActionButton.show();
    }

    /**
     * Function called when AddButton is clicked.
     * Shows an AlertDialog to ask user to Create Project or Task.
     * @param view The button view
     */
    public void addActivity(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectCreateTitle)
                .setItems(R.array.selectCreate, this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Function called when an option of the AlertDialog is selected.
     * @param dialog AlertDialog
     * @param which Option selected
     */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "Escogido: "+which);

        Intent intent;

        switch (which){
            case 0: //Add Project
                intent = new Intent(this, AddProjectActivity.class);
                startActivityForResult(intent, REQUEST_PROJECT);
                break;
            case 1: //Add task
                intent = new Intent(this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_TASK);
                break;
        }

    }

    /**
     * Callback function when Create Project or Create Task Activity ends.
     * @param requestCode Activity start request code
     * @param resultCode The result code of activity when ends
     * @param data Intent from activity
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            switch (requestCode){
                case REQUEST_PROJECT:
                case REQUEST_TASK:
                    data.setAction(CREATE_ACTIVITY);
                    sendBroadcast(data);
                    break;
            }
        }
    }

    /**
     * Handler for hardware back button click.
     */
    @Override
    public void onBackPressed() {
        if(isRoot){
            if(rootRunning){ // If there are any running task, show Alert to confirm exit.
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.exitTitle)
                        .setMessage(R.string.exitMessage)
                        .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            sendBroadcast(new Intent(PAUSE_ALL));
                            MainActivity.super.onBackPressed();
                            }
                        }).
                        setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                super.onBackPressed();
            }
        } else {
            Log.d(TAG, "OnBackPressed");
            sendBroadcast(new Intent(MainActivity.UP_TREE));
        }
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        super.onDestroy();
    }

    /**
     * Fill OptionsMenu from ActionBar
     * @param menu The Menu
     * @return Boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu, menu);
        playPauseItem = menu.findItem(R.id.playPauseAll);
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handler function called when an ActionBar Menu Option is clicked.
     * @param item MenuItem that has been clicked
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.playPauseAll: //Pause all button is clicked
                if(isPaused){   // If is paused, send intent to resume all task
                    item.setIcon(R.drawable.ic_pause_white);
                    sendBroadcast(new Intent(RESUME_ALL));
                } else {    // Send Intent to pause all running tasks
                    item.setIcon(R.drawable.ic_play_white);
                    sendBroadcast(new Intent(PAUSE_ALL));
                }
                break;
            case R.id.generateReport: // Option to load GenerateReportActivity
                Intent intent = new Intent(this, GenerateReportActivity.class);
                startActivityForResult(intent, REQUEST_PROJECT);
                break;
            case R.id.changeLanguage: // Option to change the actual language. Show AlertDialog and the available languages.

                //Languages list
                String[] langs = {
                        "English",
                        "Español",
                        "Català"
                };

                AlertDialog.Builder langsDialogBld = new AlertDialog.Builder(this);
                langsDialogBld.setTitle(R.string.selectOption)
                        .setItems(langs, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case 0: setLocale("en"); break;
                                    case 1: setLocale("es"); break;
                                    case 2: setLocale("ca"); break;
                                }
                            }
                        });
                AlertDialog langsDialog = langsDialogBld.create();
                langsDialog.show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Receiver class of broadcast intents.
     * In this case, receives the Projects/Tasks list from the service.
     */
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.RECEIVE_CHILDREN)) { // Update projects/tasks list in view
                Log.d(TAG, "Receive Childs");
                isRoot = intent.getBooleanExtra("isRoot", true);
                rootRunning = intent.getBooleanExtra("rootRunning", false);
                isPaused = intent.getBooleanExtra("isPaused" , false);
                ArrayList<ActivityHolder> activities = (ArrayList<ActivityHolder>) intent.getSerializableExtra("childs");

                playPauseItem.setVisible((rootRunning && !isPaused) || (isPaused && !rootRunning));
                activitiesListFragment.updateData(activities);
            }
        }
    }

    /**
     * This function changes the language configuration and refresh the MainActivity
     * @param lang The new language
     */
    public void setLocale(String lang) {
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        Intent refresh = new Intent(this, MainActivity.class);
        startActivity(refresh);
        finish();
    }
}
