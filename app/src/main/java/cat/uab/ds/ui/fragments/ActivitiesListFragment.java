package cat.uab.ds.ui.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Locale;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.ui.IntervalsActivity;
import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.R;
import cat.uab.ds.ui.Utils;
import cat.uab.ds.ui.adapters.ActivitiesAdapter;
import cat.uab.ds.ui.adapters.ActivityHolder;

public class ActivitiesListFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private final static String TAG = "ActivitiesListFragment";

    private ActivitiesAdapter adapter;
    private MainActivity mainActivity;
    private ListView activitiesList;
    private TextView emptyText;
    private LinearLayout descLyt;
    private TextView txtName;
    private TextView txtDesc;
    private TextView txtIni;
    private TextView txtFin;
    private TextView txtDuration;
    private TextView txtBreadcrumb;

    private Activity parent;

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

        descLyt = v.findViewById(R.id.descLayout);
        txtName = v.findViewById(R.id.txtName);
        txtDesc = v.findViewById(R.id.txtDesc);
        txtIni = v.findViewById(R.id.txtIni);
        txtFin = v.findViewById(R.id.txtFin);
        txtDuration = v.findViewById(R.id.txtDuration);
        txtBreadcrumb = v.findViewById(R.id.txtBreadcrumb);

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

        AppCompatActivity ctx = (AppCompatActivity) getActivity();
        ActionBar bar = ctx.getSupportActionBar();

        if (parent.isRoot()){
            bar.setTitle(R.string.app_name);
            descLyt.setVisibility(View.GONE);
            txtBreadcrumb.setVisibility(View.GONE);
        }else{
            bar.setTitle(parent.getName());
            descLyt.setVisibility(View.VISIBLE);
            txtBreadcrumb.setVisibility(View.VISIBLE);
        }

        txtBreadcrumb.setText(Utils.MakeBreadCrumb(parent));

        txtName.setText(parent.getName());
        txtDesc.setText(parent.getDescription());

        Date start = parent.getStart();
        Date end = parent.getEnd();
        txtIni.setText(start!=null? Utils.dfDateTimeSeconds.format(start) : "");
        txtFin.setText(end!=null? Utils.dfDateTimeSeconds.format(end) : "");
        txtDuration.setText(Utils.durationToStr(parent.getDuration()));

    }

    public void updateData(Activity parent, Collection<ActivityHolder> activities) {
        this.parent = parent;
        updateView(activities.size());
        adapter.clear();
        adapter.addAll(activities);
        adapter.notifyDataSetChanged();
    }

}

