package or.buni.ventz.interfaces;

import java.util.ArrayList;

import or.buni.ventz.objects.EventType;

/**
 * Created by yusuphwickama on 8/14/17.
 */

public interface GetEventCallback {
    void onComplete(ArrayList<EventType> privEvents, ArrayList<EventType> copEvents, Exception e);
}
