package or.buni.bukit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayout;
import android.support.v7.widget.Toolbar;
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

import or.buni.bukit.objects.VenueObject;
import or.buni.ventz.R;

public class VenueActivity extends BaseActivity {

    private AlertDialog dialog;
    private ImageView imageView;
    private VenueObject venue;
    private TextView name, place, description;
    private ArrayList<Date> venueDates;
    private GridLayout imageGrid;

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
        imageGrid = (GridLayout) findViewById(R.id.imageGrid);

        name.setText(venue.getName());

        place.setText(venue.getLocationDesc());
        String size = venue.getAccommodation();

        //getSupportActionBar().setTitle(venue.getName());

        description = (TextView) findViewById(R.id.venDescription);
        description.setText(venue.getDescription());

        venueDates = venue.getBookings();

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
                    Toast.makeText(VenueActivity.this, dateSelected, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(VenueActivity.this, "Select a date to continue", Toast.LENGTH_SHORT).show();
                }
                //dialog.dismiss();
            }
        });


        calendar.highlightDates(venueDates);

        dialog = new AlertDialog.Builder(VenueActivity.this)
                .setCancelable(false)
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
        if (dialog != null) {
            dialog.dismiss();
        } else {

            super.onBackPressed();
        }

    }
}
