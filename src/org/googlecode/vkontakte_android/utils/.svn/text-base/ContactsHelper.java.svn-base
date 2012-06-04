package org.googlecode.vkontakte_android.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.Contacts;

public class ContactsHelper {

    private final ContentResolver contentResolver;

    public ContactsHelper(Context context) {
        contentResolver = context.getContentResolver();
    }

    public String getName(long vkId) {
        final Uri id = findPersonByVkId(vkId);
        // toDo оптимизировать
        final Cursor q = contentResolver.query(id, null, null, null, null);
        try {

            if (!q.moveToFirst()) return null;
            return q.getString(q.getColumnIndexOrThrow(Contacts.PeopleColumns.NAME));

        } finally {
            q.close();
        }
    }

    /**
     * @param vkId из контакта
     * @return null или Uri человека в контактах
     */
    public Uri findPersonByVkId(long vkId) {

        final Cursor q = contentResolver.query(
                Contacts.ContactMethods.CONTENT_URI, new String[]{BaseColumns._ID},
                Contacts.ContactMethodsColumns.AUX_DATA + "=? and " +
                        Contacts.ContactMethodsColumns.DATA + "=" + vkId + " and " +
                        Contacts.ContactMethodsColumns.KIND + "=" + Contacts.KIND_IM,
                new String[]{"custom:vKontakte"}, null
        );
        try {

            if (!q.moveToFirst()) return null;
            return createPersonUri(q.getString(q.getColumnIndexOrThrow(BaseColumns._ID)));

        } finally {
            q.close();
        }
    }

    public static Uri createPersonUri(String contactId) {
        return Contacts.People.CONTENT_FILTER_URI.buildUpon().appendPath(contactId).build();
    }

    /**
     * Вычищает vkId из всех контактов в локлаьной БД
     *
     * @return количество удалённых записей
     */
    public int deleteEverything() {
        return contentResolver.delete(Contacts.ContactMethods.CONTENT_URI,
                Contacts.ContactMethodsColumns.AUX_DATA + "=? and "
                        + Contacts.ContactMethodsColumns.KIND + "=" + Contacts.KIND_IM,
                new String[]{"custom:vKontakte"}
        );
    }

    /**
     * Добавляет человеку vkId и добавляет контакт в группу
     *
     * @param person запись из Contacts
     * @param vkId   id
     * @return ContactMethods uri
     */
    public Uri bindPersonWithVkontakteId(Uri person, long vkId) {
        final ContentValues vk = new ContentValues();
        final int personId = Integer.parseInt(person.getLastPathSegment());
        vk.put(Contacts.ContactMethods.PERSON_ID, personId);
        vk.put(Contacts.ContactMethodsColumns.KIND, Contacts.KIND_IM);
        vk.put(Contacts.ContactMethodsColumns.AUX_DATA, "custom:vKontakte");
        vk.put(Contacts.ContactMethodsColumns.TYPE, Contacts.ContactMethods.TYPE_HOME);

        vk.put(Contacts.ContactMethodsColumns.DATA, "id777");
        final ContentResolver contentResolver = this.contentResolver;
        Uri lastInserted = contentResolver.insert(Contacts.ContactMethods.CONTENT_URI, vk);

//        int groupId = findOrCreateGroup(getString(R.string.group_name), getString(R.string.group_notes));
        int groupId = findOrCreateGroup("vKontakte", "Description");
        Contacts.People.addToGroup(contentResolver, personId, groupId);
        return lastInserted;
    }

    private int findOrCreateGroup(String name, String notes) {

        final ContentResolver cr = contentResolver;
        final Cursor q = cr.query(
                Contacts.Groups.CONTENT_URI,
                new String[]{BaseColumns._ID, Contacts.GroupsColumns.NAME},
                Contacts.GroupsColumns.NAME + "=?",
                new String[]{name},
                null
        );

        try {
            if (!q.moveToFirst()) {
                final ContentValues vk = new ContentValues();
                vk.put(Contacts.GroupsColumns.NAME, name);
                vk.put(Contacts.GroupsColumns.NOTES, notes);
                vk.put(Contacts.GroupsColumns.SHOULD_SYNC, "false");
                return Integer.parseInt(cr.insert(Contacts.Groups.CONTENT_URI, vk).getLastPathSegment());
            }

            return q.getInt(q.getColumnIndexOrThrow(BaseColumns._ID));

        } finally {
            q.close();
        }

    }
}
