package cat.uab.ds.ui;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.adapters.IntervalAdapter;
import cat.uab.ds.ui.adapters.IntervalHolder;
import cat.uab.ds.ui.services.TreeManagerService;

/**
 * Activity to list the intervals of a Task.
 */
public class IntervalsActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    //Constants
    private static final String TAG = "IntervalsActivity";

    public static final String GET_INTERVALS = "GetIntervals";
    public static final String REMOVE_INTERVAL = "RemoveInterval";

    //UI
    private ListView listView;
    private TextView textView;
    private TextView txtName;
    private TextView txtDesc;
    private TextView txtIni;
    private TextView txtFin;
    private TextView txtDuration;
    private TextView txtBreadcrumb;

    //Other
    private Receiver receiver;
    private IntervalAdapter adapter;
    private int pos;
    private Activity parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment_intervals);

        listView = findViewById(R.id.listItems);
        textView = findViewById(R.id.emptyText);
        txtName = findViewById(R.id.txtName);
        txtDesc = findViewById(R.id.txtDesc);
        txtIni = findViewById(R.id.txtIni);
        txtFin = findViewById(R.id.txtFin);
        txtDuration = findViewById(R.id.txtDuration);
        txtBreadcrumb = findViewById(R.id.txtBreadcrumb);

        receiver = new Receiver();

        adapter = new IntervalAdapter(this, R.layout.interval_item_list);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(this);

        pos = getIntent().getIntExtra("taskPos", -1);
        if(pos < 0)
            finish();

        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.RECEIVE_INTERVALS);
        registerReceiver(receiver, filter);

        Intent intent = new Intent(GET_INTERVALS);
        intent.putExtra("taskPos", pos);
        sendBroadcast(intent);
    }

    /**
     * Fill OptionsMenu from ActionBar
     * @param menu The Menu
     * @return Boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.action_bar_menu_intervals, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int pos, long l) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.removeTitle)
                .setMessage(R.string.removeMessage)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(REMOVE_INTERVAL);
                        intent.putExtra("pos", pos);
                        IntervalsActivity.this.sendBroadcast(intent);
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

        return true;
    }

    /**
     * Receiver class of broadcast intents.
     * In this case, receive the intervals list from the service.
     */
    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.RECEIVE_INTERVALS)) {
                ArrayList<IntervalHolder> intervalHolders =
                        (ArrayList<IntervalHolder>) intent.getSerializableExtra("intervals");

                ActivityHolder activity = (ActivityHolder) intent.getSerializableExtra("activityData");

                parent = (Activity) intent.getSerializableExtra("parent");;

                Log.d(TAG, "Received nIntervals "+intervalHolders.size());

                updateDataInterface(activity);
                updateIntervals(intervalHolders);
            }
        }
    }

    /**
     * Changes the ActionBar title to the activity name.
     * @param activity
     */
    private void updateDataInterface(ActivityHolder activity) {
        getSupportActionBar().setTitle(activity.getName());
    }

    /**
     * Show or hide the empty elements message.
     * @param nElements Number of elements in list
     */
    private void updateView(int nElements){
        if (nElements > 0) {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }

        txtName.setText(parent.getName());
        txtDesc.setText(parent.getDescription());
        Date start = parent.getStart();
        Date end = parent.getEnd();
        txtIni.setText(start!=null? Utils.dfDateTimeSeconds.format(start) : "");
        txtFin.setText(end!=null? Utils.dfDateTimeSeconds.format(end) : "");
        txtDuration.setText(Utils.durationToStr(parent.getDuration()));


        txtBreadcrumb.setText(Utils.MakeBreadCrumb(parent));
    }

    /**
     * Refresh the elements of the list
     * @param intervalHolderList
     */
    private void updateIntervals(List<IntervalHolder> intervalHolderList) {
        updateView(intervalHolderList.size());
        adapter.clear();
        adapter.addAll(intervalHolderList);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(GET_INTERVALS);
        intent.putExtra("taskPos", -1);
        sendBroadcast(intent);

        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
