package com.hyperx.magiks;

import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);

        // الشاشة الافتراضية
        loadFragment(new PerformanceFragment());

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_performance) {
                loadFragment(new PerformanceFragment());
            } else if (item.getItemId() == R.id.nav_settings) {
                loadFragment(new SettingsFragment());
            }
            return true;
        });

        // طلب صلاحية Shizuku
        if (Shizuku.pingBinder() && Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Shizuku.requestPermission(1001);
        }
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }
}
