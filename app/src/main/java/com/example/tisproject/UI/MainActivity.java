
package com.example.tisproject.UI;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.tisproject.R;

public class MainActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private ImageView weatherIcon;
    private TextView appTitle, appSubtitle, appDescription;
    private Button btnGetStarted, btnSkip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupClickListeners();
        animateWeatherIcon();
    }

    private void initViews() {
        weatherIcon = findViewById(R.id.weather_icon);
        appTitle = findViewById(R.id.app_title);
        appSubtitle = findViewById(R.id.app_subtitle);
        appDescription = findViewById(R.id.app_description);
        btnGetStarted = findViewById(R.id.btn_get_started);
        btnSkip = findViewById(R.id.btn_skip);
    }

    private void setupClickListeners() {
        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLocationPermissionAndProceed();
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proceedToDetailActivity();
            }
        });
    }

    private void animateWeatherIcon() {
        weatherIcon.animate()
                .rotation(360f)
                .setDuration(2000)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        weatherIcon.animate()
                                .scaleX(1.1f)
                                .scaleY(1.1f)
                                .setDuration(1000)
                                .withEndAction(new Runnable() {
                                    @Override
                                    public void run() {
                                        weatherIcon.animate()
                                                .scaleX(1.0f)
                                                .scaleY(1.0f)
                                                .setDuration(1000);
                                    }
                                });
                    }
                });
    }

    private void checkLocationPermissionAndProceed() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            proceedToDetailActivity();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                proceedToDetailActivity();
            } else {
                Toast.makeText(this, "Location permission needed for better experience", Toast.LENGTH_SHORT).show();
                proceedToDetailActivity();
            }
        }
    }

    private void proceedToDetailActivity() {
        Intent intent = new Intent(MainActivity.this, WeatherDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}
