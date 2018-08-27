package demo.project.landmark.ui.views.activity.home_activity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import b.laixuantam.myaarlibrary.helper.map.drawroutemap.DrawMarker;
import b.laixuantam.myaarlibrary.widgets.touch_view_anim.scaletouchlistener.ScaleTouchListener;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialModel;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialView;
import demo.project.landmark.R;
import demo.project.landmark.activity.HomeActivity;
import demo.project.landmark.dependency.AppProvider;
import demo.project.landmark.model.NotesModel;
import demo.project.landmark.widgets.custom_marker.CustomMarker;
import demo.project.landmark.widgets.custom_marker.MarkerManager;
import demo.project.landmark.widgets.custom_marker.NetworkMarker;

public class HomeActivityView extends BaseView<HomeActivityView.UiContainer> implements HomeActivityViewInterface, View.OnClickListener {

    private HomeActivityViewCallback callback;
    private HomeActivity activity;

    private GoogleMap mMap;
    private Location loc;
    private MarkerManager<NetworkMarker> networkMarkerManager;
    private ArrayList<NetworkMarker> listMarker;
    private ArrayList<NetworkMarker> listMarkerInBounds = new ArrayList<>();

    private final int MIN_ZOOM = 16;

    boolean isClickMarker = false;

    LatLng origin = new LatLng(10.805655288131444, 106.71303480863571);

    @Override
    public void init(HomeActivity activity, HomeActivityViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        setUpViewControll();

        initilizeMap();
    }

    private void setUpViewControll() {

        String fullname = AppProvider.getPreferences().getUsername();
        String address = AppProvider.getPreferences().getUserAddress();
        String avata = AppProvider.getPreferences().getUserImage();

        View hView = ui.navView.getHeaderView(0);

        ui.imageViewHeader = hView.findViewById(R.id.imageViewHeader);
        ui.tvUsernameHeader = hView.findViewById(R.id.tvUsernameHeader);
        ui.tvUsernameHeader.setText(fullname);
        if (!TextUtils.isEmpty(avata)) {
            AppProvider.getImageHelper().displayImage(avata, ui.imageViewHeader, null, R.drawable.ic_user_default, true);
        }

        ui.btnLogin = ui.navView.findViewById(R.id.btnLogin);

        ScaleTouchListener.Config conf = new ScaleTouchListener.Config(100, 1f, 0.5f);

        ui.btnMenu.setOnTouchListener(new ScaleTouchListener(conf) {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });
        ui.btnAddNote.setOnTouchListener(new ScaleTouchListener(conf) {
            @Override
            public void onClick(View view) {
//                callback.onClickAddNote();
                mMap.clear();
                NotesModel notesModel = new NotesModel();
                notesModel.setTitle("title");
                notesModel.setDescription("description");

                NetworkMarker networkMarker = new NetworkMarker(getContext(), new LatLng(loc.getLatitude(), loc.getLongitude()),notesModel);
                networkMarkerManager.addMarker(networkMarker);
            }
        });
        ui.btnLogin.setOnTouchListener(new ScaleTouchListener(conf) {
            @Override
            public void onClick(View view) {
                if (!isDrawerOpen()) {
                    AppUtils.hideKeyBoard(getView());
                }
            }
        });

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnLogin:
                closeDrawer();
                break;
        }
    }

    @Override
    public void openDrawer() {

        AppUtils.hideKeyBoard(getView());

        ui.drawer.openDrawer(GravityCompat.START);
    }

    @Override
    public void closeDrawer() {
        if (isDrawerOpen()) {
            ui.drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean isDrawerOpen() {
        return ui.drawer.isDrawerOpen(GravityCompat.START);
    }


    private void initilizeMap() {
        showMap();
        ((MapFragment) activity.getFragmentManager().findFragmentById(R.id.map)).getMapAsync(googleMap -> onMyMapReady(googleMap));
    }

    private void onMyMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.setOnMapLoadedCallback(() -> {
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        });

        networkMarkerManager = new MarkerManager<>(googleMap);

        networkMarkerManager.setOnMarkerClickListener(new MyCustomMarkerClick<NetworkMarker>());
        networkMarkerManager.setOnInfoWindowClickListener(new MyCustomInfoWindowClick<NetworkMarker>());

        networkMarkerManager.setInfoWindowAdapter(getContext());


    }

    @SuppressLint("MissingPermission")
    @Override
    public void setCurrentLocation(Location location) {
        this.loc = location;
        try {
            mMap.setMyLocationEnabled(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        DrawMarker.getInstance(getContext()).draw(mMap, new LatLng(loc.getLatitude(), loc.getLongitude()), "Vị trí của bạn");
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(loc.getLatitude(), loc.getLongitude()), MIN_ZOOM));
    }


    private class MyCustomMarkerClick<T extends CustomMarker>
            implements MarkerManager.OnMarkerClickListener<T> {

        @Override
        public boolean onMarkerClick(T marker) {

            return true;
        }
    }

    private class MyCustomInfoWindowClick<T extends CustomMarker> implements MarkerManager.OnInfoWindowClickListener<T> {

        @Override
        public void onInfoWindowClick(T marker) {

            final NetworkMarker item = (NetworkMarker) marker;

//            NotesModel model = item.getItemModel();

        }
    }

    @Override
    public void destroyMapFragment() {
        MapFragment f = (MapFragment) activity.getFragmentManager().findFragmentById(R.id.map);
        if (f != null) {

            activity.getFragmentManager().beginTransaction().remove(f).commit();
        }
    }

    @Override
    public void showMap() {
        setVisible(ui.layout_map);
        if (mMap != null) {
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

        setGone(ui.home_activity_container);
    }

    @Override
    public void showLayoutFragmentContainer() {
        setGone(ui.layout_map);
        setVisible(ui.home_activity_container);
        if (mMap != null)
            mMap.getUiSettings().setAllGesturesEnabled(false);
    }

    @Override
    public void showTutorial(TutorialModel tutorial, TutorialView.TutorialListener listener) {
        ui.tutorialView.setVisibility(View.VISIBLE);
        ui.tutorialView.setListener(listener);
//        ui.tutorialView.showTutorialDelay(tutorial);
        ui.tutorialView.showTutorial(tutorial);
    }

    @Override
    public void hideTutorial() {
        ui.tutorialView.setVisibility(View.GONE);
    }

    @Override
    public BaseUiContainer getUiContainer() {
        return new HomeActivityView.UiContainer();
    }

    @Override
    public int getViewId() {
        return R.layout.layout_activity_home;
    }

    public static class UiContainer extends BaseUiContainer {

        @UiElement(R.id.layout_map)
        public View layout_map;

        @UiElement(R.id.map)
        public SupportMapFragment map;

        @UiElement(R.id.drawer_layout)
        public DrawerLayout drawer;

        @UiElement(R.id.home_activity_container)
        public FrameLayout home_activity_container;

        @UiElement(R.id.nav_view)
        public NavigationView navView;

        @UiElement(R.id.btnMenu)
        public View btnMenu;

        @UiElement(R.id.btnSearch)
        public View btnSearch;

        @UiElement(R.id.btnAddNote)
        public View btnAddNote;

        //layout header
        @UiElement(R.id.imageViewHeader)
        public ImageView imageViewHeader;

        @UiElement(R.id.tvUsernameHeader)
        public TextView tvUsernameHeader;

        //------end----------------

        //layout nav-menu

        @UiElement(R.id.btnLogin)
        public View btnLogin;

        //------end----------------


        @UiElement(R.id.tutorial)
        public TutorialView tutorialView;

    }
}
