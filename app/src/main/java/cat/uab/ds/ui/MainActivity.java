package cat.uab.ds.ui;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.fragments.ListFragment;
import cat.uab.ds.ui.fragments.SplashFragment;
import cat.uab.ds.ui.services.TreeManagerService;

public class MainActivity extends AppCompatActivity {

    //Constants
    private final static String TAG = "MainActivity";

    public final static String DOWN_TREE = "DownTree";
    public final static String UP_TREE = "UpTree";
    public final static String CREATE_ACTIVITY = "CreateActivity";
    public final static String START_TASK = "StartTask";
    public final static String STOP_TASK = "StopTask";

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
        Intent intent = new Intent(this, AddProjectActivity.class);
        startActivityForResult(intent, 0);

        /*Intent intent2 = new Intent(CREATE_ACTIVITY);
        intent2.putExtra("activity", new Project("PX"));
        sendBroadcast(intent2);*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
