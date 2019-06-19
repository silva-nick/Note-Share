package com.test.nick.noteshare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private List<CardView> testList = new ArrayList<CardView>();
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

        recyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        testList.add((CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false));

        adapter = new NoteAdapter(testList);
        recyclerView.setAdapter(adapter);
    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");
        CardView newView = (CardView) LayoutInflater.from(this).inflate(R.layout.sticky_note, (ViewGroup) findViewById(R.id.recycle_view), false);
        int insertIndex = testList.size();
        testList.add(insertIndex, newView);
        adapter.notifyItemInserted(insertIndex);
    }
}
