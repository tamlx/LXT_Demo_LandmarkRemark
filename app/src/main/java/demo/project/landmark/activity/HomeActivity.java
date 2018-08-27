package demo.project.landmark.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import b.laixuantam.myaarlibrary.base.BaseFragment;
import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.event.GetCurrentPositionSuccess;
import b.laixuantam.myaarlibrary.event.UserAcceptLocationPermissionEvent;
import b.laixuantam.myaarlibrary.helper.OnKeyboardVisibilityListener;
import b.laixuantam.myaarlibrary.helper.map.location.LocationHelper;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialModel;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialView;
import demo.project.landmark.R;
import demo.project.landmark.event.KeyboardInEvent;
import demo.project.landmark.event.KeyboardOutEvent;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import demo.project.landmark.ui.views.activity.home_activity.HomeActivityView;
import demo.project.landmark.ui.views.activity.home_activity.HomeActivityViewCallback;
import demo.project.landmark.ui.views.activity.home_activity.HomeActivityViewInterface;

public class HomeActivity extends BaseFragmentActivity<HomeActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements HomeActivityViewCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, ActivityCompat.OnRequestPermissionsResultCallback, OnKeyboardVisibilityListener {

    private static final String TAG = HomeActivity.class.getName();
    public static LocationHelper locationHelper;

    @Override
    protected void initialize(Bundle bundle) {
        view.init(this, this);

        locationHelper = new LocationHelper(this);
        locationHelper.checkpermission();

        setKeyboardVisibilityListener(this);

        handler.postDelayed(() -> {
            showHomeTimKiemTutorial();
        }, 2000);

    }

    private void setKeyboardVisibilityListener(final OnKeyboardVisibilityListener onKeyboardVisibilityListener) {
        final View parentView = ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        parentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            private boolean alreadyOpen;
            private final int defaultKeyboardHeightDP = 100;
            private final int EstimatedKeyboardDP = defaultKeyboardHeightDP + (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP ? 48 : 0);
            private final Rect rect = new Rect();

            @Override
            public void onGlobalLayout() {
                int estimatedKeyboardHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, EstimatedKeyboardDP, parentView.getResources().getDisplayMetrics());
                parentView.getWindowVisibleDisplayFrame(rect);
                int heightDiff = parentView.getRootView().getHeight() - (rect.bottom - rect.top);
                boolean isShown = heightDiff >= estimatedKeyboardHeight;

                if (isShown == alreadyOpen) {
                    return;
                }
                alreadyOpen = isShown;
                onKeyboardVisibilityListener.onVisibilityChanged(isShown);
            }
        });
    }

    @Override
    public void onVisibilityChanged(boolean visible) {
        if (visible) {
            KeyboardInEvent.post();
        } else {
            KeyboardOutEvent.post();
        }
    }

    // Permission check functions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // redirects to utils
        locationHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        locationHelper.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected void onDestroy() {

        locationHelper = null;

        Runtime.getRuntime().gc();

        if (view != null)
            view.destroyMapFragment();

        super.onDestroy();

    }

    @Override
    protected HomeActivityViewInterface getViewInstance() {
        return new HomeActivityView();
    }

    @Override
    protected BaseMainActionbarViewInterface getActionbarInstance() {
        return null;
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.home_activity_container;
    }


    @Override
    protected void onResume() {
        super.onResume();
        locationHelper.checkPlayServices();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationHelper.requestGetLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        locationHelper.connectApiClient();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGetCurrentLocationSuccess(GetCurrentPositionSuccess event) {
        if (view != null) {
            dismissProgress();
            if (locationHelper != null && locationHelper.getmLastLocation() != null) {
                try {
                    locationHelper.getAddress(locationHelper.getmLastLocation().getLatitude(), locationHelper.getmLastLocation().getLongitude());
                    setDataLocation(locationHelper.getmLastLocation());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                showToast(R.string.txt_error_get_current_location);
            }


        }
    }

    @SuppressWarnings("unused")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUserAcceptLocationPermissionEvent(UserAcceptLocationPermissionEvent event) {
        if (view != null) {
            showProgress(getString(R.string.loading));
        }
    }

    public void setDataLocation(Location location) {

        if (location != null) {
//            requestDataByCurrentLocationAndCategory(location, "");
            view.setCurrentLocation(location);
        }
    }

    public void showLayoutMap() {
        showHeaderAndFooterHome();
    }

    public void reloadGetCurrentLocation() {
        if (locationHelper != null && locationHelper.getmLastLocation() == null) {
            locationHelper.checkpermission();
            locationHelper.reloadLocation();
        }
    }

    @Override
    public void onBackPressed() {
        BaseFragment fragment = getCurrentFragment();

        checkBack();
    }

    private int isShowContainer = 0;

    public void checkBack() {

        if (isShowContainer > 0) {
            isShowContainer--;
            FragmentManager fm = getSupportFragmentManager();
            if (fm.getBackStackEntryCount() > 0) {
                fm.popBackStack();
            }

            view.showLayoutFragmentContainer();

        } else {
            showHeaderAndFooterHome();
            checkFragment();
        }


    }

    public void checkFragment() {

        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

        } else {
            super.onBackPressed();
        }
    }


    private void changeToLoginActivity() {

        Intent intent = new Intent(HomeActivity.this, LoginAndRegisterActivity.class);

        startActivity(intent);
    }

    public void showHeaderAndFooterHome() {
        isShowContainer = 0;
        view.showMap();
    }

    private void showHomeTimKiemTutorial() {
        TutorialModel model = new TutorialModel(R.id.btnSearch, R.string.title_timkiem, R.layout.view_tutorial_home_tim_kiem);
        view.showTutorial(model, new TutorialView.TutorialListener() {
            @Override
            public void onClose() {
                showAddNoteTutorial();
            }

            @Override
            public void onAction() {
//                showToast("Da hieu");
            }
        });
    }

    private void showAddNoteTutorial() {
        TutorialModel model = new TutorialModel(R.id.btnAddNote, R.string.title_addnote, R.layout.view_tutorial_add_note, true);
        view.showTutorial(model, new TutorialView.TutorialListener() {
            @Override
            public void onClose() {
            }

            @Override
            public void onAction() {
            }
        });
    }


    @Override
    public void onClickAddNote() {
        showHomeTimKiemTutorial();
    }
}
