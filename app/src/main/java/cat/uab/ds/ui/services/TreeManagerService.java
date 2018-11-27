package cat.uab.ds.ui.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.TaskBasic;

public class TreeManagerService extends Service {

    //Constants
    public static final String TAG = "TreeManagerService";
    public static final String RECEIVE_CHILDS = "RECEIVE_CHILDS";

    public static Project root = new Project("root");
    public static Project actual;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand TreeManagerService");

        sendBroadcast(new Intent(RECEIVE_CHILDS));

        return Service.START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Created TreeManagerService");
        TreeManagerService.generateTree();
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void generateTree(){
        root.setRoot(true);

        Project p1 = new Project("P1");
        p1.addActivity(new TaskBasic("T1"));
        root.addActivity(p1);
        root.addActivity(new Project("P2"));

        actual = root;
    }

    public static void baixaNivell(Activity activity){
        actual = (Project) activity;

        Log.d(TAG, "Nivell actual "+actual.getName());
    }

    public static void pujaNivell(){
        if(actual != root)
            actual = (Project) actual.getParent();

        Log.d(TAG, "Nivell actual "+actual.getName());
    }

    public static void crearProjecte(){
        actual.addActivity(new Project("PX"));
    }

}
