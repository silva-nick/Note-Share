package com.test.nick.noteshare;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.nick.noteshare.data.Note;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckEditActivity extends AppCompatActivity {
    private static final String TAG = "CheckEditActivity";

    private Note note;
    private ArrayList<String> checkList = new ArrayList<>();
    private CheckAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.check_edit);

        RecyclerView recyclerView = findViewById(R.id.recycle_check);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        note = getIntent().getParcelableExtra("note");
        EditText title = findViewById(R.id.check_title);
        title.setText(note.title);

        checkList = new ArrayList<String>(Arrays.asList(note.body.split(MainActivity.breakCode)));
        if (!checkList.get(checkList.size() - 1).equals("-100")){checkList.add(checkList.size(), "-100");}
        adapter = new CheckAdapter(checkList);
        recyclerView.setAdapter(adapter);

        Button backButton = findViewById(R.id.check_back);
        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                leaveActivity();
            }
        });

        title.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                checkList.add(checkList.size() - 1, "");
                adapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    private void leaveActivity() {
        EditText title = findViewById(R.id.check_title);

        Intent sendData = new Intent();
        note.title = title.getText().toString();
        String bodyOutput = "";
        for(String x : checkList){
            bodyOutput += x + MainActivity.breakCode;
        }
        note.body = bodyOutput;
        sendData.putExtra("note", note);

        setResult(RESULT_OK, sendData);
        finish();
    }

    public void addCheck(View v){
        Log.d(TAG, "addCheck: ");
        checkList.add(checkList.size() - 1, "");
        adapter.notifyDataSetChanged();
    }
}
