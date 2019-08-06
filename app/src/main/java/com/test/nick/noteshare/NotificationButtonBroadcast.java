package com.test.nick.noteshare;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.test.nick.noteshare.data.Note;

public class NotificationButtonBroadcast extends BroadcastReceiver {
    public static final String NOTIFICATION_ID = "test_id";
    public static final String NOTIFICATION = "notification";
    public static final String TAG = "NotificationButtonBroadcast";

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        int id = intent.getIntExtra(NOTIFICATION_ID, 0);
        String action = intent.getStringExtra("action");
        long time = intent.getLongExtra("time", 0);
        Note note = intent.getParcelableExtra("note");


        if(action.equals("snooze")){
            notificationManager.cancelAll();
            NotificationCreator nCreator = new NotificationCreator(context, note, time + 1000 * 60 * 15, NotificationCreator.NoteFrequency.LOW);
        }
        else if(action.equals("dismiss")) {
            notificationManager.cancelAll();
        }
        else if(action.equals("completed")){
            Intent i = new Intent(MainActivity.ACTION_UPDATE_GOAL);
            String [] extra = note.extra.split(MainActivity.breakCode);
            note.extra = Integer.parseInt(extra[0]) + 1 + MainActivity.breakCode + extra[1];
            i.putExtra("note", note);
            LocalBroadcastManager.getInstance(context).sendBroadcast(i);
            notificationManager.cancelAll();
        }
    }
}
