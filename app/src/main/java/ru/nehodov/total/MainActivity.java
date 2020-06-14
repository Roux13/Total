package ru.nehodov.total;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.host) == null) {
            fm.beginTransaction()
                    .add(R.id.host, ExplorerFragment.newInstance())
                    .commit();
        }

    }
}