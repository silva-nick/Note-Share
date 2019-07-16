package com.test.nick.noteshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nick.noteshare.data.Note;
import com.test.nick.noteshare.data.NoteViewModel;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemListener{
    private static final String TAG = "MainActivity";

    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private NoteAdapter adapter;
    private int focusPosition;
    private View focusView;

    private NoteViewModel bigData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: starting program");

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter(noteArrayList, this);
        recyclerView.setAdapter(adapter);
        adapter.setListeners(this);

        bigData = ViewModelProviders.of(this).get(NoteViewModel.class);
        bigData.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                // Update the cached copy of the words in the adapter.
                noteArrayList.addAll(0, notes);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //if the codes match the successcode, then changes have been made and the local data should be updated via data from the intent
        if (resultCode == RESULT_OK){
            Note note = noteArrayList.get(focusPosition);
            note.title = data.getStringExtra("new_title");
            note.body = data.getStringExtra("new_body");
            bigData.update(note);
            noteArrayList.remove(focusPosition);
            adapter.notifyItemRemoved(focusPosition);
            Log.d(TAG, "onActivityResult ================================ " + noteArrayList.size());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume:" + focusView);
        //when going back to main act, animates card
        if(focusView != null) {
            float startX = focusView.getX();
            float startY = focusView.getY();

            PropertyValuesHolder x1 = PropertyValuesHolder.ofFloat(View.X, 0f);
            PropertyValuesHolder y1 = PropertyValuesHolder.ofFloat(View.Y, 0f);
            PropertyValuesHolder x1Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, 2f);
            PropertyValuesHolder y1Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 15f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(focusView, x1, y1, x1Scale, y1Scale);
            objectAnimator1.setDuration(0);

            PropertyValuesHolder x2 = PropertyValuesHolder.ofFloat(View.X, startX);
            PropertyValuesHolder y2 = PropertyValuesHolder.ofFloat(View.Y, startY);
            PropertyValuesHolder x2Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
            PropertyValuesHolder y2Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(focusView, x2, y2, x2Scale, y2Scale);
            objectAnimator2.setDuration(400);
            objectAnimator2.setStartDelay(100);
            objectAnimator2.setInterpolator(new DecelerateInterpolator());

            AnimatorSet sequenceAnimator = new AnimatorSet();
            sequenceAnimator.playSequentially(objectAnimator1, objectAnimator2);
            sequenceAnimator.start();
        }
        super.onResume();
    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");

        final int insertIndex = 0;

        FloatingActionButton fab = findViewById(R.id.add_button);
        float x = fab.getX() + 22;
        float y = fab.getY();

        final ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.main_layout);

        FloatingActionButton addSticky = new FloatingActionButton(this);
        FloatingActionButton addCheck = new FloatingActionButton(this);
        FloatingActionButton addEvent = new FloatingActionButton(this);

        layout.addView(addSticky);
        layout.addView(addCheck);
        layout.addView(addEvent);

        addSticky.setImageDrawable(getDrawable(R.drawable.temp));
        addCheck.setImageDrawable(getDrawable(R.drawable.temp));
        addEvent.setImageDrawable(getDrawable(R.drawable.temp));

        addSticky.setSize(FloatingActionButton.SIZE_MINI);
        addSticky.setX(x);
        addSticky.setY(y);

        addCheck.setSize(FloatingActionButton.SIZE_MINI);
        addCheck.setX(x);
        addCheck.setY(y);

        addEvent.setSize(FloatingActionButton.SIZE_MINI);
        addEvent.setX(x);
        addEvent.setY(y);

        final Note testNote = new Note(981327402, "23asd", "example", "this is a noteArrayList", "");

        addSticky.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add stickynote and append to text
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v), 3);
            }
        });
        addCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add checknote and append to text
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v) - 1, 3);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add event and append to text
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v) - 2, 3);
            }
        });

        FlingAnimation fling = new FlingAnimation(addSticky, DynamicAnimation.Y);
        fling.setStartVelocity(-500)
                .setFriction(.75f)
                .start();
        FlingAnimation fling1 = new FlingAnimation(addCheck, DynamicAnimation.Y);
        fling1.setStartVelocity(-1000)
                .setFriction(.75f)
                .start();
        FlingAnimation fling2 = new FlingAnimation(addEvent, DynamicAnimation.Y);
        fling2.setStartVelocity(-1500)
                .setFriction(.75f)
                .start();
    }

    public void removeNote(int removeIndex){
        bigData.delete(noteArrayList.get(removeIndex));
        noteArrayList.remove(removeIndex);
        adapter.notifyItemRemoved(removeIndex);
    }

    public void leaveActivity(){
        Intent intent;

        switch ( noteArrayList.get(focusPosition).type ){
            case "1" :
                intent = new Intent(this, StickyEditActivity.class);
                break;
            case "3" :
                intent = new Intent(this, CheckEditActivity.class);
                break;
            default:
                intent = new Intent(this, StickyEditActivity.class);
        }

        ActivityOptionsCompat option = ActivityOptionsCompat
                .makeScaleUpAnimation(focusView, 0, 0, focusView.getWidth(), focusView.getHeight());
        ActivityCompat.startActivityForResult( this, intent, 69, option.toBundle());
    }


    @Override
    public void onItemTap() {
        Log.d(TAG, "onItemTap: ");
        leaveActivity();
    }

    @Override
    public void onItemFling(float xVelocity) {
        Log.d(TAG, "onItemFling: " + xVelocity);

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(focusView, View.X, xVelocity/2));
        set.setDuration(1000);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();
        set.reverse();

        removeNote(focusPosition);
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
