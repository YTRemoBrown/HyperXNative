package com.hyperx.magiks;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import rikka.shizuku.Shizuku;

public class PerformanceFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_performance, container, false);

        // زر الصلاحيات
        Button btnPerm = view.findViewById(R.id.btn_permissions);
        btnPerm.setOnClickListener(v -> startActivity(new Intent(getActivity(), PermissionsActivity.class)));

        // حالة Shizuku
        TextView tvStatus = view.findViewById(R.id.tv_shizuku_status);
        if (Shizuku.pingBinder()) {
            tvStatus.setText("✅ Shizuku Active");
            tvStatus.setTextColor(0xFF00FF00);
        } else {
            tvStatus.setText("❌ Shizuku Not Running");
        }

        return view;
    }
}
