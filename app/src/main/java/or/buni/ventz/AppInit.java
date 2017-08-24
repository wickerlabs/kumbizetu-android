package or.buni.ventz;

import android.app.Application;

import com.miguelcatalan.materialsearchview.Suggestion;

import java.util.ArrayList;

/**
 * Created by yusuphwickama on 8/23/17.
 */

public class AppInit extends Application {

    private static ArrayList<Suggestion> suggestions = new ArrayList<>(15);
    private static ArrayList<String> suggestionMap = new ArrayList<>(15);

    public static void addSuggestion(ArrayList<Suggestion> nSuggestions) {
        suggestions.clear();
        if (nSuggestions.size() == suggestions.size()) {

            suggestions.addAll(nSuggestions);
        } else {
            suggestions.addAll(0, nSuggestions);
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
