package cat.uab.ds.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import cat.uab.ds.core.entity.Task;
import cat.uab.ds.ui.services.TreeManagerService;

public class EditTaskActivity extends AppCompatActivity {

    private static final String TAG = "EditTaskActivity";

    public static final String GET_CHILD = "GetChild";
    public static final String UPDATE_TASK = "UpdateTask";

    private int pos;
    private EditText name;
    private EditText description;
    private Receiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_task);

        getSupportActionBar().setTitle(R.string.editTask_title);

        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.SEND_CHILD);
        registerReceiver(receiver, filter);

        pos = getIntent().getIntExtra("pos", -1);
        if(pos < 0)
            finish();

        name = findViewById(R.id.taskName);
        description = findViewById(R.id.taskDesc);

        Intent intent = new Intent(GET_CHILD);
        intent.putExtra("pos", pos);
        sendBroadcast(intent);
    }

    public void onAccept(View view){

        if (checkErrors()) {
            Intent intent = new Intent(UPDATE_TASK);
            intent.putExtra("pos", pos);
            intent.putExtra("name", name.getText().toString());
            intent.putExtra("description", description.getText().toString());
            sendBroadcast(intent);
            finish();
        }
    }

    public boolean checkErrors(){

        boolean correct = true;

        if(TextUtils.isEmpty(name.getText())){
            correct = false;
            name.setError(getString(R.string.noNameError));
        }

        return correct;
    }

    public void onCancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.SEND_CHILD)) {
                Log.d(TAG, "Receive Child");
                Task task= (Task) intent
                        .getSerializableExtra("activity");

                name.setText(task.getName());
                description.setText(task.getDescription());
            }
        }
    }
}
