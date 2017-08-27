package or.buni.bukit.networking;

import android.support.annotation.NonNull;
import android.util.Log;

import com.miguelcatalan.materialsearchview.Suggestion;

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
import or.buni.bukit.AppInit;
import or.buni.bukit.interfaces.GetEventCallback;
import or.buni.bukit.interfaces.GetSuggestions;
import or.buni.bukit.interfaces.GetVenueCallback;
import or.buni.bukit.objects.EventType;
import or.buni.bukit.objects.VenueObject;
import or.buni.bukit.util.Constants;

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

    public void getVenueById(String venId, GetVenueCallback callback) {
        this.getVenues(venId, "", "V-ID", callback);
    }

    public void getVenuesByEvent(String eventId, GetVenueCallback callback) {
        this.getVenues("", eventId, "V-EV", callback);
    }

    public void getVenues(GetVenueCallback callback) {
        this.getVenues("", "", "ALL-V", callback);
    }

    private void getVenues(String venId, String eventId, String action, final GetVenueCallback callback) {
        final ArrayList<VenueObject> venues = new ArrayList<>();

        FormBody body = new FormBody.Builder()
                .add("action", action)
                .add("evid", eventId)
                .add("vid", venId)
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
                //Log.i("[+] VenuesJSON", venJSON);

                try {
                    JSONArray array = new JSONArray(venJSON);
                    Log.i("[+] Found", String.valueOf(array.length()));

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

    public void getEvents(final GetEventCallback callback) {
        final ArrayList<EventType> events = new ArrayList<>();
        final ArrayList<EventType> copEvents = new ArrayList<>();

        FormBody body = new FormBody.Builder()
                .add("action", "ALL-E")
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, IOException e) {
                Log.e("[+] GetVenues", "Failed");
                callback.onComplete(null, null, e);
            }

            @Override
            public void onResponse(@NonNull Call call, Response response) throws IOException {
                Log.i("[+] GetEvents", "Success");

                String venJSON = response.body().string();
                //Log.i("[+] Events", venJSON);

                try {
                    JSONArray array = new JSONArray(venJSON);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        EventType event = EventType.parse(json.toString());

                        if (event.getType().equalsIgnoreCase("cooperate")) {
                            copEvents.add(event);
                        } else if (event.getType().equalsIgnoreCase("private")) {
                            events.add(event);
                        }

                    }

                    callback.onComplete(events, copEvents, null);

                } catch (JSONException e) {
                    callback.onComplete(null, null, e);
                }

            }
        });

    }

    public void getSuggestionsByEvent(String event, String query, GetSuggestions suggestCallback) {
        this.getSuggestions(event, query, "SUG-VE", suggestCallback);
    }

    public void getSuggestions(String query, GetSuggestions callback) {
        this.getSuggestions("", query, "SUG-V", callback);
    }

    private void getSuggestions(String eventId, String query, String action, final GetSuggestions suggestionsCallback) {

        FormBody body = new FormBody.Builder()
                .add("action", action)
                .add("query", query)
                .add("evid", eventId)
                .build();

        Request request = new Request.Builder()
                .url(Constants.URL)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                suggestionsCallback.onSuggest(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String suggestions = response.body().string();
                //Log.i("[+] Suggestions", suggestions);
                try {
                    JSONArray sugArray = new JSONArray(suggestions);
                    ArrayList<Suggestion> tempList = new ArrayList<>();
                    for (int i = 0; i < sugArray.length(); i++) {
                        JSONObject object = sugArray.getJSONObject(i);
                        String venueName = object.getString("venueName");
                        String venueLocation = object.getString("venueLocation");
                        String venueId = object.getString("venueId");

                        Suggestion suggestion = new Suggestion();
                        suggestion.setId(venueId);
                        suggestion.setLocation(venueLocation);
                        suggestion.setName(venueName);

                        tempList.add(suggestion);
                    }

                    AppInit.addSuggestion(tempList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                suggestionsCallback.onSuggest(null);
            }
        });

    }



}
