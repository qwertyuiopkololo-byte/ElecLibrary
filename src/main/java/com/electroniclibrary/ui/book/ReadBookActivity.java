package com.electroniclibrary.ui.book;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.electroniclibrary.R;

public class ReadBookActivity extends AppCompatActivity {
    private WebView webView;
    private String fileUrl;
    private String content;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        
        fileUrl = getIntent().getStringExtra("file_url");
        content = getIntent().getStringExtra("content");
        if (fileUrl == null && content == null) {
            finish();
            return;
        }
        
        initViews();
        loadBook();
    }
    
    private void initViews() {
        webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebViewClient(new WebViewClient());
    }
    
    private void loadBook() {
        if (fileUrl != null) {
            // For PDF files, use Google Docs viewer or similar
            if (fileUrl.endsWith(".pdf")) {
                webView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + fileUrl);
            } else {
                webView.loadUrl(fileUrl);
            }
            return;
        }
        
        if (content != null) {
            String html = "<html><head><meta charset='utf-8'></head><body style='padding:16px; font-size:16px; line-height:1.5;'>" +
                    content.replace("\n", "<br/>") +
                    "</body></html>";
            webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null);
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}

