package or.buni.bukit.interfaces;

import java.util.ArrayList;

import or.buni.bukit.objects.VenueObject;

/**
 * Created by yusuphwickama on 8/14/17.
 */

public interface GetVenueCallback {
    void onComplete(ArrayList<VenueObject> venues, Exception e);
}
