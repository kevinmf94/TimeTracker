package cat.uab.ds.core.utils;

import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

/**
 * Visitor interface
 */
public interface ActivitiyVisitor {
    void visitProject(Project p);
    void visitTask(Task task);
}
