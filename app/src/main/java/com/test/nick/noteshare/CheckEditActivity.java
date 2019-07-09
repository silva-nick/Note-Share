package com.test.nick.noteshare;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

public class CheckEditActivity extends AppCompatActivity {
    private static final String TAG = "CheckEditActivity";

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
        //EditText body = findViewById(R.id.check_body);

        Intent sendData = new Intent();
        sendData.putExtra("new_title", title.getText().toString());
        //sendData.putExtra("new_body", body.getText().toString());

        setResult(RESULT_OK, sendData);
        finish();
    }

    public void addCheck(View v){
        Log.d(TAG, "addCheck: ");
        LinearLayout list = findViewById(R.id.check_list);
        CheckBox box = new CheckBox(this);
        box.setButtonDrawable(R.drawable.temp);
        list.addView(box);
    }
}
