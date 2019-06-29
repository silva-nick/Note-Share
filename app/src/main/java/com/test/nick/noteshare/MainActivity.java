package com.test.nick.noteshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
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
import android.view.animation.DecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemListener{
    private static final String TAG = "MainActivity";

    private ArrayList<String> test = new ArrayList<>();
    private NoteAdapter adapter;
    private int focusPosition;
    private View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting program");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);


        for (int i = 0; i < 100; i++) {
            test.add(String.valueOf(i));
        }
        adapter = new NoteAdapter(test, this);
        recyclerView.setAdapter(adapter);
        adapter.setListeners(this);
    }

    @Override
    protected void onResume(){
        Log.d(TAG, "onResume:" + focusView);

        if(focusView != null) {
            AnimatorSet resumeSet = new AnimatorSet();
            resumeSet.play(ObjectAnimator.ofFloat(focusView, View.X, 0))
                    .with(ObjectAnimator.ofFloat(focusView, View.Y, 0));
            resumeSet.setDuration(1000);
            resumeSet.setInterpolator(new DecelerateInterpolator());
            resumeSet.start();
        }
        super.onResume();
    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");

        int insertIndex = test.size();
        test.add(insertIndex, "");
        adapter.notifyItemInserted(insertIndex);
    }

    public void removeNote(int removeIndex){
        test.remove(removeIndex);
        adapter.notifyItemRemoved(removeIndex);
    }

    public void leaveActivity(){
        Intent intent = new Intent(this, StickyEditActivity.class);

        ActivityOptionsCompat option = ActivityOptionsCompat
                .makeScaleUpAnimation(focusView, 0, 0, focusView.getWidth(), focusView.getHeight());
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

        AnimatorSet set = new AnimatorSet();
        set.play(ObjectAnimator.ofFloat(focusView, View.X, xVelocity));
        set.setDuration(2000);
        set.setInterpolator(new DecelerateInterpolator());
        set.start();

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
