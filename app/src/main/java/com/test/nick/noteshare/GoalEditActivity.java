package com.test.nick.noteshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputEditText;
import com.test.nick.noteshare.data.Note;

public class GoalEditActivity extends AppCompatActivity {
    private static final String TAG = "GoalEditActivity";
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.goal_edit);

        Spinner spin = findViewById(R.id.goal_spinner);
        String [] options = new String[]{"Hourly", "Daily", "Weekly"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
        spin.setAdapter(adapter);

        note = getIntent().getParcelableExtra("note");

        ((EditText)findViewById(R.id.goal_title)).setText(note.title);
        Log.d(TAG, "onCreate: wwwwwwww " +note.body);
        if(!note.body.equals("")){
            String[] bodyArray = note.body.split(MainActivity.breakCode);
            ((Button)findViewById(R.id.goal_start1)).setText(bodyArray[0]);
            ((Button)findViewById(R.id.goal_start2)).setText(bodyArray[1]);
            ((TextInputEditText)findViewById(R.id.goal_description_text)).setText(bodyArray[2]);
            ((Spinner)findViewById(R.id.goal_spinner)).setSelection(adapter.getPosition(bodyArray[3]));
        }
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
        String output = ((Button)findViewById(R.id.goal_start1)).getText() + MainActivity.breakCode;
        output += ((Button)findViewById(R.id.goal_start2)).getText() + MainActivity.breakCode;
        output += ((TextInputEditText)findViewById(R.id.goal_description_text)).getText() + MainActivity.breakCode;
        output += ((Spinner)findViewById(R.id.goal_spinner)).getSelectedItem().toString();
        return output;
    }
}
