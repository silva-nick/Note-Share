package com.test.nick.noteshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class StickyEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sticky_edit);

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });
    }

    private void leaveActivity() {

        Intent sendData = new Intent();
        sendData.putExtra("new_title", "whatever is changed");
        sendData.putExtra("new_body", "whatever is changed");

        setResult(69, sendData);
        finish();
    }
}
