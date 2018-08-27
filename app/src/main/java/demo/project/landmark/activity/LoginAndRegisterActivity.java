package demo.project.landmark.activity;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;

import b.laixuantam.myaarlibrary.base.BaseFragmentActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import b.laixuantam.myaarlibrary.helper.OnKeyboardVisibilityListener;
import demo.project.landmark.R;
import demo.project.landmark.dependency.AppProvider;
import demo.project.landmark.event.KeyboardInEvent;
import demo.project.landmark.event.KeyboardOutEvent;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarView;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarViewCallback;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityView;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityViewCallback;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityViewInterface;

public class LoginAndRegisterActivity extends BaseFragmentActivity<BaseMainActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements BaseMainActivityViewCallback, BaseMainActionbarViewCallback, OnKeyboardVisibilityListener {

    @Override
    protected void initialize(Bundle bundle) {
        view.init(this);
        actionbar.initialize("Login", this);
        actionbar.hideLayoutFilter();
        actionbar.hideButtonRightActionBar();
        actionbar.hideButtonLeftActionBar();

        setKeyboardVisibilityListener(this);

        String userID = AppProvider.getPreferences().getUserId();

        changeToHomeActivity();

    }

    @Override
    protected void setupActionbar(ViewGroup container) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.layoutToolBar);
        this.actionbar = getActionbarInstance();

        if ((toolbar != null) && (this.actionbar != null)) {
            setSupportActionBar(toolbar);

            View actionbarView = this.actionbar.inflate(getLayoutInflater(), container);
            toolbar.addView(actionbarView);
        }
    }

    @Override
    protected BaseMainActivityViewInterface getViewInstance() {
        return new BaseMainActivityView();
    }

    @Override
    protected BaseMainActionbarViewInterface getActionbarInstance() {
        return new BaseMainActionbarView();
    }

    @Override
    protected BaseParameters getParametersContainer() {
        return null;
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }


    @Override
    public void onFilterToggle(boolean showFilter) {

    }

    @Override
    public void onFiltering(String keyword) {

    }

    @Override
    public void onClickButtonLeftActionbar() {

    }

    @Override
    public void onClickButtonRightActionbar() {

    }

    @Override
    public void onBackPressed() {
        checkFragment();
    }

    public void checkFragment() {
        FragmentManager fm = getSupportFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();

            actionbar.setTitle("Login");
        } else {
            finish();
        }
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
    //============================Main fuction===============================

    public void changeToHomeActivity() {

//        Intent intent = new Intent(this, HomeActivity.class);
//        startActivity(intent);

        String userRole = AppProvider.getPreferences().getUserRole();

        if (!TextUtils.isEmpty(userRole)) {
            if (userRole.equalsIgnoreCase("0")) {

                showToast("login");
                finish();

            } else {
                //todo change to admin page
//                Intent intent = new Intent(this, AdminActivity.class);
//                startActivity(intent);
//                finish();
            }
        } else {
            showToast("login");
            finish();
        }
    }
}
