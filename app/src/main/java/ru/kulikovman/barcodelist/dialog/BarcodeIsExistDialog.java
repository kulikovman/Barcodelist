package ru.kulikovman.barcodelist.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import ru.kulikovman.barcodelist.EditGoodActivity;
import ru.kulikovman.barcodelist.R;


public class BarcodeIsExistDialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Получаем штрих-код из аргументов
        final String barcode = getArguments().getString("barcode");

        // Создаем диалог
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.message_barcode_is_exist)
                .setPositiveButton(R.string.button_open, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Открываем штрих-код для редактирования
                        Intent editGoodActivity = new Intent(getActivity(), EditGoodActivity.class);
                        editGoodActivity.putExtra("barcode", barcode);
                        startActivity(editGoodActivity);
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
}
