package or.buni.ventz;

import android.app.Application;

import com.miguelcatalan.materialsearchview.Suggestion;

import java.util.ArrayList;

/**
 * Created by yusuphwickama on 8/23/17.
 */

public class AppInit extends Application {

    private static ArrayList<Suggestion> suggestions = new ArrayList<>(5);
    private static ArrayList<String> suggestionMap = new ArrayList<>();

    public static void addSuggestion(Suggestion suggestion) {

        if (!suggestionMap.contains(suggestion.getId())) {
            if (suggestions.size() == 5) {
                suggestions.remove(suggestions.size() - 1);
            }
            suggestions.add(0, suggestion);
            suggestionMap.add(suggestion.getId());
        }
    }

    public static ArrayList<Suggestion> getSuggestions() {
        return suggestions;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

}
