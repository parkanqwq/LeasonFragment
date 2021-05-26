package com.auction.leasonfragment;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.auction.leasonfragment.fragment.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState == null){
            SettingsFragment fragment = new SettingsFragment();
            fragment.setArguments(getIntent().getExtras());

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_settings, fragment)
                    .commit();
        }
    }
}