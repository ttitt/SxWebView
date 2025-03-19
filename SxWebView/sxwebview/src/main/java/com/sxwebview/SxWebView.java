package com.sxwebview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.*;

public class SxWebView extends WebView {
    private String authToken;
    private Activity activity;
    private ValueCallback<Uri[]> filePathCallback;

    public SxWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (context instanceof Activity) {
            this.activity = (Activity) context;
        }
        initWebView();
    }

    private void initWebView() {
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        setWebViewClient(new WebViewClient() {
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (authToken != null) {
                    request.getRequestHeaders().put("Authorization", "Bearer " + authToken);
                }
                return super.shouldInterceptRequest(view, request);
            }
        });

        setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                SxWebView.this.filePathCallback = filePathCallback;
                Intent intent = fileChooserParams.createIntent();
                activity.startActivityForResult(intent, 100);
                return true;
            }
        });

        setDownloadListener((url, userAgent, contentDisposition, mimetype, contentLength) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            getContext().startActivity(intent);
        });
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public void handleActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100 && filePathCallback != null) {
            Uri[] result = data == null || resultCode != Activity.RESULT_OK ? new Uri[]{} : new Uri[]{data.getData()};
            filePathCallback.onReceiveValue(result);
            filePathCallback = null;
        }
    }
}
