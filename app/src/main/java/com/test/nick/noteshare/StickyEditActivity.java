package com.test.nick.noteshare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.test.nick.noteshare.data.Note;

public class StickyEditActivity extends AppCompatActivity {
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticky_edit);

        note = getIntent().getParcelableExtra("note");
        ((EditText)findViewById(R.id.sticky_title)).setText(note.title);
        ((EditText)findViewById(R.id.sticky_body)).setText(note.body);

        Button backButton = findViewById(R.id.sticky_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });
    }

    private void leaveActivity() {
        Intent sendData = new Intent();
        sendData.putExtra("note", note);

        setResult(RESULT_OK, sendData);
        finish();
    }
}
