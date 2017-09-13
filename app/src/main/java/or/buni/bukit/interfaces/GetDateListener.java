package or.buni.bukit.interfaces;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by yusuphwickama on 9/4/17.
 */

public interface GetDateListener {

    void onGetDatesComplete(ArrayList<Date> dates, Exception e);

}
