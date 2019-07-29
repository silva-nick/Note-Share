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

import com.test.nick.noteshare.data.Note;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class NotificationCreator {
    public static final String TAG = "NotificationCreator";

    private static final String CHANNEL_ID = "disney_channel";

    private static final long MILLI_HOUR = 3600000;

    private Context context;
    private Note note;
    private long startTime;
    private NoteFrequency frequency;

    public NotificationCreator(Context c, Note note, String[]date, String[]time, NotificationCreator.NoteFrequency frequency){
        this.context = c;
        this.note = note;
        this.frequency = frequency;

        LocalDateTime test = LocalDateTime.of(Integer.parseInt(date[2]), Integer.parseInt(date[1]),
                Integer.parseInt(date[0]), Integer.parseInt(time[0]), Integer.parseInt(time[1]), 0);
        ZonedDateTime zdt = test.atZone(ZoneId.of("America/Chicago"));
        this.startTime = zdt.toInstant().toEpochMilli();
        this.startTime -= System.currentTimeMillis() - SystemClock.elapsedRealtime();

        if(frequency.getFreq() == 1 || frequency.getFreq() == 2 || frequency.getFreq() == 3){
            createNotificationSet(startTime);
        }
    }

    public NotificationCreator(Context c, Note note, long time, NotificationCreator.NoteFrequency frequency){
        this.context = c;
        this.note = note;
        this.frequency = frequency;
        this.startTime = time - System.currentTimeMillis() + SystemClock.elapsedRealtime();

        if(frequency.getFreq() == 1 || frequency.getFreq() == 2 || frequency.getFreq() == 3){
            createNotificationSet(startTime);
        }
    }

    public void createNotificationSet(long time){
        if(time>SystemClock.elapsedRealtime()) {
            switch (frequency.getFreq()) {
                case 1:
                    createNotification(time);
                    break;
                case 2:
                    createNotification(time);
                    createNotification((SystemClock.elapsedRealtime() - time + MILLI_HOUR > 0)
                            ? (time + SystemClock.elapsedRealtime()) / 2 : time - MILLI_HOUR);
                    break;

                case 3:
                    createNotification(time);
                    createNotification((SystemClock.elapsedRealtime() - time + MILLI_HOUR > 0)
                            ? (time + SystemClock.elapsedRealtime()) / 2 : time - MILLI_HOUR);
                    createNotification((SystemClock.elapsedRealtime() - time + 24 * MILLI_HOUR > 0)
                            ? (time + SystemClock.elapsedRealtime()) * 3 / 4 : time - 24 * MILLI_HOUR);
                    break;
                default:
                    createNotification(time);
            }
        }
        else {createNotification(time);}
    }

    public void createNotification(long time){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "thicc notification";
            String description = "doubt this matters";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel( CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent snoozeIntent = new Intent(this.context, NotificationButtonBroadcast.class);
        snoozeIntent.putExtra("action", "snooze")
            .putExtra("time", time)
            .putExtra("note", note);
        PendingIntent pendingSnoozeIntent = PendingIntent.getBroadcast(this.context, 20, snoozeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent dismissIntent = new Intent(this.context, NotificationButtonBroadcast.class);
        dismissIntent.putExtra("action", "dismiss")
                .putExtra("time", time)
                .putExtra("note", note);
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(this.context, 1230, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this.context, CHANNEL_ID)
            .setSmallIcon(R.drawable.temp)
            .setContentTitle(note.title)
            .setContentText(getDescription(note.body))
            .addAction(R.drawable.temp, "Snooze", pendingSnoozeIntent)
            .addAction(R.drawable.temp, "Dismiss", pendingDismissIntent)
            .setAutoCancel(true);

        Intent notificationIntent = new Intent(this.context, NotificationPublisher.class);
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION_ID, (int)(Math.random()*1000));              ///THSI IS REALLY BROKEN
        notificationIntent.putExtra(NotificationPublisher.NOTIFICATION, builder.build());
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(this.context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Log.d(TAG, "createNotification: ==========" + (SystemClock.elapsedRealtime() - time) / 1000);
        AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, time, broadcastIntent);
    }

    private String getDescription(String x){
        String [] temp = x.split(MainActivity.breakCode);
        return temp[3];
    }

    public enum NoteFrequency{
        HIGH(3),
        MEDIUM(2),
        LOW(1),
        HOURLY(60),
        DAILY(24),
        WEEKLY(7);

        private final int freq;

        private NoteFrequency(int freqCode) {
            this.freq = freqCode;
        }

        public int getFreq() {
            return this.freq;
        }
    }
}
