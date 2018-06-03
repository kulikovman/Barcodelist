package ru.kulikovman.barcodelist;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import ru.kulikovman.barcodelist.dialogs.BarcodeNameIsEmptyDialog;
import ru.kulikovman.barcodelist.models.Good;

public class EditGoodActivity extends AppCompatActivity {
    private Realm mRealm;
    private Good mGood;

    private TextView mBarcodeField;
    private EditText mNameField, mGroupField;

    private String mBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_good);

        // Подключаем базу данных
        mRealm = Realm.getDefaultInstance();

        // Инициализируем необходимые вью элементы
        mBarcodeField = findViewById(R.id.edit_barcode);
        mNameField = findViewById(R.id.edit_name);
        mGroupField = findViewById(R.id.edit_group);

        // Получаем штрих-код и сохраняем его в поле
        mBarcode = (String) getIntent().getSerializableExtra("barcode");
        mBarcodeField.setText(mBarcode);

        // Если штрих-код в базе, то заполняем остальные поля
        Good good = mRealm.where(Good.class).equalTo(Good.BARCODE, mBarcode).findFirst();

        if (good != null) {
            // Делаем управляемый объект
            mRealm.beginTransaction();
            mGood = mRealm.copyToRealm(good);
            mRealm.commitTransaction();

            // Заполняем поля
            mNameField.setText(mGood.getName());
            mGroupField.setText(mGood.getGroup());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void saveButton(View view) {
        String barcodeName = mNameField.getText().toString().trim();
        String barcodeGroup = mGroupField.getText().toString().trim();

        Log.d("log", "Получили строки из полей: " + barcodeName + " | " + barcodeGroup);

        // Если наименование не пустое
        if (!barcodeName.equals("")) {
            // Обновляем или создаем товар
            mRealm.beginTransaction();
            if (mGood != null) {
                mGood.setName(barcodeName);
                mGood.setGroup(barcodeGroup);
            } else {
                mRealm.insert(new Good(mBarcode, barcodeName, barcodeGroup));
            }
            mRealm.commitTransaction();
            Log.d("log", "Товар успешно сохранен");

            // Переходим назад
            onBackPressed();
        } else {
            // Показываем предупреждающее сообщение
            DialogFragment barcodeNameIsEmptyDialog = new BarcodeNameIsEmptyDialog();
            barcodeNameIsEmptyDialog.show(getSupportFragmentManager(), "barcodeNameIsEmptyDialog");
        }
    }

    public void deleteButton(View view) {
        // Удаляем товар
        if (mGood != null) {
            mRealm.beginTransaction();
            mGood.deleteFromRealm();
            mRealm.commitTransaction();
        }

        // Переходим назад
        onBackPressed();
    }

    public void clearNameField(View view) {
        mNameField.setText("");
    }

    public void clearGroupField(View view) {
        mGroupField.setText("");
    }
}
