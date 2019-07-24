package com.test.nick.noteshare;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationManagerCompat;

import com.test.nick.noteshare.data.Note;

public class NotificationCreator {
    private static final String CHANNEL_ID = "disney_channel";

    private Context context;
    private Note note;
    private String time;
    private NoteFrequency frequency;

    public NotificationCreator(Context c, Note note, String time, NotificationCreator.NoteFrequency frequency){
        this.context = c;
        this.note = note;
        this.time = time;
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

        Notification.Builder builder = new Notification.Builder(this.context, CHANNEL_ID)
            .setSmallIcon(R.drawable.temp)
            .setContentTitle(note.title)
            .setContentText(note.body)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(123, builder.build());
    }

    public enum NoteFrequency{
        HIGH,
        MEDIUM,
        LOW
    }
}
