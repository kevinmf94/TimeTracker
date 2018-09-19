package cat.uab.ds.core.entity;

import java.util.ArrayList;

/**
 * Task implements basic functionality
 */
public class TaskBasic extends Task {

    public TaskBasic(String name) {
        super(name);
    }

    public TaskBasic(String name, String description) {
        super(name, description);
    }

    @Override
    public void prueba() {
        System.out.println("PRUEBA1");
    }


}
