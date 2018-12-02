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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cat.uab.ds.ui.adapters.ActivityHolder;
import cat.uab.ds.ui.adapters.IntervalAdapter;
import cat.uab.ds.ui.adapters.IntervalHolder;
import cat.uab.ds.ui.services.TreeManagerService;

public class IntervalsActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {

    private static final String TAG = "IntervalsActivity";

    public static final String GET_INTERVALS = "GetIntervals";
    public static final String REMOVE_INTERVAL = "RemoveInterval";

    private Receiver receiver;

    private ListView listView;
    private TextView textView;
    private IntervalAdapter adapter;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_fragment_intervals);

        listView = findViewById(R.id.listItems);
        textView = findViewById(R.id.emptyText);
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

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.RECEIVE_INTERVALS)) {
                ArrayList<IntervalHolder> intervalHolders =
                        (ArrayList<IntervalHolder>) intent.getSerializableExtra("intervals");

                ActivityHolder activity = (ActivityHolder) intent.getSerializableExtra("activityData");

                Log.d(TAG, "Received nIntervals "+intervalHolders.size());

                updateDataInterface(activity);
                updateIntervals(intervalHolders);
            }
        }
    }

    private void updateDataInterface(ActivityHolder activity) {
        getSupportActionBar().setTitle(activity.getName());
    }

    private void updateView(int nElements){
        if (nElements > 0) {
            listView.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
        }
    }


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
