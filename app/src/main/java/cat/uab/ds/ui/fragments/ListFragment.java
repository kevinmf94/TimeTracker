package cat.uab.ds.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.R;
import cat.uab.ds.ui.adapters.ActivitiesAdapter;
import cat.uab.ds.ui.adapters.ActivityHolder;

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener {

    private final static String TAG = "ListFragment";

    private ActivitiesAdapter adapter;
    private MainActivity mainActivity;
    private ListView activitiesList;
    private TextView emptyText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG,"onCreate ListFragment");
        mainActivity = (MainActivity) getActivity();
        adapter = new ActivitiesAdapter(mainActivity,
                R.layout.activities_item_list, new ArrayList<ActivityHolder>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.list_fragment, container, false);

        emptyText = v.findViewById(R.id.emptyText);
        activitiesList = v.findViewById(R.id.activitiesList);
        activitiesList.setOnItemClickListener(this);
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

    public void actualitzaDades(Collection<ActivityHolder> activities) {
        updateView(activities.size());
        adapter.clear();
        adapter.addAll(activities);
        adapter.notifyDataSetChanged();
    }
}

