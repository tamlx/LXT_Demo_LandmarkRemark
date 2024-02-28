package demo.project.landmark.widgets.textstyleplus;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

class TextStyle implements ISpannable {

    private static final float DEFAULT_ALPHA = 0.20F;

    final String text;
    private int textSize = 0;
    @ColorInt
    int textColor = 0;
    private float highlightAlpha = DEFAULT_ALPHA;
    @ColorInt
    private int backgroundColor;
    @ColorRes
    private int backgroundColorRes;
    @TypeFaceStyle
    private int typeFaceStyle = Typeface.NORMAL;
    @DrawableRes
    private int iconRes;
    private Drawable iconDrawable;
    private Bitmap iconBitmap;

    private boolean underLined = false;
    private boolean strikethrough = false;
    private boolean superscript = false;
    private boolean subscript = false;

    ClickListener clickListener;
    LongClickListener longClickListener;

    TextStyle(String text,
              int textSize,
              int textColor,
              float highlightAlpha,
              int backgroundColor,
              int backgroundColorRes,
              int typeFaceStyle,
              int iconRes,
              Drawable iconDrawable,
              Bitmap iconBitmap,
              boolean underLined,
              boolean strikethrough,
              boolean superscript,
              boolean subscript,
              ClickListener clickListener,
              LongClickListener longClickListener) {
        this.text = text;
        this.textSize = textSize;
        this.textColor = textColor;
        this.highlightAlpha = highlightAlpha;
        this.backgroundColor = backgroundColor;
        this.backgroundColorRes = backgroundColorRes;
        this.typeFaceStyle = typeFaceStyle;
        this.iconRes = iconRes;
        this.iconDrawable = iconDrawable;
        this.iconBitmap = iconBitmap;
        this.underLined = underLined;
        this.strikethrough = strikethrough;
        this.superscript = superscript;
        this.subscript = subscript;
        this.clickListener = clickListener;
        this.longClickListener = longClickListener;
    }

    @Override
    public SpannableString makeSpannableString(Context context) {
        SpannableString spannableString = new SpannableString(text);
        int length = spannableString.length();

        // strikethrough
        if (strikethrough) {
            spannableString.setSpan(new StrikethroughSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // underLined
        if (underLined) {
            spannableString.setSpan(new UnderlineSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // subscript
        if (subscript) {
            spannableString.setSpan(new SubscriptSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // superscript
        if (superscript) {
            spannableString.setSpan(new SuperscriptSpan(), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // click listener or long click listener
        if (clickListener != null || longClickListener != null) {
            TouchableSpan span = new TouchableSpan(context, this);
            spannableString.setSpan(span, 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // style
        spannableString.setSpan(new StyleSpan(typeFaceStyle), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // background color
        if (backgroundColor == 0 && backgroundColorRes != 0) {
            backgroundColor = context.getResources().getColor(backgroundColorRes);
        }
        if (backgroundColor != 0) {
            spannableString.setSpan(new BackgroundColorSpan(backgroundColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // text color
        if (textColor != 0) {
            spannableString.setSpan(new ForegroundColorSpan(textColor), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // textSize
        if (textSize != 0) {
            spannableString.setSpan(new AbsoluteSizeSpan(textSize), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        // image
        if (iconBitmap == null && iconRes != 0) {
            iconBitmap = BitmapFactory.decodeResource(context.getResources(), iconRes);
        }
        if (iconBitmap == null && iconDrawable != null) {
            boolean isOpaque = iconDrawable.getOpacity() != PixelFormat.OPAQUE;
            Bitmap.Config config = isOpaque ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
            int width = iconDrawable.getIntrinsicWidth();
            int height = iconDrawable.getIntrinsicHeight();
            iconBitmap = Bitmap.createBitmap(width, height, config);
            Canvas canvas = new Canvas(iconBitmap);
            iconDrawable.setBounds(0, 0, width, height);
            iconDrawable.draw(canvas);
        }
        if (iconBitmap != null) {
            spannableString.setSpan(new ImageSpan(context, iconBitmap), 0, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }
}