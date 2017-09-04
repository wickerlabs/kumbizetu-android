package or.buni.bukit.interfaces;

/**
 * Created by yusuphwickama on 9/4/17.
 */

public interface BookingListener {
    void onComplete(String bookingId, Exception e);
}
