package cat.uab.ds.ui;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import cat.uab.ds.core.entity.Task;
import cat.uab.ds.core.entity.TaskBasic;
import cat.uab.ds.core.entity.TaskLimited;
import cat.uab.ds.core.entity.TaskScheduled;

/**
 *
 */
public class AddTaskActivity extends AppCompatActivity {

    private DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    private Calendar scheduledCal;

    //UI
    private EditText name;
    private EditText description;
    private CheckBox scheduledChk;
    private CheckBox limitedChk;
    private Button scheduledBtn;
    private EditText limitedNumber;
    private LinearLayout scheduledLyt;
    private LinearLayout limitedLyt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_task);

        getSupportActionBar().setTitle(R.string.actionBarTask);

        name = findViewById(R.id.taskName);
        description = findViewById(R.id.taskDesc);

        scheduledChk = findViewById(R.id.checkScheduled);
        scheduledBtn = findViewById(R.id.buttonScheduled);
        limitedChk = findViewById(R.id.checkLimited);
        limitedNumber = findViewById(R.id.numberLimited);

        scheduledLyt = findViewById(R.id.layoutScheduled);
        limitedLyt = findViewById(R.id.layoutLimited);

        //Scheduled Task Decorator Checkbox click handler. If is checked, shows the scheduled options (start date).
        scheduledChk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    scheduledLyt.setVisibility(View.VISIBLE);
                    scheduledCal = Calendar.getInstance();
                }else{
                    scheduledLyt.setVisibility(View.GONE);
                }
                refreshScheduledButton();
            }
        });

        //Limited Task Decorator Checkbox click handler. If is checked, shows the limited options (time limit).
        limitedChk.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    limitedLyt.setVisibility(View.VISIBLE);
                    limitedNumber.setText("60");
                }else{
                    limitedLyt.setVisibility(View.GONE);
                }
                refreshScheduledButton();
            }
        });

        //Scheduled date picker button click handler.
        scheduledBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               showDatePicker(scheduledCal);
            }
        });

    }

    /**
     * Shows a DatePicker and update the date calendar. Then shows a TimePicker.
     * @param cal Calendar to change
     */
    public void showDatePicker(final Calendar cal){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        cal.set(year, monthOfYear, dayOfMonth);
                        showTimePicker(cal);
                        refreshScheduledButton();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    /**
     * Shows a TimePicker and update the date calendar. Then refresh the buttons text.
     * @param cal Calendar to change
     */
    public void showTimePicker(final Calendar cal){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cal.set(Calendar.MINUTE, minute);

                        refreshScheduledButton();
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    /**
     * Function to refresh scheduled date button text
     */
    private void refreshScheduledButton(){
        if(scheduledCal != null){
            scheduledBtn.setText(df.format(scheduledCal.getTime()));
        }else{
            scheduledBtn.setText("");
        }
    }

    /**
     * Handler function for the "Create" button.
     * It creates a task, decorates it if is necessary, and send it to TreeManagerService.
     * @param view Button view
     */
    public void onAccept(View view){

        if (checkErrors()) {
            Intent intent = new Intent();
            Task task = new TaskBasic(name.getText().toString(),
                    description.getText().toString());

            if(scheduledChk.isChecked()){
                task = new TaskScheduled(task, scheduledCal.getTime());
            }

            if(limitedChk.isChecked()){
                task = new TaskLimited(task, Integer.parseInt(limitedNumber.getText().toString()));
            }

            intent.putExtra("activity", task);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    /**
     * Check errors on user inputs
     * @return isCorrect (bool)
     */
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
