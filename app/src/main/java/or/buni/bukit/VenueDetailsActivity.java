package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import or.buni.bukit.adapters.VenueSectionAdapter;
import or.buni.bukit.fragments.VenueOverview;
import or.buni.bukit.objects.VenueObject;
import or.buni.bukit.util.Formatter;
import or.buni.ventz.R;

public class VenueDetailsActivity extends BaseActivity {

    AlertDialog dialog;
    private VenueObject venue;
    private ImageView imageView;
    private TextView name, place, price, description;
    private ViewPager mViewPager;
    private TabLayout tabLayout;
    private VenueSectionAdapter adapter;
    private ArrayList<Date> venueDates;

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
        VenueOverview.getInstance().setDesc(venue.getDescription());

        venueDates = venue.getBookings();

        imageView = (ImageView) findViewById(R.id.imageView);

        Glide.with(this).load(venue.getPreviewImage()).into(imageView);


        adapter = new VenueSectionAdapter(getSupportFragmentManager());
        tabLayout = (TabLayout) findViewById(R.id.tablayout);

        mViewPager = (ViewPager) findViewById(R.id.containerDetails);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(mViewPager);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(1, true);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                View dateView = View.inflate(VenueDetailsActivity.this, R.layout.date_picker, null);

                final Calendar nextYear = Calendar.getInstance();
                nextYear.add(Calendar.MONTH, 6);

                final Calendar lastYear = Calendar.getInstance();

                ImageButton submit = dateView.findViewById(R.id.submitDate);
                ImageButton cancel = dateView.findViewById(R.id.closeDate);
                final CalendarPickerView calendar = dateView.findViewById(R.id.calendar_view);


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });


                //calendar.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,600));

                calendar.init(lastYear.getTime(), nextYear.getTime())//
                        .inMode(CalendarPickerView.SelectionMode.SINGLE);


                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (calendar.getSelectedDate() != null) {
                            String dateSelected = String.format("Time choosen is %s", calendar.getSelectedDate().toString());
                            Toast.makeText(VenueDetailsActivity.this, dateSelected, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(VenueDetailsActivity.this, "Select a date to continue", Toast.LENGTH_SHORT).show();
                        }
                        //dialog.dismiss();
                    }
                });


                calendar.highlightDates(venueDates);

                dialog = new AlertDialog.Builder(VenueDetailsActivity.this)
                        .setCancelable(false)
                        .setView(dateView)
                        .show();


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
