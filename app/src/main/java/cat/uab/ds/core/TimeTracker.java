package cat.uab.ds.core;

import cat.uab.ds.core.entity.Project;

public class TimeTracker {

    private Project root = null;

    public TimeTracker() {}

    public void setRoot(Project root) {
        this.root = root;
    }
}
