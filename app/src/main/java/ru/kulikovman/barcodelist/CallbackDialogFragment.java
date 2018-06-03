package ru.kulikovman.barcodelist;

import android.support.v4.app.DialogFragment;


public class CallbackDialogFragment extends DialogFragment {
    public interface CallbackDialogListener {
        public void onDialogFinish(DialogFragment dialog);
    }
}
