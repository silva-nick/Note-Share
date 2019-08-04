package com.test.nick.noteshare;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.test.nick.noteshare.data.Note;
import com.test.nick.noteshare.data.NoteViewModel;

import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements  NoteAdapter.ItemListener,
                    NfcAdapter.OnNdefPushCompleteCallback,
                    NfcAdapter.CreateNdefMessageCallback{

    private static final String TAG = "MainActivity";
    public static final String breakCode = "x92n";

    private ArrayList<Note> noteArrayList = new ArrayList<>();
    private NoteAdapter adapter;
    private int focusPosition;
    private View focusView;
    private NoteViewModel bigData;

    private NfcAdapter adapterNfc;
    private boolean isRead = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: starting program");

        RecyclerView recyclerView = findViewById(R.id.recycle_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new NoteAdapter(noteArrayList, this);
        recyclerView.setAdapter(adapter);
        adapter.setListeners(this);

        bigData = ViewModelProviders.of(this).get(NoteViewModel.class);
        bigData.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                noteArrayList.clear(); //This is bad programming :)
                noteArrayList.addAll(0, notes);
                adapter.notifyDataSetChanged();
                Log.d(TAG, "onChanged: -------------------------" + noteArrayList.size());
            }
        });

        adapterNfc = NfcAdapter.getDefaultAdapter(this);
        if(adapterNfc != null) {
            adapterNfc.setOnNdefPushCompleteCallback(this, this);
            adapterNfc.setNdefPushMessageCallback(this, this);
        }
        else {
            Toast.makeText(this, "Your infantile device does not have NFC capabilities",
                    Toast.LENGTH_SHORT).show();
        }
        handleNfcIntents(getIntent());
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //if the codes match the successCode, then changes have been made and the local data should be updated via data from the intent
        if (resultCode == RESULT_OK){
            Note note = data.getParcelableExtra("note");
            bigData.update(note);
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume:" + focusView);

        //when going back to main act, animates card
        if(focusView != null) {
            float startX = focusView.getX();
            float startY = focusView.getY();
            float startZ = focusView.getZ();

            PropertyValuesHolder x1 = PropertyValuesHolder.ofFloat(View.X, 0f);
            PropertyValuesHolder y1 = PropertyValuesHolder.ofFloat(View.Y, 0f);
            PropertyValuesHolder x1Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, 4f);
            PropertyValuesHolder y1Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 15f);
            PropertyValuesHolder z1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, 100f);
            ObjectAnimator objectAnimator1 = ObjectAnimator.ofPropertyValuesHolder(focusView, x1, y1, x1Scale, y1Scale, z1);
            objectAnimator1.setDuration(0);

            PropertyValuesHolder x2 = PropertyValuesHolder.ofFloat(View.X, startX);
            PropertyValuesHolder y2 = PropertyValuesHolder.ofFloat(View.Y, startY);
            PropertyValuesHolder x2Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, 1f);
            PropertyValuesHolder y2Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1f);
            PropertyValuesHolder z2 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Z, startZ);
            ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(focusView, x2, y2, x2Scale, y2Scale, z2);
            objectAnimator2.setDuration(500);
            objectAnimator2.setStartDelay(100);
            objectAnimator2.setInterpolator(new DecelerateInterpolator());

            AnimatorSet sequenceAnimator = new AnimatorSet();
            sequenceAnimator.playSequentially(objectAnimator1, objectAnimator2);
            sequenceAnimator.start();
        }

        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        IntentFilter techDetected = new IntentFilter(NfcAdapter.ACTION_TECH_DISCOVERED);
        IntentFilter[] nfcIntentFilter = new IntentFilter[]{techDetected,tagDetected,ndefDetected};

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        if(adapterNfc!= null)
            adapterNfc.enableForegroundDispatch(this, pendingIntent, nfcIntentFilter, null);

        handleNfcIntents(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(adapterNfc!= null)
            adapterNfc.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleNfcIntents(intent);
    }

    public void addNote(View view){
        Log.d(TAG, "addNote: Floating action button pressed");

        Log.d(TAG, "onCreate: " + findViewById(R.id.recycle_view).getWidth());

        final int insertIndex = 0;

        FloatingActionButton fab = findViewById(R.id.add_button);
        float x = fab.getX() + 22;
        float y = fab.getY();

        final ConstraintLayout layout = (ConstraintLayout)findViewById(R.id.main_layout);

        FloatingActionButton addSticky = new FloatingActionButton(this);
        FloatingActionButton addCheck = new FloatingActionButton(this);
        FloatingActionButton addEvent = new FloatingActionButton(this);
        FloatingActionButton addGoal = new FloatingActionButton(this);

        layout.addView(addSticky);
        layout.addView(addCheck);
        layout.addView(addEvent);
        layout.addView(addGoal);

        addSticky.setImageDrawable(getDrawable(R.drawable.temp));
        addCheck.setImageDrawable(getDrawable(R.drawable.temp));
        addEvent.setImageDrawable(getDrawable(R.drawable.temp));
        addGoal.setImageDrawable(getDrawable(R.drawable.temp));

        addSticky.setSize(FloatingActionButton.SIZE_MINI);
        addSticky.setX(x);
        addSticky.setY(y);

        addCheck.setSize(FloatingActionButton.SIZE_MINI);
        addCheck.setX(x);
        addCheck.setY(y);

        addEvent.setSize(FloatingActionButton.SIZE_MINI);
        addEvent.setX(x);
        addEvent.setY(y);

        addGoal.setSize(FloatingActionButton.SIZE_MINI);
        addGoal.setX(x);
        addGoal.setY(y);

        final Note testNote = new Note(0, "", "", "");

        addSticky.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add stickynote and append to text
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v), 4);
            }
        });
        addCheck.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add checknote and append to text
                testNote.type = 1;
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v) - 1, 4);
            }
        });
        addEvent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add event and append to text
                testNote.type = 2;
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v) - 2, 4);
            }
        });
        addGoal.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //add event and append to text
                testNote.type = 3;
                bigData.insert(testNote);
                layout.removeViews(layout.indexOfChild(v) - 3, 4);
            }
        });

        FlingAnimation fling = new FlingAnimation(addSticky, DynamicAnimation.Y);
        fling.setStartVelocity(-500)
                .setFriction(.75f)
                .start();
        fling = new FlingAnimation(addCheck, DynamicAnimation.Y);
        fling.setStartVelocity(-1000)
                .setFriction(.75f)
                .start();
        fling = new FlingAnimation(addEvent, DynamicAnimation.Y);
        fling.setStartVelocity(-1500)
                .setFriction(.75f)
                .start();
        fling = new FlingAnimation(addGoal, DynamicAnimation.Y);
        fling.setStartVelocity(-2000)
                .setFriction(.75f)
                .start();
    }

    public void removeNote(int removeIndex){
        Log.d(TAG, "removeNote: deleting a note");
        bigData.delete(noteArrayList.get(removeIndex));
        noteArrayList.remove(removeIndex);
        adapter.notifyItemRemoved(removeIndex);
    }

    public void leaveActivity(){
        Intent intent;
        Note focusNote = noteArrayList.get(focusPosition);

        switch ( noteArrayList.get(focusPosition).type ){
            case 0 :
                intent = new Intent(this, StickyEditActivity.class);
                break;
            case 1 :
                intent = new Intent(this, CheckEditActivity.class);
                break;
            case 2 :
                intent = new Intent(this, EventEditActivity.class);
                break;
            case 3 :
                intent = new Intent(this, GoalEditActivity.class);
                break;
            default:
                intent = new Intent(this, StickyEditActivity.class);
        }

        intent.putExtra("note", focusNote);

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
        //set.reverse();
        removeNote(focusPosition);
        //animation + delete note
    }

    @Override
    public void onItemHold() {
        Log.d(TAG, "onItemHold: ");
        //use nfc to share notes
        isRead = false;

        View layout = findViewById(R.id.main_layout);
        final View grey = findViewById(R.id.grey_view);
        grey.setVisibility(View.VISIBLE);
        grey.animate().alpha(.4f);
        grey.setTranslationZ(.01f);

        final CardView v = (CardView) focusView;

        ((ViewGroup)v.getParent()).removeView(v);
        ((ViewGroup)layout).addView(v);

        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //send nfc message
            }
        });

        grey.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                grey.setVisibility(View.INVISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });


        PropertyValuesHolder x2Scale = PropertyValuesHolder.ofFloat(View.SCALE_X, .75f);
        PropertyValuesHolder y2Scale = PropertyValuesHolder.ofFloat(View.SCALE_Y, .75f);
        PropertyValuesHolder x2 = PropertyValuesHolder.ofFloat(View.X, (float)(v.getWidth()/2 - focusView.getWidth()/2));
        PropertyValuesHolder y2 = PropertyValuesHolder.ofFloat(View.Y, 650f);
        PropertyValuesHolder z2 = PropertyValuesHolder.ofFloat(View.Z, 5000f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofPropertyValuesHolder(v, x2Scale, x2, y2, y2Scale, z2);
        objectAnimator2.setDuration(500);
        objectAnimator2.setStartDelay(100);
        objectAnimator2.setInterpolator(new DecelerateInterpolator());

        AnimatorSet sequenceAnimator = new AnimatorSet();
        sequenceAnimator.play(objectAnimator2);
        sequenceAnimator.start();
    }

    @Override
    public void sendEvent(View v, int pos){
        Log.d(TAG, "sendEvent: " + pos);
        this.focusPosition = pos;
        this.focusView = v;
    }

    @Override
    public void onNdefPushComplete(NfcEvent event){
        //Message successfully sent
        Toast.makeText(this, "Note Send!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public NdefMessage createNdefMessage (NfcEvent event){
        //called when nfc device is registered
        Log.d(TAG, "createNdefMessage:  Called to create ndef message");
        NdefRecord[] recordsToAttach = createRecords();
        return new NdefMessage(recordsToAttach);
    }

    public NdefRecord[] createRecords() {

        NdefRecord[] records = new NdefRecord[6];
        Note sendNote = noteArrayList.get(focusPosition);

        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(sendNote.nid);
        byte[] payload = bb.array();
        NdefRecord record = NdefRecord.createMime("text/plain", payload);
        records[0] = record;

        bb.clear();
        bb.putInt(sendNote.type);
        payload = bb.array();
        record = NdefRecord.createMime("text/plain", payload);
        records[1] = record;

        payload = sendNote.title.getBytes(Charset.forName("UTF-8"));
        record = NdefRecord.createMime("text/plain", payload);
        records[2] = record;

        payload = sendNote.body.getBytes(Charset.forName("UTF-8"));
        record = NdefRecord.createMime("text/plain", payload);
        records[3] = record;

        payload = sendNote.extra.getBytes(Charset.forName("UTF-8"));
        record = NdefRecord.createMime("text/plain", payload);
        records[4] = record;

        records[5] = NdefRecord.createApplicationRecord(getPackageName());
        return records;
    }

    private void handleNfcIntents(Intent intent){
        Log.d(TAG, "handleNfcIntents: --------------------------------------------------------------------------------");
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if(tag != null) {
            Ndef ndef = Ndef.get(tag);

            if(isRead){
                try {
                    ndef.connect();
                    Log.d(TAG, "handleNfcIntents: Trying to READ from tag");
                    NdefMessage message = ndef.getNdefMessage();
                    NdefRecord[] records = message.getRecords();

                    Note tagNote = getNoteFromNfc(records);
                    bigData.insert(tagNote);

                    String string = new String(message.getRecords()[2].getPayload());
                    Toast.makeText(this, string, Toast.LENGTH_LONG).show();
                    //create new note with payloads
                    ndef.close();
                } catch (IOException | FormatException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    isRead = true;
                    ndef.connect();
                    Log.d(TAG, "handleNfcIntents: Trying to WRITE to tag");
                    NdefRecord[] recordsToAttach = createRecords();
                    ndef.writeNdefMessage(new NdefMessage(recordsToAttach));
                    ndef.close();
                } catch (IOException | FormatException e) {
                    e.printStackTrace();
                }
            }
        }


        /*if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Log.d(TAG, "handleNfcIntents: AHDAEHGAIEI/egHsdoifleah;nselioubvasiudbv[iah");
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage receivedMessage = (NdefMessage) rawMessages[0];
                NdefRecord[] attachedRecords = receivedMessage.getRecords();

                for (int i = 0; i < 5; i++) {
                    Log.d(TAG, "onNewIntent: " + attachedRecords[i].toString());
                }

            }
        } else if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())){
            //unformatted tag
            if(nfcPosition != -1){
                //write to the tag
            }
            Toast.makeText(this, "UNFORMATTED TAG FOUND", Toast.LENGTH_SHORT).show();
        }*/
    }

    private Note getNoteFromNfc(NdefRecord[] records){
        Note output = new Note(-1, "", "", "");

        output.type = new BigInteger(records[1].getPayload()).intValue();
        output.title = new String(records[3].getPayload());
        output.body = new String(records[4].getPayload());
        output.extra = new String(records[5].getPayload());

        return output;
    }
}
