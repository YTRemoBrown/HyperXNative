package com.hyperx.magiks;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import rikka.shizuku.Shizuku;
import rikka.shizuku.ShizukuBinderWrapper;

public class MainActivity extends AppCompatActivity {

    private static final int SHIZUKU_PERMISSION_CODE = 1001;

    // مستمع لنتيجة طلب الصلاحية
    private final Shizuku.OnRequestPermissionResultListener permissionListener = 
            new Shizuku.OnRequestPermissionResultListener() {
        @Override
        public void onRequestPermissionResult(int requestCode, int grantResult) {
            if (requestCode == SHIZUKU_PERMISSION_CODE) {
                if (grantResult == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(MainActivity.this, "✅ تم منح صلاحية Shizuku بنجاح!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "❌ تم رفض صلاحية Shizuku.", Toast.LENGTH_LONG).show();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 1. تسجيل مستمع الصلاحية
        Shizuku.addRequestPermissionResultListener(permissionListener);

        // 2. طلب الصلاحية تلقائياً عند فتح التطبيق
        requestShizukuPermission();

        // 3. ربط الأزرار الـ 20
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
                    // TODO: هذا هو المكان الذي ستضع فيه السكربتات لاحقاً
                    if (isChecked) {
                        Toast.makeText(this, sw.getText() + " مفعل (سيتم إضافة السكربت قريباً)", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, sw.getText() + " معطل", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    // دالة لتنفيذ أوامر الشل (سنستخدمها لاحقاً في السكربتات)
    private void executeShizukuCommand(String command) {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "الرجاء منح صلاحية Shizuku أولاً!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            // تنفيذ الأمر كـ Shizuku Shell
            new ProcessBuilder("sh", "-c", command).start();
        } catch (Exception e) {
            Toast.makeText(this, "خطأ: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Shizuku.removeRequestPermissionResultListener(permissionListener);
    }
}
