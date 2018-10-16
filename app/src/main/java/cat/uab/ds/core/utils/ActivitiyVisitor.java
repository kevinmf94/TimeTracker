package cat.uab.ds.core.utils;

import cat.uab.ds.core.entity.Activity;

/**
 * Visitor interface for Visitor pattern
 */
public interface ActivitiyVisitor {
    void visitActivity(Activity p);
}
