package cat.uab.ds.comparators;

import java.util.Comparator;
import cat.uab.ds.core.entity.Interval;

public class SortIntervalByDate implements Comparator<Interval> {

    @Override
    public int compare(Interval i1, Interval i2) {
        if(i1.getStart() == null) return -1;
        if(i2.getStart() == null) return 1;
        return i1.getStart().compareTo(i2.getStart());
    }
}
