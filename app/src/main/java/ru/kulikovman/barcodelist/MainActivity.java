package ru.kulikovman.barcodelist;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.kulikovman.barcodelist.adapters.GroupAdapter;
import ru.kulikovman.barcodelist.dialogs.BarcodeIsExistDialog;
import ru.kulikovman.barcodelist.dialogs.InstallBarcodeScannerDialog;
import ru.kulikovman.barcodelist.models.Good;

public class MainActivity extends AppCompatActivity {
    private Realm mRealm;

    private GroupAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Подключаем базу данных
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        // Инициализация вью элементов
        mRecyclerView = findViewById(R.id.main_recycler_view);

        // Проверка наличия сканера штрих-кодов
        if (!isExistBarcodeScannerApp()) {
            startInstallDialog();
        }

        // Запускаем список
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        // Получение списка групп
        List<String> groups = new ArrayList<>();
        RealmResults<Good> goods = mRealm.where(Good.class).findAll();
        for (Good good : goods) {
            String group = good.getGroup();
            if (!groups.contains(group)){
                groups.add(group);
            }
        }

        // Подключение адаптера
        mAdapter = new GroupAdapter(this, groups, mRealm);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Обновляем списки
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    private boolean isExistBarcodeScannerApp() {
        PackageManager pm = getPackageManager();
        PackageInfo pi = null;
        try {
            pi = pm.getPackageInfo(getString(R.string.zxing_package), 0);
        } catch (PackageManager.NameNotFoundException ignored) {
        }

        return pi != null;
    }

    private void startInstallDialog() {
        // Предлагаем установить сканер
        DialogFragment installBarcodeScannerDialog = new InstallBarcodeScannerDialog();
        installBarcodeScannerDialog.show(getSupportFragmentManager(), "installBarcodeScannerDialog");
    }

    public void addBarcode(View view) {
        if (isExistBarcodeScannerApp()) {
            Intent intent = new Intent(getString(R.string.zxing_package) + ".SCAN");
            intent.putExtra("SCAN_MODE", "BAR_CODE_MODE");
            startActivityForResult(intent, 0);
        } else {
            startInstallDialog();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("log", contents + " | " + format);

                // Запускаем редактирование штрих-кода
                openEditGoodActivity(contents);

            } else if (resultCode == RESULT_CANCELED) {
                // Ничего не делаем
                Log.d("log", "Операция сканирования штрих-кода отменена");
            }
        }
    }

    private void openEditGoodActivity(String barcode) {
        // Проверяем наличие штрих-кода в базе
        Good good = mRealm.where(Good.class).equalTo(Good.BARCODE, barcode).findFirst();

        if (good != null) {
            // Сообщаем о существовании в базе товара с таким штрих-кодом
            Bundle args = new Bundle();
            args.putString("barcode", barcode);
            DialogFragment barcodeIsExistDialog = new BarcodeIsExistDialog();
            barcodeIsExistDialog.setArguments(args);
            barcodeIsExistDialog.show(getSupportFragmentManager(), "barcodeIsExistDialog");
        } else {
            // Редактирование полученного штрих-кода
            Intent editGoodActivity = new Intent(this, EditGoodActivity.class);
            editGoodActivity.putExtra("barcode", barcode);
            startActivity(editGoodActivity);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
