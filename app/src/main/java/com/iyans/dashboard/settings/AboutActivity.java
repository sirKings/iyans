package com.iyans.dashboard.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.iyans.R;

public class AboutActivity extends AppCompatActivity {
    String TAG = getClass().getSimpleName();
    private TextView titleTextView;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_about);
        webView = (WebView) findViewById(R.id.webView);
        titleTextView = (TextView) findViewById(R.id.titleTextView);
        webView.getSettings().setJavaScriptEnabled(true);
        String title = getIntent().getStringExtra("for_what");
        if (title.equalsIgnoreCase("About")) {
            titleTextView.setText("ABOUT");
            webView.loadUrl("http://mmdnow.com/DEMO/iyan/about_us");
        } else if (title.equalsIgnoreCase("Terms")) {
            titleTextView.setText("TERMS");
            webView.loadUrl("http://mmdnow.com/DEMO/iyan/privacy_policy");
        }
        if (title.equalsIgnoreCase("About this Version")) {
            titleTextView.setText("About This Version");
            webView.loadUrl("http://mmdnow.com/DEMO/iyan/about_us");
        }
    }

    public void backPage(View view) {
        finish();
    }
}
