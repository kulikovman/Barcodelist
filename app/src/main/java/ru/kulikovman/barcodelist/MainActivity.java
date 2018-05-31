package ru.kulikovman.barcodelist;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import io.realm.Realm;
import io.realm.RealmResults;
import ru.kulikovman.barcodelist.dialog.InstallBarScannerDialog;
import ru.kulikovman.barcodelist.model.Good;

public class MainActivity extends AppCompatActivity {

    public final String LOG = "myLog";

    private Realm mRealm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Подключаем базу данных
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();

        // Проверка наличия сканера штрих-кодов
        if (!isExistBarcodeScannerApp()) {
            startInstallDialog();
        }
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
        DialogFragment installBarcodeScannerDialog = new InstallBarScannerDialog();
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d(LOG, contents + " | " + format);

                // Открываем редактирование штри-кода
                openEditGoodActivity(contents);

            } else if (resultCode == RESULT_CANCELED) {
                // Ничего не делаем
                Log.d(LOG, "Операция сканирования штрих-кода отменена.");
            }
        }
    }

    private void openEditGoodActivity(String barcode) {
        // Проверяем наличие штрих-кода в базе
        Good good = mRealm.where(Good.class).equalTo(Good.BARCODE, barcode).findFirst();

        if (good != null) {
            // Сообщаем о существовании в базе товара с таким штрих-кодом

        } else {
            // Открываем редактирование полученного штрих-кода

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
