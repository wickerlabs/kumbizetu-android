package or.buni.ventz.objects;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yusuphwickama on 8/24/17.
 */

public class EventType {
    String id, name, imageUrl, type;

    public EventType(String id, String name, String imageUrl, String type) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.type = type;
    }

    public static EventType parse(String json) throws JSONException {
        JSONObject eventObject = new JSONObject(json);

        String id = eventObject.getString("evId");
        String name = eventObject.getString("eventName");
        String image = eventObject.getString("eventImage");
        String type = eventObject.getString("eventType");

        name = name.substring(0, 1).toUpperCase().concat(name.substring(1));

        return new EventType(id, name, image, type);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
