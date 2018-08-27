package demo.project.landmark.widgets.fcm;

import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import demo.project.landmark.dependency.AppProvider;

/**
 * Created by LaiXuanTam on 6/1/2016.
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        AppProvider.getPreferences().saveToken(refreshedToken);
        Log.e(TAG, "Refreshed token FCM: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.

        if (!TextUtils.isEmpty(token)) {
            if (AppProvider.getConnectivityHelper().hasInternetConnection()) {
                requestUpdateDeviceImei(token);
            }
        }
    }


    private void requestUpdateDeviceImei(String token) {

        String userID = AppProvider.getPreferences().getUserId();

        String deviceImei = AppProvider.getPreferences().getDeviceImei();

        if (TextUtils.isEmpty(userID)) {
            return;
        }

//        RequestUpdateDiviceImei.ApiParams params = new RequestUpdateDiviceImei.ApiParams();
//
//        params.device = deviceImei;
//        params.fcm_token = token;
//        params.id_user = userID;
//
//        AppProvider.getApiManagement().call(RequestUpdateDiviceImei.class, params, null);


    }
}
