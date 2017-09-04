package or.buni.bukit.objects;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class VenueObject {
    private String id, name, desc, priceFrom, priceTo, locationDesc, accommodation;
    private int venueImage;
    private ArrayList<String> venueImages;
    private String venueJSON;
    private ArrayList<Date> bookings;

    public VenueObject(String id, String name, String desc, String priceFrom, String priceTo, String accommodation, String locationDesc) {
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.locationDesc = locationDesc;
        this.desc = desc;
        this.id = id;
        this.accommodation = accommodation;
        this.venueImages = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    public static VenueObject parse(String json) throws JSONException {
        JSONObject venueObject = new JSONObject(json);
        String venID = venueObject.getString("venueId");
        String venName = venueObject.getString("venueName");
        String venLocDesc = venueObject.getString("locationDesc");
        String venPrice = venueObject.getString("price");
        String venAdvPrice = venueObject.getString("advancePrice");
        String venSize = venueObject.getString("accumulation");
        String description = venueObject.getString("venueDescription");

        VenueObject venue = new VenueObject(venID, venName, description, venAdvPrice, venPrice, venSize, venLocDesc);

        //Going demo mode for now...
        //venue.addImage("https://source.unsplash.com/collection/208285/");

        JSONArray imagesArray = venueObject.getJSONArray("images");
        JSONArray bookingsArray = venueObject.getJSONArray("bookings");
        //Log.i("[+] Venue->".concat(venID).concat("->found"), String.valueOf(imagesArray.length()));

        for (int j = 0; j < imagesArray.length(); j++) {
            JSONObject image = imagesArray.getJSONObject(j);
            String imageURL = image.getString("image");

            //Log.i("[+] Venue image ->", imageURL);
            venue.addImage(imageURL);
        }

        for (int j = 0; j < bookingsArray.length(); j++) {
            JSONObject dateObject = bookingsArray.getJSONObject(j);
            String date = dateObject.getString("date");
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            try {
                venue.addBooking(format.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        venue.setVenueJSON(venueObject.toString());

        return venue;
    }

    public float getFinalPrice() {
        float venPrice = Float.parseFloat(this.getPriceFrom());
        float initialPay = 0.15f * venPrice;
        float serviceCharge = 0.005f * venPrice;

        float totalPrice = initialPay + serviceCharge;

        return totalPrice;
    }

    public String getVenueJSON() {
        return venueJSON;
    }

    private void setVenueJSON(String venueJSON) {
        this.venueJSON = venueJSON;
    }

    public String getDescription() {
        return desc;
    }

    public int getVenueImage() {
        return venueImage;
    }

    public void setVenueImage(int venueImage) {
        this.venueImage = venueImage;
    }

    private void addImage(String imageURL) {
        this.venueImages.add(imageURL);
    }

    private void addBooking(Date date) {
        this.bookings.add(date);
    }

    public ArrayList<Date> getBookings() {
        return this.bookings;
    }

    public ArrayList<String> getVenueImages() {
        return this.venueImages;
    }

    public String getPreviewImage() {
        return (!this.venueImages.isEmpty()) ? this.venueImages.get(0) : "";
    }

    public String getId() {
        return id;
    }

    public String getAccommodation() {
        return accommodation;
    }

    public String getName() {
        return name;
    }

    public String getPriceFrom() {
        return priceFrom;
    }

    public String getPriceTo() {
        return priceTo;
    }

    public String getPrettyPrice() {
        DecimalFormat format = new DecimalFormat("#,###");

        Float price = Float.parseFloat(this.getPriceFrom());

        return format.format(price);
    }

    public String getLocationDesc() {
        return locationDesc;
    }
}
