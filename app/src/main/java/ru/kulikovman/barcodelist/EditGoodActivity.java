package ru.kulikovman.barcodelist;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import ru.kulikovman.barcodelist.dialog.BarcodeNameIsEmptyDialog;
import ru.kulikovman.barcodelist.model.Good;

public class EditGoodActivity extends AppCompatActivity {

    private Realm mRealm;
    private Good mGood;

    private TextView mBarcodeField;
    private EditText mName, mGroup;

    private String mBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_good);

        // Подключаем базу данных
        mRealm = Realm.getDefaultInstance();

        // Инициализируем необходимые вью элементы
        mBarcodeField = findViewById(R.id.edit_title_barcode);
        mName = findViewById(R.id.edit_name);
        mGroup = findViewById(R.id.edit_group);

        // Получаем штрих-код и сохраняем его в поле
        mBarcode = (String) getIntent().getSerializableExtra("barcode");
        mBarcodeField.setText(mBarcode);

        // Если штрих-код в базе, то заполняем остальные поля
        mGood = mRealm.where(Good.class).equalTo(Good.BARCODE, mBarcode).findFirst();

        if (mGood != null) {
            mName.setText(mGood.getName());
            mGroup.setText(mGood.getGroup());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    public void saveButton(View view) {
        String barcodeName = mName.getText().toString().trim();
        String barcodeGroup = mGroup.getText().toString().trim();

        // Если имя не пустое
        if (!barcodeName.isEmpty()) {
            // Создаем или обновляем товар
            if (mGood != null) {
                mGood.setName(barcodeName);
                mGood.setGroup(barcodeGroup);
            } else {
                mGood = new Good(mBarcode, barcodeName, barcodeGroup);
            }

            // Сохраняем в базе
            mRealm.beginTransaction();
            mRealm.insertOrUpdate(mGood);
            mRealm.commitTransaction();
        } else {
            // Показываем предупреждающее сообщение
            DialogFragment barcodeNameIsEmptyDialog = new BarcodeNameIsEmptyDialog();
            barcodeNameIsEmptyDialog.show(getSupportFragmentManager(), "barcodeNameIsEmptyDialog");
        }
    }

    public void cancelButton(View view) {
        onBackPressed();
    }
}
