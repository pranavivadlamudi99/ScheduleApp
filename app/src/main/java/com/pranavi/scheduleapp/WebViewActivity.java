package com.pranavi.scheduleapp;

import android.os.Bundle;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Simple WebView-based activity to load external links from schedule buttons
 * (like Game Center or Highlights).
 */
public class WebViewActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WebView webView = new WebView(this);
        setContentView(webView);

        String url = getIntent().getStringExtra("url");
        if (url != null && url.startsWith("http")) {
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(url);
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
