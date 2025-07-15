package controller;

import java.util.prefs.Preferences;

/**
 * Class acting as interface for the preferences API.
 */
public class Pref {
    public transient Preferences prefs;

    /**
     * Sets default preference for new instance.
     */
    public void setPreference() {
        // This will define a node in which the preferences can be stored
        prefs = Preferences.userRoot().node(this.getClass().getName());
        String darkMode = "darkmode";
        String remember = "remember";
        String username = "username";
        String password = "password";

        prefs.getBoolean(darkMode, false);
        prefs.getBoolean(remember, false);
        prefs.get(username, "");
        prefs.get(password, "");
    }

    /**
     * returns the preferences.
     *
     * @return prefs.
     */
    public Preferences getPrefs() {
        prefs = Preferences.userRoot().node(this.getClass().getName());
        return this.prefs;
    }
}

