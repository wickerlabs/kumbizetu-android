package or.buni.bukit.interfaces;

/**
 * Created by yusuphwickama on 9/4/17.
 */

public interface PaymentListener {

    void onCheckComplete(String status, Exception e);

}
