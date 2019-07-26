package com.test.nick.noteshare;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.test.nick.noteshare.data.Note;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class EventEditActivity extends AppCompatActivity {
    public static final String TAG = "EventEditActivity";

    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_edit);
        note = getIntent().getParcelableExtra("note");

        Spinner spin = findViewById(R.id.event_spinner);
        String [] options = new String[]{"Low", "Medium", "High"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spin.setAdapter(adapter);

        if(note.extra.equals("true")) {
            ((EditText) findViewById(R.id.event_title)).setText(note.title);

            String[] bodyArray = note.body.split(MainActivity.breakCode);

            ((Button)findViewById(R.id.event_start1)).setText(bodyArray[0]);
            ((Button)findViewById(R.id.event_start2)).setText(bodyArray[1]);
            ((TextInputEditText)findViewById(R.id.event_location_text)).setText(bodyArray[2]);
            ((TextInputEditText)findViewById(R.id.event_description_text)).setText(bodyArray[3]);
            ((Button)findViewById(R.id.event_create)).setText("Edit Event");
            ((Spinner)findViewById(R.id.event_spinner)).setSelection(adapter.getPosition(bodyArray[4]));
        }

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
                        timeButton.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select Time");
                timePicker.show();
            }
        });
    }

    //called by create event button
    public void createEvent(View v){
        EditText text = findViewById(R.id.event_title);
        note.title = text.getText().toString();
        note.body = formatBody();
        note.extra = "true";

        String [] info = note.body.split(MainActivity.breakCode);
        String [] date = info[0].split("/");
        String [] time = info[1].split(":");
        LocalDateTime cal = LocalDateTime.of(2019,7,25, 18,2, 20);
        LocalDateTime test = LocalDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]), Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);
        Log.d(TAG, "createEvent: " + date[2]+ date[1]+ date[0]);
        ZonedDateTime zdt = test.atZone(ZoneId.of("America/Chicago"));

        NotificationCreator c = new NotificationCreator(this, note, getSpinner());
        c.createNotification(zdt.toInstant().toEpochMilli());

        Intent sendData = new Intent();
        sendData.putExtra("note", note);
        setResult(RESULT_OK, sendData);
        finish();
    }

    private void leaveActivity() {
        if(note.extra.equals("true")){
            EditText text = findViewById(R.id.event_title);
            note.title = text.getText().toString();
            note.body = formatBody();

            Intent sendData = new Intent();
            sendData.putExtra("note", note);
            setResult(RESULT_OK, sendData);
        }
        finish();
    }

    private String formatBody(){
        String output = ((Button)findViewById(R.id.event_start1)).getText() + MainActivity.breakCode;
        output += ((Button)findViewById(R.id.event_start2)).getText() + MainActivity.breakCode;
        output += ((TextInputEditText)findViewById(R.id.event_location_text)).getText() + MainActivity.breakCode;
        output += ((TextInputEditText)findViewById(R.id.event_description_text)).getText() + MainActivity.breakCode;
        output += ((Spinner)findViewById(R.id.event_spinner)).getSelectedItem().toString();
        return output;
    }

    private NotificationCreator.NoteFrequency getSpinner(){
        String value = ((Spinner)findViewById(R.id.event_spinner)).getSelectedItem().toString();
        switch (value){
            case "Low":
                return NotificationCreator.NoteFrequency.LOW;
            case "Medium":
                return NotificationCreator.NoteFrequency.MEDIUM;
            case "High":
                return NotificationCreator.NoteFrequency.HIGH;
        }
        return NotificationCreator.NoteFrequency.MEDIUM;
    }
}
