package org.googlecode.userapi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ProfileInfo {
    protected long id;
    protected String firstname;
    protected String surname;
    protected String maidenName;
    protected Status status;
    protected String photo;
    protected int sex;
    protected Date birthday;
    protected String phone;
    protected int politicalViews;
    protected int familyStatus;
    protected String currentCity;

    public ProfileInfo(ProfileInfo p) {
        id = p.getId();
        firstname = p.getFirstname();
        surname = p.getSurname();
        maidenName = p.getMaidenName();
        status = p.getStatus();
        photo = p.getPhoto();
        sex = p.getSex();
        birthday = p.getBirthday();
        phone = p.getPhone();
        politicalViews = p.getPoliticalViews();
        familyStatus = p.getFamilyStatus();
        currentCity = p.currentCity;
    }

    public ProfileInfo() {

    }

    public ProfileInfo(JSONObject profileJson) throws JSONException {
        //id
        id = profileJson.getLong("id");
        //name
        firstname = profileJson.getString("fn");
        surname = profileJson.getString("ln");
        //maiden name
        maidenName = profileJson.getString("mn");
        JSONObject element = profileJson.getJSONObject("actv");
        //status
        status = Status.fromJson(JSONHelper.objectToArray(element));
        //returns strange data if there is no avatar
        photo = profileJson.getString("bp").contains("http") ? profileJson.getString("bp") : User.STUB_URL;
        sex = profileJson.getInt("sx");
        //birth day
        int bd = profileJson.getInt("bd");
        int bm = profileJson.getInt("bm");
        int by = profileJson.getInt("by");
        if (bd != 0 && bm != 0 && by != 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(by, bm - 1, bd, 0, 0, 0);
            birthday = calendar.getTime();
        }
        //mobile phone
        if (profileJson.has("mo"))
            phone = profileJson.getString("mo");
        //political views
        politicalViews = profileJson.getInt("pv");
        //family status
        familyStatus = profileJson.getInt("fs");
        //current city
        currentCity = profileJson.getJSONObject("ht").getString("cin");

//        JSONObject photosJSON = profileJson.getJSONObject("ph");
//        int photosCount = photosJSON.getInt("n");
//        System.out.println("photosCount = " + photosCount);
//        JSONArray photos = photosJSON.getJSONArray("d");
//        System.out.println("photos.length() = " + photos.length()); //usually <=39       
    }

    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public String getMaidenName() {
        return maidenName;
    }

    public Status getStatus() {
        return status;
    }

    public String getPhoto() {
        return photo;
    }

    public int getSex() {
        return sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public int getPoliticalViews() {
        return politicalViews;
    }

    public int getFamilyStatus() {
        return familyStatus;
    }

    public String getCurrentCity() {
        return currentCity;
    }

    @Override
    public String toString() {
        return "ProfileInfo{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", surname='" + surname + '\'' +
                ", maidenName='" + maidenName + '\'' +
                ", status=" + status +
                ", photo='" + photo + '\'' +
                ", sex=" + sex +
                ", birthday=" + birthday +
                ", phone='" + phone + '\'' +
                ", politicalViews='" + politicalViews + '\'' +
                ", familyStatus='" + familyStatus + '\'' +
                ", currentCity='" + currentCity + '\'' +
                '}';
    }
}
