package cat.uab.ds.comparators;

import java.util.Comparator;

import cat.uab.ds.core.entity.Activity;

public class SortActivityByDuration implements Comparator<Activity> {

    @Override
    public int compare(Activity a1, Activity a2) {
        return Integer.compare(a1.getDuration(), a2.getDuration());
    }
}
