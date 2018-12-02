package cat.uab.ds.ui.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Collection;

import cat.uab.ds.ui.IntervalsActivity;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.R;
import cat.uab.ds.ui.adapters.ActivitiesAdapter;
import cat.uab.ds.ui.adapters.ActivityHolder;

public class ActivitiesListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final static String TAG = "ActivitiesListFragment";

    private ActivitiesAdapter adapter;
    private MainActivity mainActivity;
    private ListView activitiesList;
    private TextView emptyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate ActivitiesListFragment");
        mainActivity = (MainActivity) getActivity();
        adapter = new ActivitiesAdapter(mainActivity,
                R.layout.activities_item_list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        emptyText = v.findViewById(R.id.emptyText);
        activitiesList = v.findViewById(R.id.listItems);
        activitiesList.setOnItemClickListener(this);
        activitiesList.setOnItemLongClickListener(this);
        activitiesList.setAdapter(adapter);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG,"Item clicked");
        ActivityHolder activity = adapter.getItem(position);
        if (activity.isProject()) {
            Intent intent = new Intent(MainActivity.DOWN_TREE);
            intent.putExtra("pos", position);
            mainActivity.sendBroadcast(intent);
        }
        else if(activity.isTask()) {
            Intent intent = new Intent(mainActivity, IntervalsActivity.class);
            intent.putExtra("taskPos", position);
            startActivity(intent);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setTitle(R.string.removeTitle)
                .setMessage(R.string.removeMessage)
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(MainActivity.REMOVE_ACTIVITY);
                        intent.putExtra("pos", position);
                        mainActivity.sendBroadcast(intent);
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

    private void updateView(int nElements){
        if (nElements > 0) {
            activitiesList.setVisibility(View.VISIBLE);
            emptyText.setVisibility(View.GONE);
        } else {
            activitiesList.setVisibility(View.GONE);
            emptyText.setVisibility(View.VISIBLE);
        }
    }

    public void updateData(Collection<ActivityHolder> activities) {
        updateView(activities.size());
        adapter.clear();
        adapter.addAll(activities);
        adapter.notifyDataSetChanged();
    }


}

