package or.buni.ventz.objects;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

import or.buni.ventz.util.Constants;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class VenueObject {
    private String id, name, priceFrom, priceTo, locationDesc, accommodation;
    private int venueImage;
    private ArrayList<String> venueImages;
    private String venueJSON;

    public VenueObject(String id, String name, String priceFrom, String priceTo, String accommodation, String locationDesc) {
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.locationDesc = locationDesc;
        this.id = id;
        this.accommodation = accommodation;
        this.venueImages = new ArrayList<>();
    }

    public static VenueObject parse(String json) throws JSONException {
        JSONObject venueObject = new JSONObject(json);
        String venID = venueObject.getString("venueId");
        String venName = venueObject.getString("venueName");
        String venLocDesc = venueObject.getString("locationDesc");
        String venPrice = venueObject.getString("price");
        String venAdvPrice = venueObject.getString("advancePrice");
        String venSize = venueObject.getString("accumulation");

        VenueObject venue = new VenueObject(venID, venName, venAdvPrice, venPrice, venSize, venLocDesc);


        JSONArray imagesArray = venueObject.getJSONArray("images");
        Log.i("[+] Venue->".concat(venID).concat("->found"), String.valueOf(imagesArray.length()));

        for (int j = 0; j < imagesArray.length(); j++) {
            JSONObject image = imagesArray.getJSONObject(j);
            String imageURL = image.getString("image");

            Log.i("[+] Venue->".concat(venID).concat("->image"), imageURL);
            venue.addImage(Constants.IMAGE_URL.concat(imageURL));
        }

        venue.setVenueJSON(venueObject.toString());

        return venue;
    }

    public String getVenueJSON() {
        return venueJSON;
    }

    public void setVenueJSON(String venueJSON) {
        this.venueJSON = venueJSON;
    }

    public int getVenueImage() {
        return venueImage;
    }

    public void setVenueImage(int venueImage) {
        this.venueImage = venueImage;
    }

    public void addImage(String imageURL) {
        this.venueImages.add(imageURL);
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
