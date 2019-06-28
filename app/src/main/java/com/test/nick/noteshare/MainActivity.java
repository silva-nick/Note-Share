package com.test.nick.noteshare;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemListener{
    private static final String TAG = "MainActivity";

    private List<CardView> testList = new ArrayList<CardView>();
    private ArrayList<String> test = new ArrayList<>();
    private RecyclerView recyclerView;
    private NoteAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private int focusPosition;
    private View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting program");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        testList.add((CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false));
        for (int i = 0; i < 100; i++) {
            test.add(String.valueOf(i));
        }
        adapter = new NoteAdapter(testList, test, this);
        recyclerView.setAdapter(adapter);
        adapter.setListeners(this);

    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");
        CardView newView = (CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false);
        int insertIndex = testList.size();
        testList.add(insertIndex, newView);
        test.add(insertIndex, "");
        adapter.notifyItemInserted(insertIndex);
    }

    public void leaveActivity(){
        Intent intent = new Intent(this, StickyEditActivity.class);

        ActivityOptionsCompat option = ActivityOptionsCompat.makeScaleUpAnimation(focusView, 0, 0, focusView.getWidth(), focusView.getHeight());
        ActivityCompat.startActivity(this, intent, option.toBundle());
    }


    @Override
    public void onItemTap() {
        Log.d(TAG, "onItemTap: ");
        leaveActivity();
    }

    @Override
    public void onItemFling(float xVelocity) {
        Log.d(TAG, "onItemFling: " + xVelocity);
        //animation + delete note
    }

    @Override
    public void onItemHold() {
        Log.d(TAG, "onItemHold: ");
        //use nfc to share notes
    }

    @Override
    public void sendEvent(View v, int pos){
        Log.d(TAG, "sendEvent: " + pos);
        this.focusPosition = pos;
        this.focusView = v;
    }
}
