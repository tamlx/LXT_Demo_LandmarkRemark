package demo.project.landmark.widgets.custom_marker;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;

import demo.project.landmark.model.NotesModel;

public class IconMarker extends CustomMarker {

    private final LatLng position;
    private int selectedIcon;
    private int defaultIcon;

    public IconMarker(LatLng position, int iconRes) {

        this.position = position;
        defaultIcon = iconRes;
    }

    public IconMarker(LatLng position, int iconRes, int selectedRes) {

        this(position, iconRes);
        selectedIcon = selectedRes;
    }

    public void setSelectedIcon(int selectedRes) {
        selectedIcon = selectedRes;
    }

    @Override
    public boolean onStateChange(boolean selected) {
        return selectedIcon > 0;
    }

    @Override
    public BitmapDescriptor getBitmapDescriptor() {

        return BitmapDescriptorFactory.fromResource(
                selectedIcon > 0 && isSelected()
                        ? selectedIcon
                        : defaultIcon
        );
    }

    @Override
    public LatLng getPosition() {
        return position;
    }

    @Override
    public NotesModel getItemModel() {
        return null;
    }
}
