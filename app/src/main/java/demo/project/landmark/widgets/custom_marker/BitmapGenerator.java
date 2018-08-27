package demo.project.landmark.widgets.custom_marker;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.view.View;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public final class BitmapGenerator {

    private BitmapGenerator() {
    }

    public static BitmapDescriptor fromView(View view) {

        // TODO: Single views have trouble with measure.
        final int spec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(spec, spec);

        final int width = view.getMeasuredWidth();
        final int height = view.getMeasuredHeight();

        view.layout(0, 0, width, height);

        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        view.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}