package or.buni.ventz.interfaces;

import java.util.ArrayList;

import or.buni.ventz.objects.VenueObject;

/**
 * Created by yusuphwickama on 8/14/17.
 */

public interface GetVenueCallback {
    void onComplete(ArrayList<VenueObject> venues, Exception e);
}
