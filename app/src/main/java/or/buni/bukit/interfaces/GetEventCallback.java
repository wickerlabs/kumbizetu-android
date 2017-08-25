package or.buni.bukit.interfaces;

import java.util.ArrayList;

import or.buni.bukit.objects.EventType;

/**
 * Created by yusuphwickama on 8/14/17.
 */

public interface GetEventCallback {
    void onComplete(ArrayList<EventType> privEvents, ArrayList<EventType> copEvents, Exception e);
}
