package com.hyperx.magiks;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import rikka.shizuku.Shizuku;

public class MainActivity extends AppCompatActivity {

    private static final int SHIZUKU_PERMISSION_CODE = 1001;

    private final Shizuku.OnRequestPermissionResultListener permissionListener = 
            new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            if (requestCode == SHIZUKU_PERMISSION_CODE) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "✅ تم منح صلاحية Shizuku بنجاح!", Toast.LENGTH_LONG).show();
                    updateShizukuStatus();
                } else {
                    Toast.makeText(MainActivity.this, "❌ تم رفض صلاحية Shizuku.", Toast.LENGTH_LONG).show();
                    updateShizukuStatus();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Shizuku.addRequestPermissionResultListener(permissionListener);
        requestShizukuPermission();
        updateShizukuStatus();
        setupAllSwitches();
    }

    private void requestShizukuPermission() {
        if (!Shizuku.pingBinder()) {
            Toast.makeText(this, "⚠️ Shizuku غير مثبت أو لا يعمل!", Toast.LENGTH_LONG).show();
            return;
        }
        if (Shizuku.isPreV11()) {
            Toast.makeText(this, "⚠️ إصدار Shizuku قديم جداً.", Toast.LENGTH_LONG).show();
            return;
        }
        if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "✅ الصلاحية ممنوحة مسبقاً.", Toast.LENGTH_SHORT).show();
        } else {
            Shizuku.requestPermission(SHIZUKU_PERMISSION_CODE);
        }
    }

    private void updateShizukuStatus() {
        TextView statusText = findViewById(R.id.tv_shizuku_status);
        if (statusText != null) {
            if (Shizuku.pingBinder()) {
                if (Shizuku.checkSelfPermission() == PackageManager.PERMISSION_GRANTED) {
                    statusText.setText("✅ Shizuku مفعل ومصرح له");
                    statusText.setTextColor(android.graphics.Color.parseColor("#00FF00"));
                } else {
                    statusText.setText("⚠️ Shizuku يعمل ولكن الصلاحية مرفوضة");
                    statusText.setTextColor(android.graphics.Color.parseColor("#FFA500"));
                }
            } else {
                statusText.setText("❌ Shizuku غير مثبت أو لا يعمل");
                statusText.setTextColor(android.graphics.Color.parseColor("#FF0000"));
            }
        }
    }

    private void setupAllSwitches() {
        int[] switches = {
            R.id.switch_fps, R.id.switch_gpu, R.id.switch_thermal, R.id.switch_ram, R.id.switch_cpu, R.id.switch_network,
            R.id.switch_skiagl, R.id.switch_vulkan, R.id.switch_ultra, R.id.switch_aa, R.id.switch_hdr,
            R.id.switch_touch, R.id.switch_aim, R.id.switch_gyro, R.id.switch_multitouch,
            R.id.switch_battery, R.id.switch_doze, R.id.switch_cleaner, R.id.switch_adb, R.id.switch_120fps
        };

        for (int id : switches) {
            Switch sw = findViewById(id);
            if (sw != null) {
                sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (isChecked) {
                        Toast.makeText(this, sw.getText() + " مفعل (سيتم إضافة السكربت قريباً)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, sw.getText() + " معطل", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(permissionListener);
    }
}
