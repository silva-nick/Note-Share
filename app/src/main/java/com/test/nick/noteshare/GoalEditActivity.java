package com.test.nick.noteshare;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.test.nick.noteshare.data.Note;

public class GoalEditActivity extends AppCompatActivity {
    private static final String TAG = "GoalEditActivity";
    private Note note;
    private int[] completion = new int[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_edit);

        Spinner date = findViewById(R.id.goal_time_spinner);
        String [] dates = new String[]{"Day", "Week", "Month"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, dates);
        date.setAdapter(adapter);

        Spinner freq = findViewById(R.id.goal_freq_spinner);
        String [] freqs = new String[]{"Once", "Twice", "Three Times", "Four Times",
                "Five Times","Six Times","Seven Times","Eight Times","Nine Times","Ten Times"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, freqs);
        freq.setAdapter(adapter1);


        note = getIntent().getParcelableExtra("note");

        ((EditText)findViewById(R.id.goal_title)).setText(note.title);
        Log.d(TAG, "onCreate: wwwwwwww " + note.body);
        if(!note.body.equals("")){
            String[] bodyArray = note.body.split(MainActivity.breakCode);
            ((Spinner)findViewById(R.id.goal_time_spinner)).setSelection(adapter.getPosition(bodyArray[0]));
            ((Spinner)findViewById(R.id.goal_freq_spinner)).setSelection(adapter.getPosition(bodyArray[1]));
            ((Button)findViewById(R.id.goal_date)).setText(bodyArray[2]);
        }
        if(!note.extra.equals("")){
            completion[0] = Integer.parseInt(note.extra.split(MainActivity.breakCode)[0]);
            completion[1] = Integer.parseInt(note.extra.split(MainActivity.breakCode)[1]);
        } else {
            completion[0] = 0;
            completion[1] = 1;
        }

        (findViewById(R.id.goal_runner)).setX(findViewById(R.id.goal_runner_layout).getWidth() * (completion[0]/completion[1]) );

        final Button dateButton = findViewById(R.id.goal_date);
        dateButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                DatePickerDialog dateDialog = new DatePickerDialog(GoalEditActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
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
    }

    public void createGoal(View v){
        EditText text = findViewById(R.id.goal_title);
        note.title = text.getText().toString();
        note.body = formatBody();

        Intent sendData = new Intent();
        sendData.putExtra("note", note);
        setResult(RESULT_OK, sendData);
        finish();
    }

    private String formatBody(){
        String output = ((Spinner)findViewById(R.id.goal_time_spinner)).getSelectedItem().toString() + MainActivity.breakCode;
        output += ((Spinner)findViewById(R.id.goal_freq_spinner)).getSelectedItem().toString() + MainActivity.breakCode;
        output += ((Button)findViewById(R.id.goal_date)).getText() + MainActivity.breakCode;
        return output;
    }
}
