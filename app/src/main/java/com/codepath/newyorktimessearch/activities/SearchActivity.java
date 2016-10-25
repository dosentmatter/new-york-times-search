package com.codepath.newyorktimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.codepath.newyorktimessearch.R;
import com.codepath.newyorktimessearch.adapters.ArticleArrayAdapter;
import com.codepath.newyorktimessearch.models.Article;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = "SearchActivity";

    EditText etQuery;
    GridView gvResults;
    Button btnSearch;

    ArrayList<Article> articles;
    ArticleArrayAdapter articleAdapter;

    private static final int REQUEST_FILTER = 0;

    private String date = "";
    private String sort = "";
    private int sortPosition = 0;
    private boolean arts = false;
    private boolean fashion = false;
    private boolean sports = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setupViews();
    }

    public void setupViews() {
        etQuery = (EditText)findViewById(R.id.etQuery);
        gvResults = (GridView)findViewById(R.id.gvResults);
        btnSearch = (Button)findViewById(R.id.btnSearch);

        articles = new ArrayList<>();
        articleAdapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(articleAdapter);

        setupGridViewListeners();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } if (id == R.id.action_filter) {
            setFilters();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFilters() {
        Intent filterIntent = new Intent(getApplicationContext(),
                                         FilterActivity.class);
        filterIntent.putExtra("date", date);
        filterIntent.putExtra("sortPosition", sortPosition);
        filterIntent.putExtra("arts", arts);
        filterIntent.putExtra("fashion", fashion);
        filterIntent.putExtra("sports", sports);
        startActivityForResult(filterIntent, REQUEST_FILTER);
    }

    private void setupGridViewListeners() {
        AdapterView.OnItemClickListener gridViewItemClickListener
            = new AdapterView.OnItemClickListener() {
                  @Override
                  public void onItemClick(AdapterView<?> parent, View view,
                                          int position, long id) {
                      Intent articleIntent
                          = new Intent(getApplicationContext(),
                                       ArticleActivity.class);
                      Article article = articles.get(position);
                      articleIntent.putExtra("article", article);
                      startActivity(articleIntent);
                  }
              };

        gvResults.setOnItemClickListener(gridViewItemClickListener);
    }

    public void onArticleSearch(View view) {
        String query = etQuery.getText().toString();

        AsyncHttpClient client = new AsyncHttpClient();
        final String URL = "https://api.nytimes.com/svc/search/v2/articlesearch.json";
        final String API_KEY = "8c64b0bb4a814231801480b038eb060e";

        RequestParams params = new RequestParams();
        params.put("api-key", API_KEY);
        params.put("page", 0);
        params.put("q", query);
        if (!TextUtils.isEmpty(date)) {
            params.put("begin_date", date);
        }
        if (!TextUtils.isEmpty(sort)) {
            params.put("sort", sort);
        }
        if (arts || fashion || sports) {
            ArrayList<String> filter = new ArrayList<>();
            if (arts) {
                filter.add("\"Arts\"");
            }
            if (fashion) {
                filter.add("\"Fashion & Style\"");
            }
            if (sports) {
                filter.add("\"Sports\"");
            }
            String filterQuery = String.format("news_desk:(%s)",
                                               TextUtils.join(" ", filter));
            params.put("fq", filterQuery);
        }
        client.get(URL, params,
                   new JsonHttpResponseHandler() {
                       @Override
                       public void onSuccess(int statusCode, Header[] headers,
                                             JSONObject response) {
                           JSONArray articleJsonResults = null;

                           try {
                               articleJsonResults
                                   = response.getJSONObject("response")
                                             .getJSONArray("docs");
                               ArrayList<Article> articleResults
                                   = Article.fromJSONArray(articleJsonResults);
                               articles.clear();
                               articles.addAll(articleResults);
                               articleAdapter.notifyDataSetChanged();
                           } catch (JSONException e) {
                               Log.e(LOG_TAG, e.getMessage());
                           }
                       }

                       @Override
                       public void onFailure(int statusCode, Header[] headers,
                                             String responseString,
                                             Throwable throwable) {
                           super.onFailure(statusCode, headers, responseString,
                                           throwable);
                       }
                   });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_FILTER) {
            date = data.getStringExtra("date");
            sort = data.getStringExtra("sort");
            sortPosition = data.getIntExtra("sortPosition", 0);
            arts = data.getBooleanExtra("arts", false);
            fashion = data.getBooleanExtra("fashion", false);
            sports = data.getBooleanExtra("sports", false);
        }
    }
}
