package or.buni.ventz.networking;

import android.support.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import or.buni.ventz.interfaces.GetCallback;
import or.buni.ventz.objects.VenueObject;
import or.buni.ventz.util.Constants;

/**
 * Created by yusuphwickama on 8/11/17.
 */

public class Backend {
    private static final Backend ourInstance = new Backend();
    private OkHttpClient client = new OkHttpClient();

    private Backend() {
    }

    public static Backend getInstance() {
        return ourInstance;
    }

    public void getVenues(final GetCallback callback) {
        final ArrayList<VenueObject> venues = new ArrayList<>();

        FormBody body = new FormBody.Builder()
                .add("action", "ALL-V")
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("[+] GetVenues", "Failed");
                callback.onComplete(null, e);
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                Log.i("[+] GetVenues", "Success");

                String venJSON = response.body().string();
                Log.i("[+] VenuesJSON", venJSON);

                try {
                    JSONArray array = new JSONArray(venJSON);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject venueObject = array.getJSONObject(i);
                        VenueObject venue = VenueObject.parse(venueObject.toString());

                        venues.add(venue);
                    }

                    callback.onComplete(venues, null);

                } catch (JSONException e) {
                    callback.onComplete(null, e);
                }

            }
        });

    }

}
