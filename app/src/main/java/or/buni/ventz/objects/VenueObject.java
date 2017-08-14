package or.buni.ventz.objects;

import java.util.ArrayList;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class VenueObject {
    private String id, name, priceFrom, priceTo, locationDesc, accommodation;
    private int venueImage;
    private ArrayList<String> venueImages;

    public VenueObject(String id, String name, String priceFrom, String priceTo, String accommodation, String locationDesc) {
        this.name = name;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.locationDesc = locationDesc;
        this.id = id;
        this.accommodation = accommodation;
        this.venueImages = new ArrayList<>();
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
        return this.venueImages.get(0);
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

    public String getLocationDesc() {
        return locationDesc;
    }
}
