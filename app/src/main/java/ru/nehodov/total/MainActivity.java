package ru.nehodov.total;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    private static final String READ_EXTERNAL_STORAGE_PERMISSION =
            Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int REQUEST_CODE_EXTERNAL_PERMISSION = 145;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE_PERMISSION)
                == PackageManager.PERMISSION_DENIED) {
            requestPermissions(
                    new String[]{READ_EXTERNAL_STORAGE_PERMISSION},
                    REQUEST_CODE_EXTERNAL_PERMISSION);
        }

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.host) == null) {
            fm.beginTransaction()
                    .add(R.id.host, ExplorerFragment.newInstance())
                    .commit();
        }

    }
}