package ru.kulikovman.barcodelist.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import io.realm.Realm;
import ru.kulikovman.barcodelist.CallbackDialogFragment;
import ru.kulikovman.barcodelist.R;


public class DataDeletionWarningDialog extends CallbackDialogFragment {
    private Realm mRealm;
    private CallbackDialogListener mListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CallbackDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement CallbackDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Подключаемся к базе
        mRealm = Realm.getDefaultInstance();

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_data_deletion)
                .setPositiveButton(R.string.button_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Полная очистка базы данных
                        mRealm.beginTransaction();
                        mRealm.deleteAll();
                        mRealm.commitTransaction();

                        // Сообщение об успешном удалении
                        DialogFragment dataHasBeenDeletedDialog = new DataHasBeenDeletedDialog();
                        dataHasBeenDeletedDialog.show(getActivity().getSupportFragmentManager(), "dataHasBeenDeletedDialog");
                    }
                })
                .setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Ничего не делаем при нажатии
                    }
                });

        return builder.create();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRealm.close();

        // Запускаем код в активити
        mListener.onDialogFinish(DataDeletionWarningDialog.this);
    }
}
