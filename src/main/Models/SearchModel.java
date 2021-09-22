package main.Models;

import javafx.collections.ObservableList;
import main.Utility.Suggestion;

import java.util.ArrayList;
import java.util.Locale;

public class SearchModel {

    public static ArrayList<Suggestion> suggestionList = new ArrayList<>();

    public static void searchPastActivityListForNameAndLoadToObservableList(String targetFragment, ObservableList<String> list, String category) {
        targetFragment = targetFragment.toLowerCase();
        String name;
        String suggestionCategory;

        for (Suggestion suggestion : suggestionList) {
            name = suggestion.getName();
            suggestionCategory = suggestion.getCategory();

            if (name.contains(targetFragment)) {

                if (suggestionCategory.equals(category) || category == null) {
                    list.add(name);
                }
            }
        }
    }


    public static void searchPastActivityListForNameAndLoadToObservableListInReverse(String targetFragment, ObservableList<String> list, String category) {
        targetFragment = targetFragment.toLowerCase();
        String name;
        String suggestionCategory;

        for (int i = suggestionList.size() - 1; i >= 0; i--) {
            name = suggestionList.get(i).getName();
            suggestionCategory = suggestionList.get(i).getCategory();

            if (name.contains(targetFragment)) {

                if (suggestionCategory.equals(category) || category == null) {
                    list.add(name);
                }
            }
        }
    }
}