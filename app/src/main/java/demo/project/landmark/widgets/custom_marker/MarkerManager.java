package demo.project.landmark.widgets.custom_marker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import demo.project.landmark.R;

public class MarkerManager<T extends CustomMarker> implements
        GoogleMap.OnMarkerClickListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnCameraIdleListener {

    private final GoogleMap googleMap;

    private OnMarkerClickListener<T> onMarkerClickListener;
    private OnInfoWindowClickListener<T> onInfoWindowClickListener;

    private T lastSelected;

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (onInfoWindowClickListener != null) {
            onInfoWindowClickListener.onInfoWindowClick(markerCache.get(marker));
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        if (lastSelected != null) {
            lastSelected.setSelected(false);
        }

        final T item = markerCache.get(marker);

        if (item != null) {
            item.setSelected(true);
            lastSelected = item;
        }

        return false;
    }

    public interface OnMarkerClickListener<T extends CustomMarker> {

        boolean onMarkerClick(T marker);
    }

    public interface OnInfoWindowClickListener<T extends CustomMarker> {

        void onInfoWindowClick(T marker);
    }

    public void setOnMarkerClickListener(OnMarkerClickListener<T> onMarkerClickListener) {
        this.onMarkerClickListener = onMarkerClickListener;
    }

    public void setOnInfoWindowClickListener(OnInfoWindowClickListener<T> onInfoWindowClickListener) {
        this.onInfoWindowClickListener = onInfoWindowClickListener;
    }

    @Override
    public void onCameraIdle() {
        if (lastSelected != null) {
            lastSelected.setSelected(false);
        }
    }

    /**
     * Marker cache for on screen markers.
     */
    private MarkerCache<T> markerCache = new MarkerCache<>();

    public MarkerManager(GoogleMap map) {
        googleMap = map;

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setOnMarkerClickListener(this);

//        googleMap.setOnCameraIdleListener(this);

    }

    public void setInfoWindowAdapter(Context context) {

        googleMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(context, markerCache));
        googleMap.setOnInfoWindowClickListener(this);
    }


    class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".

        private final View mContents;
        private MarkerCache<T> markerCache;

        CustomInfoWindowAdapter(Context context, MarkerCache<T> markerCache) {
            this.markerCache = markerCache;

            mContents = LayoutInflater.from(context).inflate(R.layout.layout_custom_product_window_info, null);

        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {

            final NetworkMarker item = (NetworkMarker) markerCache.get(marker);
            if (item != null && item.getItemModel() != null) {
                fillDataInfoWindow(marker);
                return mContents;
            } else
                return null;

        }

        private void fillDataInfoWindow(Marker marker) {

            TextView tvTitle = (TextView) mContents.findViewById(R.id.tvTitle);
            TextView tvDescription = (TextView) mContents.findViewById(R.id.tvDescription);

            if (markerCache != null) {
                final NetworkMarker item = (NetworkMarker) markerCache.get(marker);

                if (item != null && item.getItemModel() != null) {

                    String title = item.getItemModel().getTitle();
                    tvTitle.setText(title);

                    tvDescription.setText("Note: " + item.getItemModel().getDescription());

                }

            }
        }


    }

    /**
     * A cache of markers.
     */
    private static class MarkerCache<T> {
        private Map<T, Marker> cache = new HashMap<>();
        private Map<Marker, T> cacheReverse = new HashMap<>();

        public Marker get(T item) {
            return cache.get(item);
        }

        public T get(Marker m) {
            return cacheReverse.get(m);
        }

        public void put(T item, Marker m) {
            cache.put(item, m);
            cacheReverse.put(m, item);
        }

        public void clear() {
            cache.clear();
            cacheReverse.clear();
        }

        public void remove(T item) {
            Marker m = cache.get(item);
            cacheReverse.remove(m);
            cache.remove(item);
        }

    }

    /**
     * Add a marker to the map.
     *
     * @param marker marker to add
     */
    public void addMarker(T marker) {

        final MarkerOptions markerOptions = new MarkerOptions();

        marker.setMarkerManager(this);
        marker.prepareMarker(markerOptions);

        markerCache.put(marker, googleMap.addMarker(markerOptions));

        marker.onAdd();
    }

    /**
     * Add all markers to the map.
     *
     * @param markers markers to add
     */
    public void addMarkers(List<T> markers) {

        for (T marker : markers) {
            addMarker(marker);
        }
    }

    /**
     * Remove a marker from the map.
     *
     * @param marker marker to remove
     */
    public void removeMarker(T marker) {

        final Marker realMarker = markerCache.get(marker);

        if (realMarker != null) {
            realMarker.remove();
        }

        markerCache.remove(marker);
    }

    /**
     * Clear the map.
     */
    public void clear() {

        googleMap.clear();
        markerCache.clear();
    }

    /**
     * Get the the real marker object.
     *
     * @param marker marker which to obtain its real marker
     * @return real marker
     */
    public Marker getMarker(T marker) {
        return markerCache.get(marker);
    }

}

