package cat.uab.ds.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.R;
import cat.uab.ds.ui.adapters.ActivitiesAdapter;
import cat.uab.ds.ui.services.TreeManagerService;

public class ListFragment extends Fragment implements AdapterView.OnItemClickListener{

    private ActivitiesAdapter adapter;
    private MainActivity mainActivity;
    private ListView activitiesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        adapter = new ActivitiesAdapter(mainActivity, R.layout.activities_item_list, new ArrayList<Activity>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.main_fragment, container, false);

        activitiesList = v.findViewById(R.id.activitiesList);
        activitiesList.setOnItemClickListener(this);
        activitiesList.setAdapter(adapter);

        ArrayList<Activity> activities = (ArrayList<Activity>) TreeManagerService.actual.getActivities();
        adapter.clear();
        adapter.addAll(new ArrayList<>(activities));
        adapter.notifyDataSetChanged();

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Activity activity = adapter.getItem(position);
        if (activity instanceof Project) {
            TreeManagerService.baixaNivell(activity);
            mainActivity.mockReceiver();
        }
    }

    public void actualitzaDades(Collection<Activity> activities) {
        adapter.clear();
        adapter.addAll(new ArrayList<>(activities));
        adapter.notifyDataSetChanged();
    }
}

