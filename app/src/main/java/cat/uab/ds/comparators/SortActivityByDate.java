package cat.uab.ds.comparators;

import java.util.Comparator;

import cat.uab.ds.core.entity.Activity;

public class SortActivityByDate implements Comparator<Activity> {

    @Override
    public int compare(Activity a1, Activity a2) {
        if(a1.getStart() == null) return -1;
        if(a2.getStart() == null) return 1;
        return a1.getStart().compareTo(a2.getStart());
    }
}
