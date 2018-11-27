package cat.uab.ds.ui;

import android.app.Fragment;
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

import java.util.Collection;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.ui.fragments.EmptyFragment;
import cat.uab.ds.ui.fragments.ListFragment;
import cat.uab.ds.ui.fragments.SplashFragment;
import cat.uab.ds.ui.services.TreeManagerService;

public class MainActivity extends AppCompatActivity {

    //Constants
    private final static String TAG = "MainActivity";

    //UI and Fragments
    private FloatingActionButton floatingActionButton;
    private ListFragment listFragment;
    private EmptyFragment emptyFragment;

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

        listFragment = new ListFragment();
        emptyFragment = new EmptyFragment();
        receiver = new Receiver();

        startService(new Intent(this, TreeManagerService.class));

        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.RECEIVE_CHILDS);
        registerReceiver(receiver, filter);

        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFragment, new SplashFragment());
        transaction.commit();
    }

    public void loadMainList(){
        floatingActionButton.show();
        Collection<Activity> activities = TreeManagerService.actual.getActivities();
        if(activities.size() == 0)
            replaceFragment(emptyFragment);
        else {
            replaceFragment(listFragment);
        }
    }

    public void mockReceiver(){
        Collection<Activity> activities = TreeManagerService.actual.getActivities();
        if(activities.size() == 0)
            changeToEmpty();
        else {
            replaceFragment(listFragment);
        }
        listFragment.actualitzaDades(activities);
    }

    public void changeToEmpty(){
        replaceFragment(emptyFragment);
    }

    public void changeToList(){
        replaceFragment(listFragment);
    }

    public void replaceFragment(Fragment fragment){
        FragmentManager manager = this.getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.mainFragment, fragment);
        transaction.commit();
    }

    public void addActivity(View view){
        if(TreeManagerService.actual.getActivities().size() == 0) {
            replaceFragment(listFragment);
        }

        TreeManagerService.crearProjecte();
        Collection<Activity> activities = TreeManagerService.actual.getActivities();
        changeToList();
        listFragment.actualitzaDades(activities);
    }

    @Override
    public void onBackPressed() {
        if(isRoot){
            Log.d(TAG, "OnBackPressed");
            TreeManagerService.pujaNivell();
            Collection<Activity> activities = TreeManagerService.actual.getActivities();
            changeToList();
            listFragment.actualitzaDades(activities);
        } else {
            Log.d(TAG, "OnBackPressed");
            TreeManagerService.pujaNivell();
            Collection<Activity> activities = TreeManagerService.actual.getActivities();
            changeToList();
            listFragment.actualitzaDades(activities);
        }
    }

    class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.RECEIVE_CHILDS)) {
                Log.d(TAG, "Receive Childs");
            }
        }
    }
}
