package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.miguelcatalan.materialsearchview.FilterListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.utils.LoadingListener;

import java.util.ArrayList;

import or.buni.bukit.interfaces.GetSuggestions;
import or.buni.bukit.interfaces.GetVenueCallback;
import or.buni.bukit.networking.Backend;
import or.buni.bukit.objects.VenueObject;
import or.buni.ventz.R;

public class EventsActivity extends BaseActivity {

    private MaterialSearchView searchView;
    private LoadingListener loadingListener;
    private AlertDialog dialog;
    private String name, type, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view_events);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initiateSearchView();

        Intent intent = getIntent();
        type = intent.getStringExtra("name");
        id = intent.getStringExtra("typeId");
        name = intent.getStringExtra("name");

        getSupportActionBar().setTitle(type);

    }

    private void initiateSearchView() {
        loadingListener = new LoadingListener() {
            @Override
            public void isLoading() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView.isLoading(true);
                    }
                });

            }

            @Override
            public void loadingComplete() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        searchView.isLoading(false);
                    }
                });
            }
        };

        searchView.setActivity(this);
        searchView.setHint("What are you looking for?");
        searchView.setTextColor(R.color.colorAccent);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.setSubmitOnClick(false);
                loadingListener.isLoading();
                //Toast.makeText(EventsActivity.this, query, Toast.LENGTH_SHORT).show();

                Backend.getInstance().getVenueById(query, new GetVenueCallback() {
                    @Override
                    public void onComplete(ArrayList<VenueObject> venues, Exception e) {
                        loadingListener.loadingComplete();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                searchView.closeSearch();
                            }
                        });
                        if (e == null) {
                            Log.d(getClass().getSimpleName(), "[+] Found -> " + String.valueOf(venues.size() + " in " + name + " with Id " + id));
                            Intent intent = new Intent(EventsActivity.this, VenueDetailsActivity.class);
                            intent.putExtra("json", venues.get(0).getVenueJSON());

                            startActivity(intent);
                        } else {
                            e.printStackTrace();

                        }
                    }
                });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().length() >= 2) {
                    loadingListener.isLoading();
                    Backend.getInstance().getSuggestionsByEvent(id, newText, new GetSuggestions() {
                        @Override
                        public void onSuggest(Exception e) {
                            if (e == null) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        searchView.updateSuggestions(AppInit.getSuggestions());
                                    }
                                });
                            } else {
                                e.printStackTrace();
                            }

                            loadingListener.loadingComplete();
                        }
                    });
                }
                return false;
            }
        });
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
            }

            @Override
            public void onSearchViewClosed() {
                getSupportActionBar().show();
            }
        });

        searchView.setFilterListener(new FilterListener() {
            @Override
            public void onLaunchFilter() {
                View contentView = View.inflate(EventsActivity.this, R.layout.search_layout, null);
                dialog = new AlertDialog.Builder(EventsActivity.this)
                        .setView(contentView)
                        .show();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

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
        } else if (id == R.id.action_search) {
            //getSupportActionBar().hide();
            searchView.showSearch();
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }


}
