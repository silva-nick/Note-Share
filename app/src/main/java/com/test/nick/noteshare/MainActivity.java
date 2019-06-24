package com.test.nick.noteshare;

import android.app.ActivityOptions;
import android.content.Intent;
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
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemClickListener{
    private static final String TAG = "MainActivity";

    private List<CardView> testList = new ArrayList<CardView>();
    private ArrayList<String> test = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting program");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inflater = getLayoutInflater();

        recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        testList.add((CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false));
        for (int i = 0; i < 100; i++) {
            test.add(String.valueOf(i));
        }
        adapter = new NoteAdapter(testList, test);
        recyclerView.setAdapter(adapter);
        ((NoteAdapter)adapter).setClickListener(this);
    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");
        CardView newView = (CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false);
        int insertIndex = testList.size();
        testList.add(insertIndex, newView);
        test.add(insertIndex, "");
        adapter.notifyItemInserted(insertIndex);
    }

    //Called on screen click, moves to HomeActivity
    public void leaveActivity(View view, int pos){
        Intent intent = new Intent(this, StickyEditActivity.class);

        //Errors out
        //Transition exitTransition = TransitionInflater.from(this).inflateTransition(R.transition.exit_transition);
        //getWindow().setExitTransition(exitTransition);
        getWindow().setExitTransition(new AutoTransition());
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.d(TAG, "onItemClick: position was clicked " + position);
        leaveActivity(view, position);
    }
}
