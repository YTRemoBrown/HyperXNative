package com.hyperx.magiks;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import java.util.Locale;

public class SettingsFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // تبديل اللغة (عربي / إنجليزي)
        Switch switchLang = view.findViewById(R.id.switch_lang);
        switchLang.setOnCheckedChangeListener((buttonView, isChecked) -> {
            Locale locale = isChecked ? new Locale("ar") : new Locale("en");
            Locale.setDefault(locale);
            Configuration config = new Configuration();
            config.setLocale(locale);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
            getActivity().recreate(); // إعادة تشغيل الواجهة لتطبيق اللغة
        });

        // تبديل الوضع الداكن
        Switch switchDark = view.findViewById(R.id.switch_dark);
        switchDark.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        return view;
    }
}
