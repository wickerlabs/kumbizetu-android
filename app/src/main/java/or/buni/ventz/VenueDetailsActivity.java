package or.buni.ventz;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.roomorama.caldroid.CaldroidFragment;

import org.json.JSONException;

import java.util.Date;

import or.buni.ventz.adapters.VenueSectionAdapter;
import or.buni.ventz.fragments.VenueOverview;
import or.buni.ventz.objects.VenueObject;
import or.buni.ventz.util.Formatter;

public class VenueDetailsActivity extends AppCompatActivity {

    private VenueObject venue;
    private ImageView imageView;
    private TextView name, place, price, description;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private VenueSectionAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");

        try {
            venue = VenueObject.parse(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name = (TextView) findViewById(R.id.venueName);
        price = (TextView) findViewById(R.id.priceRange);
        place = (TextView) findViewById(R.id.location);

        name.setText(venue.getName());
        price.setText(Formatter.shortenMoney(Float.parseFloat(venue.getPriceFrom())));
        place.setText(venue.getLocationDesc());
        String size = venue.getAccommodation();
        VenueOverview.getInstance().setSize(size);
        VenueOverview.getInstance().setName(venue.getName());

        imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this).load(venue.getPreviewImage()).into(imageView);

        adapter = new VenueSectionAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        mViewPager = (ViewPager) findViewById(R.id.containerDetails);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(mViewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CaldroidFragment fragment = CaldroidFragment.newInstance("Pick date of event", new Date().getMonth(), new Date().getDay());
                fragment.setMinDate(new Date());

                fragment.show(getSupportFragmentManager(), "CALDROID_TAG");
                //Toast.makeText(VenueDetailsActivity.this, "Cooming soon...", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Log.d("[+] " + VenueDetailsActivity.class.getSimpleName(), "Back!");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("[+] " + VenueDetailsActivity.class.getSimpleName(), "Destroy!");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("[+] " + VenueDetailsActivity.class.getSimpleName(), "Stop!");

    }
}
