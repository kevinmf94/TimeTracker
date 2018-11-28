package cat.uab.ds.ui.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

import cat.uab.ds.ui.MainActivity;
import cat.uab.ds.ui.R;

public class ActivitiesAdapter extends ArrayAdapter<ActivityHolder>  {

    private static final int MINUTE = 60;
    private static final String TAG = "ActivitiesAdapter";

    private Context context;
    private int resource;

    public ActivitiesAdapter(Context context, int resource, List<ActivityHolder> objects) {
        super(context, resource, objects);
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
            holder.title = v.findViewById(R.id.activityTitle);
            holder.duration = v.findViewById(R.id.duration);
            holder.btn = v.findViewById(R.id.btnPlay);
            holder.typeIcon = v.findViewById(R.id.imgType);
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final ActivityHolder item = getItem(position);

        holder.title.setText(item.getName());
        holder.duration.setText(durationToStr(item.getDuration()));

        if(item.isTask()){
            Log.d(TAG, item.toString()+" Duration: "+item.getDuration());
            holder.btn.setVisibility(View.VISIBLE);

            if(item.isRunning()){
                holder.btn.setImageResource(R.drawable.ic_stop);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Click task");
                        Intent intent = new Intent(MainActivity.STOP_TASK);
                        intent.putExtra("pos", position);
                        context.sendBroadcast(intent);
                    }
                });
            } else {
                holder.btn.setImageResource(R.drawable.ic_play);
                holder.btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "Click task");
                        Intent intent = new Intent(MainActivity.START_TASK);
                        intent.putExtra("pos", position);
                        context.sendBroadcast(intent);
                    }
                });
            }

            holder.typeIcon.setImageResource(R.drawable.ic_task);
        } else {
            holder.typeIcon.setImageResource(R.drawable.ic_folder);
            holder.btn.setVisibility(View.GONE);
        }

        return v;
    }

    private String durationToStr(final int time) {
        long hours = time / MINUTE / MINUTE;
        long minutes = time / MINUTE;
        long seconds = time % MINUTE;

        return String.format(new Locale("en"),
                "%02d:%02d:%02d", hours, minutes, seconds);
    }

    class ViewHolder {
        TextView title;
        TextView duration;
        ImageView btn;
        ImageView typeIcon;
    }
}
