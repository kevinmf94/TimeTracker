package cat.uab.ds.core;

import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.entity.TaskLimited;
import cat.uab.ds.core.entity.TaskScheduled;
import cat.uab.ds.core.entity.Task;

public class MainApplication {

    public static void main(String args[]){

        Task ts = new TaskBasic();
        ts = new TaskLimited(ts);
        ts = new TaskScheduled(ts);

        ts.prueba();

    }

}
