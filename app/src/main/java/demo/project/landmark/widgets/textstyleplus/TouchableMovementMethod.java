package demo.project.landmark.widgets.textstyleplus;

import android.os.Handler;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.widget.TextView;

class TouchableMovementMethod extends LinkMovementMethod {

    private static TouchableMovementMethod sInstance;
    static boolean touched = false;

    public static MovementMethod getInstance() {
        if (sInstance == null) {
            sInstance = new TouchableMovementMethod();
        }
        return sInstance;
    }

    // **************************************************************************************************
    // **************************************************************************************************

    // TouchableSpan to handle the clicks
    private TouchableSpan mPressedSpan;

    /**
     * Manages the touches to find the link that was clicked and highlight it
     *
     * @param textView  view the user clicked
     * @param spannable spannable string inside the clicked view
     * @param event     motion event that occurred
     * @return
     */
    @Override
    public boolean onTouchEvent(final TextView textView, final Spannable spannable, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mPressedSpan = getPressedSpan(textView, spannable, event);

            if (mPressedSpan != null) {
                mPressedSpan.setTouched(true);
                touched = true;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (touched && mPressedSpan != null) {
                            if (textView.isHapticFeedbackEnabled()) {
                                textView.setHapticFeedbackEnabled(true);
                            }
                            textView.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS);

                            mPressedSpan.onLongClick(textView);
                            mPressedSpan.setTouched(false);
                            mPressedSpan = null;

                            Selection.removeSelection(spannable);
                        }
                    }
                }, 500);

                Selection.setSelection(spannable, spannable.getSpanStart(mPressedSpan), spannable.getSpanEnd(mPressedSpan));
            }
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            TouchableSpan touchedSpan = getPressedSpan(textView, spannable, event);

            if (mPressedSpan != null && touchedSpan != mPressedSpan) {
                mPressedSpan.setTouched(false);
                mPressedSpan = null;
                touched = false;

                Selection.removeSelection(spannable);
            }
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            if (mPressedSpan != null) {
                mPressedSpan.onClick(textView);
                mPressedSpan.setTouched(false);
                mPressedSpan = null;

                Selection.removeSelection(spannable);
            }
        } else {
            if (mPressedSpan != null) {
                mPressedSpan.setTouched(false);
                touched = false;

                super.onTouchEvent(textView, spannable, event);
            }

            mPressedSpan = null;

            Selection.removeSelection(spannable);
        }
        return true;
    }

    /**
     * Find the span that was clicked
     *
     * @param widget    view the user clicked
     * @param spannable spannable string inside the clicked view
     * @param event     motion event that occurred
     * @return the touchable span that was pressed
     */
    private TouchableSpan getPressedSpan(TextView widget, Spannable spannable, MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();

        x -= widget.getTotalPaddingLeft();
        y -= widget.getTotalPaddingTop();

        x += widget.getScrollX();
        y += widget.getScrollY();

        Layout layout = widget.getLayout();
        int line = layout.getLineForVertical(y);
        int off = layout.getOffsetForHorizontal(line, x);
        int end = layout.getLineEnd(line);

        // offset seems like it can be one off in some cases
        // Could be what was causing issue 7 in the first place:
        // https://github.com/klinker24/Android-TextView-LinkBuilder/issues/7
        if (off != end && off != end - 1) {
            TouchableSpan[] link = spannable.getSpans(off, off, TouchableSpan.class);

            if (link.length > 0)
                return link[0];
        }

        return null;
    }
}