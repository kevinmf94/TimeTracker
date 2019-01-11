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

import cat.uab.ds.core.entity.Project;
import cat.uab.ds.ui.services.TreeManagerService;

public class EditProjectActivity extends AppCompatActivity {

    private static final String TAG = "EditProjectActivity";

    private Receiver receiver;
    public static final String GET_CHILD = "GetChild";
    public static final String UPDATE_PROJECT = "UpdateProject";

    private int pos;
    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_project);

        getSupportActionBar().setTitle(R.string.editProject_title);

        receiver = new Receiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TreeManagerService.SEND_CHILD);
        registerReceiver(receiver, filter);

        pos = getIntent().getIntExtra("pos", -1);
        if(pos < 0)
            finish();

        name = findViewById(R.id.projectName);
        description = findViewById(R.id.projectDesc);

        Intent intent = new Intent(GET_CHILD);
        intent.putExtra("pos", pos);
        sendBroadcast(intent);
    }

    public void onAccept(View view){

        if (checkErrors()) {
            Intent intent = new Intent(UPDATE_PROJECT);
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
        finish();
    }

    private class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(TreeManagerService.SEND_CHILD)) {
                Log.d(TAG, "Receive Child");
                Project project = (Project) intent
                        .getSerializableExtra("activity");

                name.setText(project.getName());
                description.setText(project.getDescription());
            }
        }
    }

}
