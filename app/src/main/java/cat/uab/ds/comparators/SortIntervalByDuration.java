package cat.uab.ds.comparators;

import java.util.Comparator;
import cat.uab.ds.core.entity.Interval;

public class SortIntervalByDuration implements Comparator<Interval> {

    @Override
    public int compare(Interval i1, Interval i2) {
        return Long.compare(i1.getDuration(), i2.getDuration());
    }
}
