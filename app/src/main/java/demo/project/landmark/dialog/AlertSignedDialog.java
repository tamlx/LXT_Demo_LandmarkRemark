package demo.project.landmark.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;

import b.laixuantam.myaarlibrary.widgets.dialog.AppDialog;

public class AlertSignedDialog extends AppDialog<AlertSignedDialog.AlertSignedDialogListener> {

    private boolean isOk = false;

    public AlertSignedDialog() {
        setCancelable(true);
    }


    public interface AlertSignedDialogListener {
        void onOk(AppDialog<?> f);

    }


    public static AlertSignedDialog newInstance(String message, String title, boolean cancelable, AlertSignedDialogListener listener) {
        AlertSignedDialog dialog = new AlertSignedDialog();
        dialog.setListener(listener);
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        args.putString(EXTRA_TITLE, title);
        args.putBoolean(EXTRA_CANCELABLE, cancelable);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    protected boolean isListenerOptional() {
        return true;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        String message = null, title = null;
        boolean cancelable = true;
        int buttonText = b.laixuantam.myaarlibrary.R.string.dongy;
        isOk = false;
        if (bundle != null) {
            message = bundle.getString(EXTRA_MESSAGE);
            title = bundle.getString(EXTRA_TITLE);
            cancelable = bundle.getBoolean(EXTRA_CANCELABLE);
        }
        setCancelable(cancelable);
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()).setMessage(message).setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                isOk = true;
                if (getListener() != null) {
                    getListener().onOk(AlertSignedDialog.this);
                }
            }
        }).setCancelable(cancelable);

        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }

        return builder.create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
