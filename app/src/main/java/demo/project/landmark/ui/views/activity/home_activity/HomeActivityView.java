package demo.project.landmark.ui.views.activity.home_activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.location.Location;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import b.laixuantam.myaarlibrary.base.BaseUiContainer;
import b.laixuantam.myaarlibrary.base.BaseView;
import b.laixuantam.myaarlibrary.helper.AppUtils;
import b.laixuantam.myaarlibrary.helper.MyLog;
import b.laixuantam.myaarlibrary.helper.map.drawroutemap.DrawMarker;
import b.laixuantam.myaarlibrary.widgets.cptr.PtrDefaultHandler;
import b.laixuantam.myaarlibrary.widgets.cptr.PtrFrameLayout;
import b.laixuantam.myaarlibrary.widgets.cptr.loadmore.OnLoadMoreListener;
import b.laixuantam.myaarlibrary.widgets.cptr.recyclerview.RecyclerAdapterWithHF;
import b.laixuantam.myaarlibrary.widgets.recyclerviewenhanced.RecyclerTouchListener;
import b.laixuantam.myaarlibrary.widgets.touch_view_anim.scaletouchlistener.ScaleTouchListener;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialModel;
import b.laixuantam.myaarlibrary.widgets.tutorial.TutorialView;
import demo.project.landmark.R;
import demo.project.landmark.activity.HomeActivity;
import demo.project.landmark.adapter.ListNoteAdapter;
import demo.project.landmark.database.table_note.NoteSaved;
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

    private final int MIN_ZOOM = 16;


    @Override
    public void init(HomeActivity activity, HomeActivityViewCallback callback) {
        this.activity = activity;
        this.callback = callback;
        setUpViewControll();

        initilizeMap();

        setUpListNote();
    }

    private void setUpViewControll() {

        ScaleTouchListener.Config conf = new ScaleTouchListener.Config(100, 1f, 0.5f);

        ui.btnAddNote.setOnClickListener(this);
        ui.btnSearch.setOnClickListener(this);
        ui.btnBackLayoutAddNote.setOnClickListener(this);
        ui.btnBackHeaderListNote.setOnClickListener(this);
        ui.btnSubmitAddNote.setOnClickListener(this);

        ui.edtInputSearchNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(editable)){
                    String titleFilter = editable.toString().trim();
                    listDatas.clear();
                    List<NoteSaved> noteSaveds = NoteSaved.find(NoteSaved.class, "title LIKE ? OR description LIKE ?", titleFilter + "%" , titleFilter + "%" );
                    listDatas.addAll(noteSaveds);
                    noteSavedAdapter.notifyDataSetChanged();
                }else {
                    listDatas.clear();
                    List<NoteSaved> noteSaveds = NoteSaved.listAll(NoteSaved.class);
                    listDatas.addAll(noteSaveds);
                    noteSavedAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAddNote:
                showLayoutAddNote();
                break;

            case R.id.btnBackLayoutAddNote:
                showMap();
                break;

            case R.id.btnSubmitAddNote:
                if (TextUtils.isEmpty(ui.edtInputUsername.getText())) {
                    ui.edtInputUsername.setError("Nhập username");
                    return;
                }

                if (TextUtils.isEmpty(ui.edtInputDescription.getText())) {
                    ui.edtInputDescription.setError("Nhập note");
                    return;
                }
                AppUtils.hideKeyBoard(getView());

                String username = ui.edtInputUsername.getText().toString().trim();
                String des = ui.edtInputDescription.getText().toString().trim();

                NotesModel notesModel = new NotesModel();
                notesModel.setTitle(username);
                notesModel.setDescription(des);

                List<NoteSaved> noteSaveds = NoteSaved.find(NoteSaved.class, "title = ?", username);
                if (noteSaveds != null && noteSaveds.size() > 0) {
                    Toast.makeText(getContext(), "Note đã được lưu", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    NoteSaved itemNoteSaved = new NoteSaved(notesModel, loc.getLatitude(), loc.getLongitude());

                    itemNoteSaved.save();

                    Toast.makeText(getContext(), "Lưu note thành công", Toast.LENGTH_SHORT).show();
                }

                mMap.clear();
                NetworkMarker networkMarker = new NetworkMarker(getContext(), new LatLng(loc.getLatitude(), loc.getLongitude()), notesModel);
                networkMarkerManager.addMarker(networkMarker);
                showMap();
                break;
            case  R.id.btnSearch:
                showLayoutListNote();
                break;
            case  R.id.btnBackHeaderListNote:
                AppUtils.hideKeyBoard(getView());
                showMap();
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

    private void showMap() {
        setVisible(ui.layout_map);
        setGone(ui.rLayoutAddNote);
        setGone(ui.rLayoutListNote);
        if (mMap != null) {
            mMap.getUiSettings().setAllGesturesEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

    }

    private void showLayoutAddNote() {
        setGone(ui.layout_map);
        setVisible(ui.rLayoutAddNote);
        if (mMap != null)
            mMap.getUiSettings().setAllGesturesEnabled(false);
    }

    private void showLayoutListNote(){
        setGone(ui.layout_map);
        setVisible(ui.rLayoutListNote);
        if (mMap != null)
            mMap.getUiSettings().setAllGesturesEnabled(false);

        listDatas.clear();
        List<NoteSaved> noteSaveds = NoteSaved.listAll(NoteSaved.class);
        listDatas.addAll(noteSaveds);
        noteSavedAdapter.notifyDataSetChanged();
    }


    @Override
    public void showTutorial(TutorialModel tutorial, TutorialView.TutorialListener listener) {
        ui.tutorialView.setVisibility(View.VISIBLE);
        ui.tutorialView.setListener(listener);
//        ui.tutorialView.showTutorialDelay(tutorial);
        ui.tutorialView.showTutorial(tutorial);
    }


    private List<NoteSaved> listDatas = new ArrayList<>();
    private ListNoteAdapter noteSavedAdapter;

    private void setUpListNote() {
        noteSavedAdapter = new ListNoteAdapter(getContext(), listDatas);
        noteSavedAdapter.setListener(item -> {
            AppUtils.hideKeyBoard(getView());
            NotesModel notesModel = new NotesModel();
            notesModel.setTitle(item.getTitle());
            notesModel.setDescription(item.getDescription());
            mMap.clear();
            NetworkMarker networkMarker = new NetworkMarker(getContext(), new LatLng(loc.getLatitude(), loc.getLongitude()), notesModel);
            networkMarkerManager.addMarker(networkMarker);
            showMap();
        });
        ui.recycler_view_list_note.setLayoutManager(new LinearLayoutManager(getContext()));
        ui.recycler_view_list_note.setAdapter(noteSavedAdapter);

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

        @UiElement(R.id.rLayoutMap)
        public View layout_map;

        @UiElement(R.id.map)
        public SupportMapFragment map;

        @UiElement(R.id.drawer_layout)
        public DrawerLayout drawer;


        @UiElement(R.id.nav_view)
        public NavigationView navView;

        @UiElement(R.id.btnMenu)
        public View btnMenu;

        @UiElement(R.id.btnSearch)
        public View btnSearch;

        @UiElement(R.id.btnAddNote)
        public View btnAddNote;

        //------end----------------

        //layout AddNote
        @UiElement(R.id.rLayoutAddNote)
        public View rLayoutAddNote;

        @UiElement(R.id.btnBackLayoutAddNote)
        public View btnBackLayoutAddNote;

        @UiElement(R.id.edtInputUsername)
        public EditText edtInputUsername;

        @UiElement(R.id.edtInputDescription)
        public EditText edtInputDescription;

        @UiElement(R.id.btnSubmitAddNote)
        public View btnSubmitAddNote;

        //------end----------------

        @UiElement(R.id.rLayoutListNote)
        public View rLayoutListNote;

        @UiElement(R.id.btnBackHeaderListNote)
        public View btnBackHeaderListNote;

        @UiElement(R.id.edtInputSearchNote)
        public EditText edtInputSearchNote;

        @UiElement(R.id.recycler_view_list_note)
        public RecyclerView recycler_view_list_note;


        @UiElement(R.id.tutorial)
        public TutorialView tutorialView;

    }
}
