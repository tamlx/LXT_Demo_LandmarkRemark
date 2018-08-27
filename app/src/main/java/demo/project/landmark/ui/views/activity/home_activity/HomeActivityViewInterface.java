package demo.project.landmark.ui.views.activity.home_activity;

import android.location.Location;

import b.laixuantam.myaarlibrary.base.BaseViewInterface;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialModel;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialView;
import demo.project.landmark.activity.HomeActivity;

public interface HomeActivityViewInterface extends BaseViewInterface {

    void init(HomeActivity activity, HomeActivityViewCallback callback);

    void openDrawer();

    void closeDrawer();

    boolean isDrawerOpen();

    void setCurrentLocation(Location location);

    void destroyMapFragment();

    void showMap();

    void showLayoutFragmentContainer();

    void showTutorial(TutorialModel tutorial, TutorialView.TutorialListener listener);

    void hideTutorial();
}
