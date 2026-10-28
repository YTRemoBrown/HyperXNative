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

        // 1. طلب صلاحية الكتابة على النظام (الفتح المباشر لتطبيقنا)
        btnWriteSettings.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.System.canWrite(this)) {
                    // هذه هي الطريقة التي تفتح صفحة التطبيق مباشرة
                    Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                    startActivity(intent);
                    Toast.makeText(this, "ابحث عن HyperX وفعل الخيار", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "الصلاحية ممنوحة مسبقاً!", Toast.LENGTH_SHORT).show();
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

        // 3. طلب صلاحية الإشعارات (Android 13+)
        btnNotifications.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQ_NOTIFICATIONS);
                } else {
                    Toast.makeText(this, "الإشعارات مفعلة مسبقاً!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // تحديث حالة الأزرار عند فتح الشاشة
        updateButtonStates(btnWriteSettings, btnBatteryOpt, btnNotifications);
    }

    private void updateButtonStates(Button write, Button battery, Button notif) {
        // حالة الكتابة على النظام
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Settings.System.canWrite(this)) {
            write.setText("✅ Granted / ممنوح");
            write.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF00FF00));
        }

        // حالة البطارية
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            android.os.PowerManager pm = (android.os.PowerManager) getSystemService(POWER_SERVICE);
            if (pm != null && pm.isIgnoringBatteryOptimizations(getPackageName())) {
                battery.setText("✅ Granted / ممنوح");
                battery.setBackgroundTintList(android.content.res.ColorStateList.valueOf(0xFF00FF00));
            }
        }

        // حالة الإشعارات
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
        // تحديث حالة الأزرار عند العودة للتطبيق
        Button btnWriteSettings = findViewById(R.id.btn_write_settings);
        Button btnBatteryOpt = findViewById(R.id.btn_battery_opt);
        Button btnNotifications = findViewById(R.id.btn_notifications);
        if (btnWriteSettings != null && btnBatteryOpt != null && btnNotifications != null) {
             updateButtonStates(btnWriteSettings, btnBatteryOpt, btnNotifications);
        }
    }
}
