package com.codepath.newyorktimessearch.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.codepath.newyorktimessearch.R;
import com.codepath.newyorktimessearch.models.Article;

public class ArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Article article = (Article)getIntent().getSerializableExtra("article");

        WebView webView = (WebView)findViewById(R.id.wvArticle);

        WebViewClient webViewClient
            = new WebViewClient() {
                  @Override
                  public boolean shouldOverrideUrlLoading(WebView view,
                                                          String url) {
                      view.loadUrl(url);
                      return true;
                  }
              };
        webView.setWebViewClient(webViewClient);
        webView.loadUrl(article.getWebUrl());
    }

}
