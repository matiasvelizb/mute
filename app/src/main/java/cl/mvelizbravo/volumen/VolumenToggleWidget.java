package cl.mvelizbravo.volumen;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

/**
 * Ringer Volume Toggle Widget.
 *
 * @author Matias
 */
public class VolumenToggleWidget extends AppWidgetProvider {

    private static final String ACTION_SET_NORMAL_MODE = "action.SET_NORMAL_MODE";
    private static final String ACTION_SET_VIBRATE_MODE = "action.SET_VIBRATE_MODE";
    private static final String TAG = "Volumen";

    private AudioManager manager;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        ComponentName volumeToggleWidget;
        volumeToggleWidget = new ComponentName(context, VolumenToggleWidget.class);

        RemoteViews views;
        views = new RemoteViews(context.getPackageName(), R.layout.volumen_widget);

        appWidgetManager.updateAppWidget(volumeToggleWidget, views);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName volumeToggleWidget;
        volumeToggleWidget = new ComponentName(context, VolumenToggleWidget.class);
        RemoteViews views;
        views = new RemoteViews(context.getPackageName(), R.layout.volumen_widget);

        Log.d(TAG, "getAction(): " + intent.getAction());
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        if (ACTION_SET_NORMAL_MODE.equals(intent.getAction())) {
            Log.d(TAG, "Changed to: RINGER_MODE_NORMAL");
            manager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            manager.setStreamVolume(AudioManager.STREAM_RING, manager.getStreamMaxVolume(AudioManager.STREAM_RING), 0);
            views.setImageViewResource(R.id.widget_button, R.drawable.normal);
            Toast.makeText(context, "Sonido", Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, "Changed to: RINGER_MODE_VIBRATE");
            manager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            views.setImageViewResource(R.id.widget_button, R.drawable.vibrate);
            Toast.makeText(context, "Vibración", Toast.LENGTH_SHORT).show();
        }

        CharSequence charSequence = context.getString(R.string.mama);
        views.setTextViewText(R.id.widget_text, charSequence);
        views.setOnClickPendingIntent(R.id.widget_button, getPendingSelfIntent(context));

        appWidgetManager.updateAppWidget(volumeToggleWidget, views);
    }

    private PendingIntent getPendingSelfIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        String ringerMode = Integer.toString(manager.getRingerMode());
        Log.d(TAG, "getRingerMode(): " + ringerMode);

        if (manager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            // Set ringer stream to vibrate mode
            intent.setAction(ACTION_SET_VIBRATE_MODE);
        } else {
            // Set ringer stream to normal mode
            intent.setAction(ACTION_SET_NORMAL_MODE);
        }

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
