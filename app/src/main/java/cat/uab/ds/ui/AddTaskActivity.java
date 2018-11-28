package cat.uab.ds.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cat.uab.ds.core.entity.Task;
import cat.uab.ds.core.entity.TaskBasic;

public class AddTaskActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        getSupportActionBar().setTitle(R.string.actionBarTask);

        name = findViewById(R.id.taskName);
        description = findViewById(R.id.taskDesc);
    }

    public void onAccept(View view){

        if (checkErrors()) {
            Intent intent = new Intent();
            Task task = new TaskBasic(name.getText().toString(),
                    description.getText().toString());
            intent.putExtra("activity", task);
            setResult(RESULT_OK, intent);
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
}
