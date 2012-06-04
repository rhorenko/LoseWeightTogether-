package org.googlecode.vkontakte_android.utils;

public class PropertiesHolder {
    String property;
    String meaning;

    public PropertiesHolder(String property, String meaning) {
        this.property = property;
        this.meaning = meaning;
    }

    public String getProperty() {
        return property;
    }

    public String getMeaning() {
        return meaning;
    }
}
