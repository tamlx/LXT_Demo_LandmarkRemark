package demo.project.landmark;

import android.content.Context;
import androidx.multidex.MultiDex;
import com.zxy.tiny.Tiny;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;
import b.laixuantam.myaarlibrary.database.orm.SugarApp;
import b.laixuantam.myaarlibrary.database.orm.SugarContext;
import b.laixuantam.myaarlibrary.helper.MyLifecycleHandler;
import b.laixuantam.myaarlibrary.helper.MyLog;
import demo.project.landmark.dependency.AppObjectProvider;
import demo.project.landmark.dependency.AppProvider;
import demo.project.landmark.dependency.ObjectProviderInterface;

@ReportsCrashes(

        // This is required for backward compatibility but not used
        mailTo = "ndxuantam10@gmail.com", formUri = "https://www.bugsense.com/api/acra?api_key=66b5a2b6",

        //show notification
        mode = ReportingInteractionMode.NOTIFICATION,
        //        resToastText = R.string.crash_toast_text // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resNotifTickerText = R.string.crash_notif_ticker_text, resNotifTitle = R.string.crash_notif_title, resNotifText = R.string.crash_notif_text, resNotifIcon = android.R.drawable.stat_notify_error,
        //
        //        //show dialog with user comment
        ////        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.crash_dialog_text, resDialogIcon = android.R.drawable.ic_dialog_info, resDialogTitle = R.string.crash_dialog_title,
        //        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt,
        resDialogOkToast = R.string.crash_dialog_ok_toast

)
public class MyApplication extends SugarApp {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        // Đăng ký ActivityLifecycleCallbacks để kiểm tra application có background running hay visible on screen
        registerActivityLifecycleCallbacks(new MyLifecycleHandler());

        MultiDex.install(this);
        MyLog.isEnableLog = true;

        ObjectProviderInterface objectProviderInterface = new AppObjectProvider(this);
        AppProvider.init(objectProviderInterface);

        SugarContext.init(getApplicationContext());

        ACRA.init(this);

        Tiny.getInstance().init(this);
    }


}
