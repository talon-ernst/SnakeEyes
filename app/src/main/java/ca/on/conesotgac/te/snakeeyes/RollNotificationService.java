package ca.on.conesotgac.te.snakeeyes;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import java.util.Timer;
import java.util.TimerTask;

public class RollNotificationService extends Service {
    public RollNotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        //Declare application for future use
        final SnakeEyesApplication application = ((SnakeEyesApplication) getApplication());
        //Get the last user roll
        String lastRollText = " " + application.SEGetLastRoll("player_roll");
        //Constants for id and timer delay
        final int rollNotifId = 0;
        final int notificationDelay = 10000;
        //timer declaration
        final Timer notificationTimer = new Timer(true);
        //declaration of notification
        final NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        final Intent rollNotifIntent = new Intent(getApplicationContext(), MainActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                rollNotifId, rollNotifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final Notification rollNotification =  new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle(getString(R.string.roll_notif_title))
                .setContentText(getString(R.string.roll_notif_body) + lastRollText)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();


        notificationTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                manager.notify(rollNotifId, rollNotification);

                stopSelf();
            }
        }, notificationDelay);
        super.onCreate();
    }
}