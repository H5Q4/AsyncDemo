package com.github.geoffreyhuang.asyncdemo.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.Telephony;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.github.geoffreyhuang.asyncdemo.R;

import java.util.Locale;


public class CountMsgsIntentService extends IntentService {

    public static final String KEY_NUMBER = "number";
    public static final String PENDING_RESULT = "pending_result";
    public static final String RESULT = "result";
    public static final int RESULT_CODE = "countOfMsgs".hashCode();

    public CountMsgsIntentService() {
        super("CountMsgsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String phoneNumber = intent.getStringExtra(KEY_NUMBER);
        Cursor cursor = getMsgsFrom(phoneNumber);
        int countOfMsgs = cursor.getCount();
        notifyUser(phoneNumber, countOfMsgs);
        Intent result = new Intent();
        result.putExtra(RESULT, countOfMsgs);
        PendingIntent reply = intent.getParcelableExtra(PENDING_RESULT);
        try {
            reply.send(this, RESULT_CODE, result);
        } catch (PendingIntent.CanceledException e) {
            Log.e("CountMsgsIntentService", "reply cancelled: ", e);
        }
    }

    private void notifyUser(String phoneNumber, int countOfMsgs) {
        String msg = String.format(Locale.CHINA,
            "Found %d msgs from %s",
            countOfMsgs, phoneNumber);
        Notification.Builder builder
            = new Notification.Builder(this)
            .setSmallIcon(android.R.drawable.ic_notification_overlay)
            .setContentTitle("Inbox Counter")
            .setContentText(msg);
        NotificationManager nm
            = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        nm.notify(phoneNumber.hashCode(), builder.build());
    }

    private Cursor getMsgsFrom(String phoneNumber) {
        String[] projection = {
            Telephony.Sms._ID,
            Telephony.Sms.ADDRESS,
            Telephony.Sms.BODY
        };
        String selection = Telephony.Sms.ADDRESS + " = '" + phoneNumber +"'";
        Uri qUri = Uri.parse("content://sms/inbox");
        return getContentResolver().query(
            qUri,
            projection,
            selection,
            null,
            null
        );
    }
}
