package cat.uab.ds.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import cat.uab.ds.core.entity.Project;

public class AddProjectActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_project);

        getSupportActionBar().setTitle(R.string.actionBarProject);

        name = findViewById(R.id.projectName);
        description = findViewById(R.id.projectDesc);
    }

    public void onAccept(View view){

        if (checkErrors()) {
            Intent intent = new Intent();
            Project project = new Project(name.getText().toString(),
                    description.getText().toString());
            intent.putExtra("activity", project);
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
