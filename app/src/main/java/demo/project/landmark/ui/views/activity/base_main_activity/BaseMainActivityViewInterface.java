package demo.project.landmark.ui.views.activity.base_main_activity;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;

/**
 * Created by laixuantam on 7/6/17.
 */

public interface BaseMainActivityViewInterface extends BaseViewInterface {
    void init(BaseMainActivityViewCallback callback);

    void showToolBar();

    void hideToolBar();

}
