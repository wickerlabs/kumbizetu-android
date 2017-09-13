package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.savvi.rangedatepicker.CalendarPickerView;

import org.json.JSONException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import or.buni.bukit.adapters.GalleryAdapter;
import or.buni.bukit.interfaces.BookingListener;
import or.buni.bukit.interfaces.GetDateListener;
import or.buni.bukit.networking.Backend;
import or.buni.bukit.objects.VenueObject;
import or.buni.ventz.R;

public class VenueActivity extends BaseActivity {

    private AlertDialog dialog;
    private ImageView imageView;
    private VenueObject venue;
    private TextView name, place, description, details;
    private ArrayList<Date> venueDates;
    private GridLayout imageGrid;
    private CalendarPickerView calendar;
    private ProgressBar progressBar;
    private GridView galleryRecycler;
    private GalleryAdapter galleryAdapter;
    private ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venue_overview);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        String json = intent.getStringExtra("json");

        try {
            venue = VenueObject.parse(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        name = (TextView) findViewById(R.id.venueName);
        place = (TextView) findViewById(R.id.location);
        galleryRecycler = (GridView) findViewById(R.id.imageGrid);
        galleryRecycler.setNumColumns(3);

        galleryAdapter = new GalleryAdapter(VenueActivity.this, R.layout.gallery_item, venue.getVenueImages());

        galleryRecycler.setAdapter(galleryAdapter);

        name.setText(venue.getName());

        place.setText(venue.getLocationDesc());
        String size = venue.getAccommodation();

        //getSupportActionBar().setTitle(venue.getName());

        description = (TextView) findViewById(R.id.venDescription);
        description.setText(venue.getDescription());

        scrollView = (ScrollView) findViewById(R.id.mScrollView);

        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        details = (TextView) findViewById(R.id.venDetail);


        String venDetails = String.format("Booking price - %s \nAccommodates - %s people", venue.getPrettyPrice().concat("/="), venue.getAccommodation());

        details.setText(venDetails);

        imageView = (ImageView) findViewById(R.id.previewImage);

        Glide.with(this).load(venue.getPreviewImage()).into(imageView);

        ImageButton fav = (ImageButton) findViewById(R.id.favBtn);
        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(VenueActivity.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void launchDateDialog(View view) {
        View dateView = View.inflate(VenueActivity.this, R.layout.date_picker, null);

        final Calendar nextYear = Calendar.getInstance();
        // allow booking for the next 6 months
        nextYear.add(Calendar.MONTH, 6);

        final Calendar lastYear = Calendar.getInstance();

        ImageButton submit = dateView.findViewById(R.id.submitDate);
        ImageButton cancel = dateView.findViewById(R.id.closeDate);
        calendar = dateView.findViewById(R.id.calendar_view);
        progressBar = dateView.findViewById(R.id.calendarProgress);

        progressBar.setVisibility(View.VISIBLE);
        calendar.setVisibility(View.INVISIBLE);

        Backend.getInstance().getVenueDates(venue.getId(), new GetDateListener() {
            @Override
            public void onGetDatesComplete(final ArrayList<Date> dates, Exception e) {
                if (e == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calendar.highlightDates(dates);
                        }
                    });
                } else {
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        calendar.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //calendar.setLayoutParams(new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,600));



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (calendar.getSelectedDate() != null) {

                    DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String dateSelected = format.format(calendar.getSelectedDate());

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.VISIBLE);
                            calendar.setVisibility(View.INVISIBLE);

                        }
                    });

                    Backend.getInstance().checkAvailability(venue.getId(), dateSelected, new BookingListener() {
                        @Override
                        public void onComplete(final String bookingId, Exception e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressBar.setVisibility(View.GONE);
                                    calendar.setVisibility(View.VISIBLE);
                                }
                            });
                            if (e == null) {
                                if (bookingId != null && !bookingId.equals("NOT_OKAY")) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(VenueActivity.this, PaymentActivity.class);
                                            intent.putExtra("bid", bookingId);
                                            intent.putExtra("vid", venue.getId());
                                            startActivity(intent);
                                            dialog.dismiss();
                                            calendar.setVisibility(View.VISIBLE);
                                        }
                                    });

                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            calendar.setVisibility(View.VISIBLE);
                                            Toast.makeText(VenueActivity.this, "Try choosing another date", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        calendar.setVisibility(View.VISIBLE);
                                        Toast.makeText(VenueActivity.this, "Try choosing another date", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                e.printStackTrace();
                            }
                        }
                    });

                } else {
                    Toast.makeText(VenueActivity.this, "Select a date to continue", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
            }
        });

        calendar.init(lastYear.getTime(), nextYear.getTime())//
                .inMode(CalendarPickerView.SelectionMode.SINGLE);

        dialog = new AlertDialog.Builder(VenueActivity.this)
                .setView(dateView)
                .show();

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
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        } else {
            super.onBackPressed();
        }

    }
}
