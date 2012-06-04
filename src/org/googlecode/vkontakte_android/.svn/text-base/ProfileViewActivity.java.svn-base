package org.googlecode.vkontakte_android;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.RemoteException;
import android.provider.Contacts;
import android.util.Log;
import android.view.*;
import android.widget.*;
import org.googlecode.vkontakte_android.AutoLoadList.Loader;
import org.googlecode.vkontakte_android.database.ProfileDao;
import org.googlecode.vkontakte_android.database.UserDao;
import org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper;
import org.googlecode.vkontakte_android.service.CheckingService;
import org.googlecode.vkontakte_android.utils.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_STATUS_DATE;
import static org.googlecode.vkontakte_android.provider.UserapiDatabaseHelper.KEY_STATUS_USERID;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.PROFILES_URI;
import static org.googlecode.vkontakte_android.provider.UserapiProvider.STATUSES_URI;

public class ProfileViewActivity extends Activity implements TabHost.TabContentFactory {

    private static final String TAG = "org.googlecode.vkontakte_android.ProfileViewActivity";
    private long profileId;
    private ProfileDao profileDao;
    private Menu menuToRefresh; //menu is disabled until we haven't friend data
    private static final int SEX_FEMALE = 1;

    private static final String INFO_TAB = "info_tab";
    private static final String UPDATES_TAB = "updates_tab";
    private static final String PHOTOS_TAB = "photos_tab";
    private static final String WALL_TAB = "wall_tab";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.profile_view);

        initTabHost();
        initInfoAndPhotosTab();
        initWallTab();
        initUpdatesTab();
    }

    private void initTabHost() {
        final TabHost tabHost = (TabHost) findViewById(R.id.ProfileTabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec(INFO_TAB)
                .setIndicator(getString(R.string.info))
                .setContent(this));
//        tabHost.addTab(tabHost.newTabSpec(WALL_TAB)
//                .setIndicator("Wall")
//                .setContent(this));
        tabHost.addTab(tabHost.newTabSpec(UPDATES_TAB)
                .setIndicator(getString(R.string.updates))
                .setContent(this));
//        tabHost.addTab(tabHost.newTabSpec(PHOTOS_TAB)
//                .setIndicator("Photos")
//                .setContent(this));

    }


    private void initInfoAndPhotosTab() {
        profileId = PreferenceHelper.getMyId(this);
        if (getIntent().getExtras() != null)
            profileId = getIntent().getExtras().getLong(UserapiDatabaseHelper.KEY_PROFILE_USERID, profileId);

        new AsyncTask<Long, Object, ProfileDao>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                setProgressBarIndeterminateVisibility(true);
            }

            @Override
            protected void onPostExecute(ProfileDao result) {
                setProgressBarIndeterminateVisibility(false);
                if (result != null) showProfileInfo(result);
            }

            @Override
            protected ProfileDao doInBackground(Long... id) {
                try {
                    ServiceHelper.getService().loadProfile(id[0]);
                } catch (RemoteException e) {
                    Log.e(TAG, "Cannot load profile");
                    e.printStackTrace();
                    return null;
                }
                Cursor cursor = managedQuery(PROFILES_URI, null,
                        UserapiDatabaseHelper.KEY_PROFILE_USERID + "=?",
                        new String[]{String.valueOf(id[0])}, null);

                cursor.moveToFirst();
                return new ProfileDao(cursor);

            }
        }.execute(profileId);

    }

    private void initWallTab() {
    }

    private void initUpdatesTab() {
        Cursor statusesCursor = managedQuery(STATUSES_URI, null, KEY_STATUS_USERID + "=" + profileId, null, KEY_STATUS_DATE + " DESC ");
        if (statusesCursor != null && statusesCursor.getCount() < 2) {
            new AsyncTask<Long, Object, Boolean>() {

                @Override
                protected Boolean doInBackground(Long... params) {
                    try {
                        ServiceHelper.getService().loadStatusesByUser(0, CheckingService.STATUS_NUM_LOAD, profileId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

            }.execute(new Long[]{profileId});
        }


    }

    private void showProfileInfo(ProfileDao profile) {
        ArrayList<PropertiesHolder> DATA = new ArrayList<PropertiesHolder>();

        profileDao = profile;
        AvatarLoader avatarLoader = new AvatarLoader(this);
        AvatarLoader.AvatarInfo info = new AvatarLoader.AvatarInfo();
        info.view = (ImageView) findViewById(R.id.photo);
        info.userId = profileDao.id;
        info.type = AvatarLoader.AvatarInfo.AvatarType.PROFILE;
        info.avatarUrl = profileDao.photo;
        avatarLoader.applyAvatarImmediately(info);

        if (profileDao.getStatus() != null) {
            TextView status = ((TextView) findViewById(R.id.status));
            status.setText(profileDao.getStatus());
            status.setVisibility(View.VISIBLE);
        }


        TextView userName = ((TextView) findViewById(R.id.firstname_lastname));
        userName.setText(profileDao.getFirstname() + " " + profileDao.getSurname());


        if (profileDao.birthday != null && profileDao.birthday != 0) {
            SimpleDateFormat format = new SimpleDateFormat("d MMM yyyy");
            DATA.add(new PropertiesHolder(getString(R.string.info_birthday), format.format(new Date(profileDao.birthday))));
        }
        if (profileDao.sex != 0) {
            DATA.add(new PropertiesHolder(getString(R.string.info_sex), getString(profileDao.sex == SEX_FEMALE ? R.string.sex_female : R.string.sex_male)));
        }
        if (profileDao.phone != null) {
            DATA.add(new PropertiesHolder(getString(R.string.info_phone), profileDao.phone));
        }
        if (profileDao.politicalViews != 0) {
            int id = ProfileInfoHelper.getPoliticalViewId(profileDao.politicalViews);
            String politicalViews = getString(R.string.info_views);
            if (id != -1) {
                DATA.add(new PropertiesHolder(politicalViews, getString(id)));
            } else {
                DATA.add(new PropertiesHolder(politicalViews, ""));
            }
        }
        if (profileDao.familyStatus != 0) {
            int id = ProfileInfoHelper.getFamilyStatusId(profileDao.familyStatus, profileDao.sex);
            String status = getString(R.string.info_status);
            if (id != -1) {
                DATA.add(new PropertiesHolder(status, getString(id)));
            } else {
                DATA.add(new PropertiesHolder(status, ""));
            }
        }
        if (profileDao.currentCity != null) {
            DATA.add(new PropertiesHolder(getString(R.string.info_city), profileDao.currentCity));
        }

        ListView infoListView = (ListView) findViewById(R.id.profile_info_list);
        infoListView.setAdapter(new ProfileInfoAdapter(this, DATA));
        final TabHost tabHost = (TabHost) findViewById(R.id.ProfileTabHost);
//        ListView photoListView = (ListView) findViewById(R.id.profile_photo_tab_list);
//        photoListView.setAdapter(new ProfileInfoAdapter(this, DATA));
        refreshMenu();  //TODO: avoid unnecessary calls
    }

    private void refreshMenu() {
        if (menuToRefresh != null) {
            onPrepareOptionsMenu(menuToRefresh);
        }
    }


    private void addOrEditContact() {
        //TODO add mail and other
        Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
        intent.setType("vnd.android.cursor.item/person");
        if (profileDao.phone != null) {
            intent.putExtra(Contacts.Intents.Insert.PHONE, Phone.formatPhoneNumber(profileDao.phone));
        }
        intent.putExtra(Contacts.Intents.Insert.NAME, profileDao.getFirstname() + " " + profileDao.getSurname());
        startActivity(intent);
    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.friend_context_menu, menu);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        UserDao user = UserDao.get(this, info.id);
        if (user.isNewFriend()) {
            menu.removeItem(R.id.remove_from_friends);
        } else {
            menu.removeItem(R.id.add_to_friends);
        }
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        long rowId = info.id;
        switch (item.getItemId()) {
            case R.id.view_profile:
                UserHelper.viewProfile(this, rowId);
                return true;
            case R.id.remove_from_friends:
                //todo!
                Toast.makeText(this, "not implemented yet", Toast.LENGTH_LONG).show();
                return true;
            case R.id.add_to_friends:
                //todo!
                Toast.makeText(this, "not implemented yet", Toast.LENGTH_LONG).show();
                return true;
            case R.id.send_message:
                UserHelper.sendMessage(this, rowId);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public View createTabContent(String tag) {
        System.out.println("tag = " + tag);
        View tv = new View(this);

        if (tag.equals(INFO_TAB)) {
            tv = getLayoutInflater().inflate(R.layout.profile_info, null);
        } else if (tag.equals(PHOTOS_TAB)) {
            tv = getLayoutInflater().inflate(R.layout.profile_photo_tab, null);
        } else if (tag.equals(UPDATES_TAB)) {

            final AutoLoadList arl = new AutoLoadList(this);
            Cursor statusesCursor = managedQuery(STATUSES_URI, null, KEY_STATUS_USERID + "=" + profileId, null, KEY_STATUS_DATE + " DESC ");
            arl.setAdapter(new UpdatesListAdapter(this, R.layout.status_row_profile, statusesCursor));
            arl.setLoader(new Loader() {
                @Override
                public Boolean load() {
                    try {
                        ServiceHelper.getService().loadStatusesByUser(arl.getAdapter().getCount(),
                                arl.getAdapter().getCount() + CheckingService.STATUS_NUM_LOAD, profileId);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            });
            return arl;
        }
        return tv;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isMyProfile()) return false;
        MenuInflater inflater = getMenuInflater();
        menuToRefresh = menu;
        inflater.inflate(R.menu.friend_profile_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private boolean isMyProfile() {
        return profileId == PreferenceHelper.getMyId(this);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (profileDao == null) {
            menu.setGroupEnabled(0, false); // can't add friend as contact without data
        } else {
            menu.setGroupEnabled(0, true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_to_contacts:
                addOrEditContact();
                return true;
            default:
                return true;
        }
    }

}