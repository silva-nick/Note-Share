package com.test.nick.noteshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.animation.DynamicAnimation;
import android.support.animation.FlingAnimation;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NoteAdapter.ItemListener{
    private static final String TAG = "MainActivity";

    private static final String FILE_NAME = "notes";
    private static final String PREFERENCE = "first";

    private ArrayList<String> test = new ArrayList<>();
    private NoteAdapter adapter;
    private int focusPosition;
    private View focusView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: starting program");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (preferences.getBoolean(PREFERENCE, true)){
            //write data to file
            try {
                FileOutputStream outputStream = openFileOutput(FILE_NAME, Context.MODE_PRIVATE);
                String message = "DEFAULT MESSAGE";
                outputStream.write(message.getBytes());
                outputStream.close();
            } catch (Exception e){
                e.printStackTrace();
            }
            preferences.edit().putBoolean(PREFERENCE, false).commit();
        }

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //if the codes match the successcode, then changes have been made and the local data should be updated via data from the intent
        if (resultCode == RESULT_OK){
            ((TextView)findViewById(R.id.title_text)).setText(data.getStringExtra("new_title"));
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
            PropertyValuesHolder y1Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 10f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(focusView, x1, y1, x1Scale, y1Scale);
            objectAnimator1.setDuration(0);

            PropertyValuesHolder x2 = PropertyValuesHolder.ofFloat(View.X, startX);
            PropertyValuesHolder y2 = PropertyValuesHolder.ofFloat(View.Y, startY);
            PropertyValuesHolder x2Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
            PropertyValuesHolder y2Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(focusView, x2, y2, x2Scale, y2Scale);
            objectAnimator2.setDuration(500);
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

        addSticky.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add stickynote and append to text
                test.add(insertIndex, "");
                adapter.notifyItemInserted(insertIndex);
                writeRead();
                layout.removeView(v);
            }
        });

        FlingAnimation fling = new FlingAnimation(addSticky, DynamicAnimation.Y);
        fling.setStartVelocity(-500)
                .setFriction(1f)
                .start();
        FlingAnimation fling1 = new FlingAnimation(addCheck, DynamicAnimation.Y);
        fling1.setStartVelocity(-1000)
                .setFriction(1f)
                .start();
        FlingAnimation fling2 = new FlingAnimation(addEvent, DynamicAnimation.Y);
        fling2.setStartVelocity(-1500)
                .setFriction(1f)
                .start();

        //writeRead();
    }

    private void writeRead(){
        //Appending the file
        try {
            FileOutputStream outputStream = openFileOutput(FILE_NAME, Context.MODE_APPEND);
            String message = "NEW APPENDED NOTE";
            outputStream.write(message.getBytes());
            outputStream.close();
        } catch (Exception e){
            e.printStackTrace();
        }


        //Reading from the file
        File file = new File(getFilesDir(), FILE_NAME);

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        ((TextView)findViewById(R.id.title_text)).setText(text.toString());
    }

    public void removeNote(int removeIndex){
        test.remove(removeIndex);
        adapter.notifyItemRemoved(removeIndex);
    }

    public void leaveActivity(){
        Intent intent = new Intent(this, StickyEditActivity.class);

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
