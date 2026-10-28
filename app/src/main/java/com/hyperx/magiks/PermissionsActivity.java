package com.hyperx.magiks;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsActivity extends AppCompatActivity {

    private static final int REQ_NOTIFICATIONS = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);

        Button btnWriteSettings = findViewById(R.id.btn_write_settings);
        Button btnBatteryOpt = findViewById(R.id.btn_battery_opt);
        Button btnNotifications = findViewById(R.id.btn_notifications);

        // 1. طلب صلاحية الكتابة على النظام (يدوياً)
        btnWriteSettings.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(this)) {
                    Toast.makeText(this, "الصلاحية ممنوحة مسبقاً!", Toast.LENGTH_SHORT).show();
                } else {
                    // فتح شاشة معلومات التطبيق مباشرة
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    Toast.makeText(this, "اضغط على 3 نقاط (⋮) -> Allow restricted settings", Toast.LENGTH_LONG).show();
                }
            }
        });

        // 2. طلب تجاهل تحسين البطارية
        btnBatteryOpt.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });

        // 3. طلب صلاحية الإشعارات
        btnNotifications.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFICATIONS);
                } else {
                    Toast.makeText(this, "الإشعارات مفعلة مسبقاً!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // تحديث حالة الأزرار
        updateButtonStates(btnWriteSettings, btnBatteryOpt, btnNotifications);
    }

    private void updateButtonStates(Button write, Button battery, Button notif) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
            write.setText("✅ Granted / ممنوح");
            write.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF00FF00));
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.os.PowerManager pm = (android.os.PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null && pm.isIgnoringBatteryOptimizations(getPackageName())) {
                battery.setText("✅ Granted / ممنوح");
                battery.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF00FF00));
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                notif.setText("✅ Granted / ممنوح");
                notif.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF00FF00));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Button btnWriteSettings = findViewById(R.id.btn_write_settings);
        Button btnBatteryOpt = findViewById(R.id.btn_battery_opt);
        Button btnNotifications = findViewById(R.id.btn_notifications);
        if (btnWriteSettings != null) {
             updateButtonStates(btnWriteSettings, btnBatteryOpt, btnNotifications);
        }
    }
}
