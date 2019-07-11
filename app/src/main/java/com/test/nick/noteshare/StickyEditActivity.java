package com.test.nick.noteshare;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class StickyEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticky_edit);

        Button backButton = findViewById(R.id.sticky_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });
    }

    private void leaveActivity() {
        EditText title = findViewById(R.id.sticky_title);
        EditText body = findViewById(R.id.sticky_body);

        Intent sendData = new Intent();
        sendData.putExtra("new_title", title.getText().toString());
        sendData.putExtra("new_body", body.getText().toString());

        setResult(RESULT_OK, sendData);
        finish();
    }
}
