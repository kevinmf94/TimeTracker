package cat.uab.ds.ui;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GenerateReportActivity extends AppCompatActivity {

    public static final String GENERATE_REPORT = "GenerateReport";
    DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public enum REPORT_TYPES {
        SHORT,
        DETAILED
    }

    public enum REPORT_FORMATS {
        ASCII,
        HTML
    }

    private Spinner type;
    private Spinner format;
    private Button startButton;
    private Button endButton;
    private Calendar start;
    private Calendar end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_report);

        getSupportActionBar().setTitle(R.string.generate_report);

        type = findViewById(R.id.spinnerReportType);
        format = findViewById(R.id.spinnerFormat);
        startButton = findViewById(R.id.buttonStartDate);
        endButton = findViewById(R.id.buttonEndDate);

        end = Calendar.getInstance();
        start = Calendar.getInstance();
        start.add(Calendar.MONTH, -1);

        ArrayAdapter<CharSequence> adapterTypes = ArrayAdapter.createFromResource(this,
                R.array.report_types_array, android.R.layout.simple_spinner_item);
        adapterTypes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        type.setAdapter(adapterTypes);
        type.setSelection(REPORT_TYPES.SHORT.ordinal());

        ArrayAdapter<CharSequence> adapterFormats = ArrayAdapter.createFromResource(this,
                R.array.formats_array, android.R.layout.simple_spinner_item);
        adapterFormats.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        format.setAdapter(adapterFormats);
        format.setSelection(REPORT_FORMATS.ASCII.ordinal());

        startButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePicker(start);
            }
        });

        endButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDatePicker(end);
            }
        });

        updateButtonsText();

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }

    }

    public void onCreate(View view){

        if (checkErrors()) {

            Intent intent = new Intent(GENERATE_REPORT);
            intent.putExtra("from", start.getTime());
            intent.putExtra("to", end.getTime());
            intent.putExtra("type", type.getSelectedItemPosition());
            intent.putExtra("format", format.getSelectedItemPosition());
            sendBroadcast(intent);
            //setResult(RESULT_OK, intent);
            //finish();
        }
    }

    public boolean checkErrors(){

        boolean correct = true;
        if(start.after(end)){
            correct = false;
            Toast.makeText(this, "La fecha de inicio es mayor a la de fin.", Toast.LENGTH_LONG).show();
        }
        return correct;
    }

    public void updateButtonsText(){
        startButton.setText(df.format(start.getTime()));
        endButton.setText(df.format(end.getTime()));
    }

    public void showDatePicker(final Calendar cal){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        cal.set(year, monthOfYear, dayOfMonth);
                        showTimePicker(cal);
                        updateButtonsText();
                    }
                }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));


        datePickerDialog.show();
    }

    public void showTimePicker(final Calendar cal){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        cal.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        cal.set(Calendar.MINUTE, minute);

                        updateButtonsText();
                    }
                }, cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    public void onCancel(View view){
        setResult(RESULT_CANCELED);
        finish();
    }
}
