package cat.uab.ds.core.utils;

import java.util.List;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

public interface ActivitiyVisitor {
    void visitActivities(List<Activity> activities);
    void visitProject(Project p);
    void visitTask(Task task);
}
