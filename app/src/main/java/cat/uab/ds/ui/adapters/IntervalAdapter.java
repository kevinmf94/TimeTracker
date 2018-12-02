package cat.uab.ds.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Locale;

import cat.uab.ds.ui.R;

public class IntervalAdapter extends ArrayAdapter<IntervalHolder> {

    private static final int MINUTE = 60;
    private static final String TAG = "IntervalAdapter";

    private Context context;
    private int resource;

    public IntervalAdapter(Context context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(final int position, View v, ViewGroup parent) {

        ViewHolder holder;

        if (v == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            v = layoutInflater.inflate(resource, null);

            holder = new ViewHolder();
            holder.startDate = v.findViewById(R.id.startDate);
            holder.endDate = v.findViewById(R.id.endDate);
            holder.duration = v.findViewById(R.id.duration);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final IntervalHolder item = getItem(position);

        holder.startDate.setText(item.getDateStart());
        holder.endDate.setText(item.getDateEnd());
        holder.duration.setText(durationToStr(item.getDuration()));

        return v;
    }

    private String durationToStr(final int time) {
        long hours = time / MINUTE / MINUTE;
        long minutes = time / MINUTE;
        long seconds = time % MINUTE;

        return String.format(new Locale("en"),
                "%02d:%02d:%02d", hours, minutes, seconds);
    }

    private class ViewHolder {
        TextView startDate;
        TextView endDate;
        TextView duration;
    }

}
