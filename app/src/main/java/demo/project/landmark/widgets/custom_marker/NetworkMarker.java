package demo.project.landmark.widgets.custom_marker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;

import demo.project.landmark.R;
import demo.project.landmark.model.NotesModel;

public class NetworkMarker extends CustomMarker {

    private LatLng position;
    private View view, loading_view;
    private ImageView markerImage;
    private NotesModel item;

    public NetworkMarker(Context context, LatLng position, NotesModel item) {
        this.position = position;

        this.item = item;

        view = LayoutInflater.from(context).inflate(R.layout.view_network_marker, null);
        markerImage = (ImageView) view.findViewById(R.id.marker_image);

        loading_view = view.findViewById(R.id.loading_view);

    }

    @Override
    public void onAdd() {

    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {
        return BitmapGenerator.fromView(view);
    }


    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public NotesModel getItemModel() {
        return item != null ? item : null;
    }

    @Override
    public boolean onStateChange(boolean selected) {

        return true;
    }

}
