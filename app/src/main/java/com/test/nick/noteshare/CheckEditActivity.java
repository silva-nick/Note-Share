package com.test.nick.noteshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CheckEditActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_edit);

        Button backButton = findViewById(R.id.check_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });
    }

    private void leaveActivity() {
        EditText title = findViewById(R.id.check_title);
        EditText body = findViewById(R.id.check_body);

        Intent sendData = new Intent();
        sendData.putExtra("new_title", title.getText().toString());
        sendData.putExtra("new_body", body.getText().toString());

        setResult(RESULT_OK, sendData);
        finish();
    }
}
