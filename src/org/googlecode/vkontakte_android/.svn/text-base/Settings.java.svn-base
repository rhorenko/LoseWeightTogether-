package org.googlecode.vkontakte_android;

import android.content.Intent;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import org.googlecode.vkontakte_android.utils.AppHelper;
import org.googlecode.vkontakte_android.utils.PreferenceHelper;

public class Settings extends PreferenceActivity implements Preference.OnPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        PreferenceScreen scr = getPreferenceScreen();
        
        Preference ps = scr.findPreference("sound");
        ps.setOnPreferenceChangeListener(this);

        Preference pn = scr.findPreference("notif");
        pn.setOnPreferenceChangeListener(this);

        ListPreference list = (ListPreference)scr.findPreference(PreferenceHelper.SYNC_PERIOD);
        list.setOnPreferenceChangeListener(this);
        list.setSummary(list.getEntry());
    }

    @Override
    public boolean onPreferenceChange(Preference arg0, Object arg1) {
        String key = arg0.getKey();

        if (key.equals(PreferenceHelper.SYNC_PERIOD)) {
            String value = (String) arg1;
            ListPreference pr = ((ListPreference) arg0);
            int pos = pr.findIndexOfValue(value);
            pr.setSummary(pr.getEntries()[pos]);

            int period = Integer.parseInt(value);
            startService(new Intent(AppHelper.ACTION_SET_AUTOUPDATE).putExtra(AppHelper.EXTRA_AUTOUPDATE_PERIOD, period));
        }
        return true;
    }
    
}
