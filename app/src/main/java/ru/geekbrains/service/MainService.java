package ru.geekbrains.service;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

// Служба
public class MainService extends IntentService {

    private int messageId;

    public MainService() {
        super("MainService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        makeNote("onHandleIntent");
    }

    @Override
    public void onCreate() {
        makeNote("onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        makeNote("onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        makeNote("onDestroy");
        super.onDestroy();
    }

    // вывод уведомления в строке состояния
    private void makeNote(String message) {
        // Идентификатор уведомления
        final int NOTIFY_ID = 101;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_HIGH);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        //.setSmallIcon(R.drawable.ic_action_message)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Main service notification")
                        .setContentText(message)
                        //.setTicker(text)
                        //.setNumber(totalMessages++)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND | NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS)
                        .setAutoCancel(true);

// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);
        //resultIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

        if (notificationManager != null) {
            notificationManager.notify(NOTIFY_ID, builder.build());
        }
    }
}
