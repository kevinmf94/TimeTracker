package cat.uab.ds.core.utils;

import cat.uab.ds.core.entity.Interval;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

/**
 * Visitor interface for Visitor pattern.
 */
public interface ActivityVisitor {
    void visit(Project project);
    void visit(Task task);
    void visit(Interval interval);
}
