package cat.uab.ds.ui;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.fragments.ListFragment;
import cat.uab.ds.ui.fragments.SplashFragment;
import cat.uab.ds.ui.services.TreeManagerService;

public class MainActivity extends AppCompatActivity implements DialogInterface.OnClickListener {

    //Constants
    private final static String TAG = "MainActivity";

    public final static String DOWN_TREE = "DownTree";
    public final static String UP_TREE = "UpTree";
    public final static String CREATE_ACTIVITY = "CreateActivity";
    public final static String REMOVE_ACTIVITY = "RemoveActivity";
    public final static String START_TASK = "StartTask";
    public final static String STOP_TASK = "StopTask";

    public final static int REQUEST_PROJECT = 0;
    public final static int REQUEST_TASK = 1;

    //UI and Fragments
    private FloatingActionButton floatingActionButton;
    private ListFragment listFragment;

    private Receiver receiver;
    private boolean isRoot = true;

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
        listFragment = new ListFragment();
        receiver = new Receiver();

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFragment, listFragment);
        transaction.commit();

        startService(new Intent(this, TreeManagerService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.RECEIVE_CHILDREN);
        registerReceiver(receiver, filter);
        floatingActionButton.show();
    }

    public void addActivity(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.selectCreateTitle)
                .setItems(R.array.selectCreate, this);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        Log.d(TAG, "Escogido: "+which);

        Intent intent;

        switch (which){
            case 0:
                intent = new Intent(this, AddProjectActivity.class);
                startActivityForResult(intent, REQUEST_PROJECT);
                break;
            case 1:
                intent = new Intent(this, AddTaskActivity.class);
                startActivityForResult(intent, REQUEST_TASK);
                break;
        }

    }

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

    @Override
    public void onBackPressed() {
        if(isRoot){
            super.onBackPressed();
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

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.RECEIVE_CHILDREN)) {
                Log.d(TAG, "Receive Childs");
                isRoot = intent.getBooleanExtra("isRoot", true);
                ArrayList<ActivityHolder> activities = (ArrayList<ActivityHolder>) intent.getSerializableExtra("childs");

                Log.d(TAG, "NChilds "+activities.size());
                listFragment.actualitzaDades(activities);
            }
        }
    }
}
