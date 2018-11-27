package cat.uab.ds.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.ui.R;

public class ActivitiesAdapter extends ArrayAdapter<Activity> {

    private Context context;
    private int resource;

    public ActivitiesAdapter(Context context, int resource, List<Activity> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(resource, null);
        }

        Activity item = getItem(position);

        if (convertView != null) {
            TextView title = convertView.findViewById(R.id.activityTitle);
            title.setText(item.getName());
        }

        return convertView;
    }
}
