package demo.project.landmark.widgets.fcm;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import androidx.legacy.content.WakefulBroadcastReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import b.laixuantam.myaarlibrary.helper.MyLifecycleHandler;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.helper.MyNotification;
import b.laixuantam.myaarlibrary.model.BaseNotificationModel;
import demo.project.landmark.R;
import demo.project.landmark.activity.HomeActivity;
import demo.project.landmark.dependency.AppProvider;
import demo.project.landmark.event.AleartSignedEvent;

/**
 * Created by laixuantam on 5/9/17.
 */

public class FirebaseDataReceiver extends WakefulBroadcastReceiver {

    private final String TAG = "FirebaseCustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (TextUtils.isEmpty(AppProvider.getPreferences().getUserId())) {
            return;
        }

        boolean userHasLogin = AppProvider.getPreferences().checkLoginStatus();
        if (!userHasLogin) {
            return;
        }

        if (intent.getExtras() != null) {
            for (String key : intent.getExtras().keySet()) {
                Object value = intent.getExtras().get(key);
                MyLog.e(TAG, "Key: " + key + " Value: " + value);
            }
        }

        String title = intent.getExtras().get("notification_title").toString();

        String message = intent.getExtras().get("notification_body").toString();

        BaseNotificationModel model = new BaseNotificationModel();
        model.setTitle(title);
        model.setMessage(message);
        model.setTime(System.currentTimeMillis());

        if (MyLifecycleHandler.isApplicationInForeground()) {

//            MyNotification.getInstance().showCustomNotification(context, R.mipmap.ic_launcher, model);
            AleartSignedEvent.post();


        } else {
            //application running in background

            Bitmap defaultUser = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_user_default);

            MyNotification.getInstance().showCustomNotificationGoToActivity(context, R.mipmap.ic_launcher, HomeActivity.class, model, defaultUser);
        }

    }

    private void showDefaultNotification(Context context, String messageBody) {
        String dataNotification = messageBody;

        if (dataNotification == null || dataNotification.isEmpty()) {
            return;
        }

        try {
            JSONObject jsData = new JSONObject(dataNotification);

            String title = jsData.getString("notification_title");
            String message = jsData.getString("notification_body");

//            MyNotification.getInstance().showCustomNotification(context, title, message, R.mipmap.ic_launcher, dataNotification);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showNotification(Context context, String messageBody) {
        String dataNotification = messageBody;
        if (dataNotification == null || dataNotification.isEmpty()) {
            return;
        }

        if (dataNotification == null || dataNotification.isEmpty()) {
            return;
        }

        try {

            JSONObject jsData = new JSONObject(dataNotification);

            String title = jsData.getString("notification_title");
            String message = jsData.getString("notification_body");

//            MyNotification.getInstance().showCustomNotificationGoToActivity(context, title, message, R.mipmap.ic_launcher, FlySearchActivity.class, dataNotification);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
