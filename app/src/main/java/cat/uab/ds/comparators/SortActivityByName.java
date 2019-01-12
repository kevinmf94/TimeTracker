package cat.uab.ds.comparators;

import java.util.Comparator;

import cat.uab.ds.core.entity.Activity;

public class SortActivityByName implements Comparator<Activity> {

    @Override
    public int compare(Activity a1, Activity a2) {
        return a1.getName().compareTo(a2.getName());
    }
}
