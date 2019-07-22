package com.test.nick.noteshare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;
import com.test.nick.noteshare.data.Note;

public class EventEditActivity extends AppCompatActivity {
    private Note note;

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
    }

    private void leaveActivity() {
        EditText title = findViewById(R.id.event_title);
        //EditText body = findViewById(R.id.fuckidk);
        Intent sendData = new Intent();
        note.title = title.getText().toString();
        //note.body = body.getText().toString();

        sendData.putExtra("note", note);

        setResult(RESULT_OK, sendData);
        finish();
    }
}
