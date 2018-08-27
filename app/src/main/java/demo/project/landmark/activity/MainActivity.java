package demo.project.landmark.activity;

import b.laixuantam.myaarlibrary.base.BaseActivity;
import b.laixuantam.myaarlibrary.base.BaseParameters;
import demo.project.landmark.R;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarView;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarViewCallback;
import demo.project.landmark.ui.views.action_bar.base_main_actionbar.BaseMainActionbarViewInterface;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityView;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityViewCallback;
import demo.project.landmark.ui.views.activity.base_main_activity.BaseMainActivityViewInterface;

public class MainActivity extends BaseActivity<BaseMainActivityViewInterface, BaseMainActionbarViewInterface, BaseParameters> implements BaseMainActivityViewCallback, BaseMainActionbarViewCallback {

    @Override
    protected void initialize() {
        view.init(this);
        actionbar.initialize("Main", this);
        actionbar.hideLayoutFilter();

        actionbar.showButtonRightActionBar();

        actionbar.configButtonLeftActionBar(R.drawable.ic_keyboard_arrow_left_black_24dp, R.color.blue_light);

        actionbar.configButtonRightActionBar(R.drawable.ic_check_black_24dp, R.color.blue_light);

        actionbar.configBackgroundLayoutFilter(R.color.white);

        actionbar.configBackgroundLayoutFilterContainer(R.drawable.border_shape_primary_layout_search_contact);

        actionbar.configButtonBackLayoutFilter(R.color.black);

        actionbar.configEdtSearchLayoutFilter(R.color.white, R.color.color_primary);

    }

/*    @Override
    protected void setupActionbar(ViewGroup container) {
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.layoutToolBar);
        this.actionbar = getActionbarInstance();

        if ((toolbar != null) && (this.actionbar != null)) {
            setSupportActionBar(toolbar);

            View actionbarView = this.actionbar.inflate(getLayoutInflater(), container);
            toolbar.addView(actionbarView);
        }
    }*/

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
        actionbar.showLayoutFilter();

    }
}
