package ru.kulikovman.barcodelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import io.realm.Realm;
import ru.kulikovman.barcodelist.model.Good;

public class EditGoodActivity extends AppCompatActivity {

    private Realm mRealm;
    private Good mGood;

    private TextView mBarcode;
    private EditText mName, mGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_good);

        // Подключаем базу данных
        mRealm = Realm.getDefaultInstance();

        // Инициализируем необходимые вью элементы
        mBarcode = findViewById(R.id.edit_title_barcode);
        mName = findViewById(R.id.edit_name);
        mGroup = findViewById(R.id.edit_group);

        // Получаем штрих-код и сохраняем его в поле
        String barcode = (String) getIntent().getSerializableExtra("barcode");
        mBarcode.setText(barcode);

        // Если штрих-код в базе, то заполняем остальные поля
        mGood = mRealm.where(Good.class).equalTo(Good.BARCODE, barcode).findFirst();

        if (mGood != null) {
            mName.setText(mGood.getName());
            mGroup.setText(mGood.getGroup());
        }
    }

    public void saveButton(View view) {
    }

    public void cancelButton(View view) {
    }
}
