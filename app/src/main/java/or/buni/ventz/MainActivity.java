package or.buni.ventz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.miguelcatalan.materialsearchview.FilterListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.miguelcatalan.materialsearchview.utils.LoadingListener;

import or.buni.ventz.adapters.SectionsPageAdapter;
import or.buni.ventz.interfaces.GetSuggestions;
import or.buni.ventz.networking.Backend;

public class MainActivity extends AppCompatActivity {


    AlertDialog dialog;
    LoadingListener loadingListener;
    private SectionsPageAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
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

        mViewPager = (ViewPager) findViewById(R.id.container);
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
                Toast.makeText(MainActivity.this, query, Toast.LENGTH_SHORT).show();
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
