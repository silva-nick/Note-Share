package com.test.nick.noteshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.EventLog;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.nick.noteshare.data.Note;

import org.w3c.dom.Text;

import java.util.Locale;

public class EventEditActivity extends AppCompatActivity {
    private Note note;
    private int mYear,mMonth,mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);

        note = getIntent().getParcelableExtra("note");
        ((EditText)findViewById(R.id.event_title)).setText(note.title);
        //((TextInputLayout)findViewById(R.id.fuckidk)).setText(note.body);

        Button backButton = findViewById(R.id.event_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });

        final Button dateButton = (Button) findViewById(R.id.event_start1);

        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();

                DatePickerDialog dateDialog = new DatePickerDialog(EventEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateButton.setText(dayOfMonth + "/"
                                        + (monthOfYear + 1) + "/" + year);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                dateDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dateDialog.show();
            }
        });

        final Button timeButton = (Button)findViewById(R.id.event_start2);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        timeButton.setText( selectedHour + ":" + String.format("%2d", (double)selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
    }

    private void leaveActivity() {
        EditText text = findViewById(R.id.event_title);
        note.title = text.getText().toString();
        //text = findViewById(R.id.event_start2);
        Intent sendData = new Intent();

        //note.body = body.getText().toString();

        sendData.putExtra("note", note);

        setResult(RESULT_OK, sendData);
        finish();
    }
}
