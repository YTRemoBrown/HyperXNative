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
        // 1. FPS Unlocker
        setupSwitch(R.id.switch_fps, 
            "settings put global game_default_frame_rate_override 120; settings put system peak_refresh_rate 120", 
            "settings delete global game_default_frame_rate_override; settings delete system peak_refresh_rate");

        // 2. GPU Optimizer
        setupSwitch(R.id.switch_gpu, 
            "settings put global force_gpu_rendering 1", 
            "settings put global force_gpu_rendering 0");

        // 3. Thermal Controller
        setupSwitch(R.id.switch_thermal, 
            "settings put global thermal_status 0", 
            "settings put global thermal_status 1");

        // 4. RAM Booster
        setupSwitch(R.id.switch_ram, 
            "am kill-all", 
            "echo 'RAM Restored'");

        // 5. CPU Turbo
        setupSwitch(R.id.switch_cpu, 
            "settings put global sem_enhanced_cpu_responsiveness 1", 
            "settings put global sem_enhanced_cpu_responsiveness 0");

        // 6. Network Boost
        setupSwitch(R.id.switch_network, 
            "settings put global tcp_default_init_rwnd 60", 
            "settings delete global tcp_default_init_rwnd");
            
        // (باقي الأزرار سيتم ربطها لاحقاً)
        setupSwitch(R.id.switch_skiagl, "echo 'SkiaGL ON'", "echo 'SkiaGL OFF'");
        setupSwitch(R.id.switch_vulkan, "echo 'Vulkan ON'", "echo 'Vulkan OFF'");
        setupSwitch(R.id.switch_ultra, "echo 'Ultra ON'", "echo 'Ultra OFF'");
        setupSwitch(R.id.switch_aa, "echo 'AA ON'", "echo 'AA OFF'");
        setupSwitch(R.id.switch_hdr, "echo 'HDR ON'", "echo 'HDR OFF'");
        setupSwitch(R.id.switch_touch, "echo 'Touch ON'", "echo 'Touch OFF'");
        setupSwitch(R.id.switch_aim, "echo 'Aim ON'", "echo 'Aim OFF'");
        setupSwitch(R.id.switch_gyro, "echo 'Gyro ON'", "echo 'Gyro OFF'");
        setupSwitch(R.id.switch_multitouch, "echo 'Multi ON'", "echo 'Multi OFF'");
        setupSwitch(R.id.switch_battery, "echo 'Battery ON'", "echo 'Battery OFF'");
        setupSwitch(R.id.switch_doze, "echo 'Doze ON'", "echo 'Doze OFF'");
        setupSwitch(R.id.switch_cleaner, "echo 'Cleaner ON'", "echo 'Cleaner OFF'");
        setupSwitch(R.id.switch_adb, "echo 'ADB ON'", "echo 'ADB OFF'");
        setupSwitch(R.id.switch_120fps, "echo '120FPS ON'", "echo '120FPS OFF'");
    }

    // الدالة التي تنفذ السكربت
    private void setupSwitch(int id, String scriptOn, String scriptOff) {
        Switch sw = findViewById(id);
        if (sw != null) {
            sw.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    executeShizukuCommand(scriptOn);
                    Toast.makeText(this, sw.getText() + " مفعل", Toast.LENGTH_SHORT).show();
                } else {
                    executeShizukuCommand(scriptOff);
                    Toast.makeText(this, sw.getText() + " معطل", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // دالة تنفيذ الأوامر عبر Shizuku
    private void executeShizukuCommand(String command) {
        if (Shizuku.checkSelfPermission() != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "الرجاء منح صلاحية Shizuku أولاً!", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
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
