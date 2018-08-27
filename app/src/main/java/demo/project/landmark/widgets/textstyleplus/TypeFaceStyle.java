package demo.project.landmark.widgets.textstyleplus;

import android.graphics.Typeface;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({Typeface.NORMAL, Typeface.BOLD, Typeface.BOLD_ITALIC, Typeface.ITALIC})
@Retention(RetentionPolicy.SOURCE)
@interface TypeFaceStyle {
}