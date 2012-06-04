package org.googlecode.vkontakte_android.matching;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Contacts;
import android.util.Log;
import org.googlecode.vkontakte_android.utils.ContactsHelper;

import java.util.HashSet;
import java.util.Set;

public class ContactsMatcher {
    private final ContentResolver contentResolver;

    public ContactsMatcher(Context context) {
        contentResolver = context.getContentResolver();
    }

    public Uri findPersonByPhones(String... phones) {

        String[] filtered = new String[phones.length];

        StringBuilder sb = new StringBuilder();
        for (int i = 0, j = 0, phonesLength = phones.length; i < phonesLength; i++) {

            String ph = phones[i].replaceAll("[^\\d\\+]+", "");
            if (ph.length() < 6) continue;

            if (ph.startsWith("8")) {
                ph = "+7" + ph.substring(1);
            }

            filtered[j++] = ph;
            if (j != 0) sb.append(" or ");
            sb.append(Contacts.PhonesColumns.NUMBER_KEY + "=?");
        }

        final Cursor q = contentResolver.query(Contacts.Phones.CONTENT_URI,
                new String[]{Contacts.Phones.PERSON_ID},
                sb.toString(),
                filtered, null);
        return ContactsHelper.createPersonUri(getUniqueRecord(q, Contacts.Phones.PERSON_ID));
    }

    public Uri findPersonByName(String name) {

        final Cursor q = contentResolver.query(Contacts.People.CONTENT_URI,
                new String[]{BaseColumns._ID},
                Contacts.PeopleColumns.NAME + "=? or " + Contacts.PeopleColumns.DISPLAY_NAME + "=?",
                new String[]{name, name},
                null);

        return ContactsHelper.createPersonUri(getUniqueRecord(q, Contacts.Phones.PERSON_ID));
    }

    private static String getUniqueRecord(Cursor q, String column) {
        try {
            if (!q.moveToFirst()) return null;
            final int idColumn = q.getColumnIndexOrThrow(column);

            final int cnt = q.getCount();
            if (cnt != 1) {
                return q.getString(idColumn);
            }

            String lastId;
            Set<String> persons = new HashSet<String>(cnt);  // слишком сложно
            do {
                lastId = q.getString(idColumn);
                persons.add(lastId);
            } while (q.moveToNext());

            if (persons.size() > 1) {
                Log.w("VK", "Several contacts found with unique filter: " + persons);
                return null;
            }

            return lastId;
        } finally {
            q.close();
        }
    }

}
