package com.test.nick.noteshare;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationManagerCompat;

import com.test.nick.noteshare.data.Note;

public class NotificationCreator {
    public static final String TAG = "NotificationCreator";

    private static final String CHANNEL_ID = "disney_channel";

    private Context context;
    private Note note;
    private long startTime;
    private NoteFrequency frequency;

    public NotificationCreator(Context c, Note note, long time, NotificationCreator.NoteFrequency frequency){
        this.context = c;
        this.note = note;
        this.startTime = time;
        this.frequency = frequency;
    }

    public void createNotification(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "thicc notification";
            String description = "doubt this matters";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent intent = new Intent(this.context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this.context, 0, intent, 0);

        //intent = new Intent(this.context, MainActivity.class);
        //intent.setAction(ACTION_SNOOZE);

        Notification.Builder builder = new Notification.Builder(this.context, CHANNEL_ID)
            .setSmallIcon(R.drawable.temp)
            .setContentTitle(note.title)
            .setContentText(note.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        Intent notificationIntent = new Intent(this.context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, 1);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this.context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        //time = SystemClock.elapsedRealtime() + 10000;
        startTime -= System.currentTimeMillis() - SystemClock.elapsedRealtime();
        Log.d(TAG, "createNotification: ==========" + (SystemClock.elapsedRealtime() - startTime) / 1000);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, startTime, broadcastIntent);
    }

    public enum NoteFrequency{
        HIGH,
        MEDIUM,
        LOW,
        HOURLY,
        DAILY
    }
}
