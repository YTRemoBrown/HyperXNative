package com.hyperx.magiks;

import android.os.Bundle;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
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
                    Toast.makeText(MainActivity.this, sw.getText() + (isChecked ? " ON" : " OFF"), Toast.LENGTH_SHORT).show();
                });
            }
        }
    }
}
