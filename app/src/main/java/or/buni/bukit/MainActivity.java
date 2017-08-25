package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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

import or.buni.bukit.adapters.SectionsPageAdapter;
import or.buni.bukit.interfaces.GetSuggestions;
import or.buni.bukit.interfaces.GetVenueCallback;
import or.buni.bukit.networking.Backend;
import or.buni.bukit.objects.VenueObject;
import or.buni.bukit.util.ExtendedViewPager;
import or.buni.ventz.R;

import static android.R.attr.id;
import static android.R.attr.name;

public class MainActivity extends BaseActivity {


    AlertDialog dialog;
    LoadingListener loadingListener;
    private SectionsPageAdapter mSectionsPagerAdapter;
    private ExtendedViewPager mViewPager;
    private MaterialSearchView searchView;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int tabIcons[] = {
            R.drawable.venues,
            R.drawable.book,
            R.drawable.ic_favorite_black_24dp
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        setSupportActionBar(toolbar);

        mSectionsPagerAdapter = new SectionsPageAdapter(getSupportFragmentManager());

        mViewPager = (ExtendedViewPager) findViewById(R.id.container);
        mViewPager.setEnableSwipe(false);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[2]);
        //tabLayout.getTabAt(2).setIcon(tabIcons[2]);

        initiateSearchView();

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
                //Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();

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
                            Intent intent = new Intent(MainActivity.this, VenueDetailsActivity.class);
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
                    Backend.getInstance().getSuggestions(newText, new GetSuggestions() {
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
                tabLayout.setVisibility(View.VISIBLE);
            }
        });
        searchView.setFilterListener(new FilterListener() {
            @Override
            public void onLaunchFilter() {
                View contentView = View.inflate(MainActivity.this, R.layout.search_layout, null);
                dialog = new AlertDialog.Builder(MainActivity.this)
                        .setCancelable(true)
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
            tabLayout.setVisibility(View.INVISIBLE);
            getSupportActionBar().hide();
            searchView.showSearch();
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
