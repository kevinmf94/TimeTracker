package cat.uab.ds.core.utils;

import cat.uab.ds.core.entity.Activity;
import cat.uab.ds.core.entity.Project;
import cat.uab.ds.core.entity.Task;

/**
 * Visitor interface
 */
public interface ActivitiyVisitor {
    void visitActivity(Activity p);
}
