package org.googlecode.userapi;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class VkontakteAPI {
    //todo: myId should be set manually in case of non-username/pass login
    public long myId;

    private AbstractHttpClient httpClient;
    //    private static final int SITE_ID = 4128;
    private int siteId;
    private static final String SESSION_EXPIRED = "{\"ok\":-1}";
    private static final String CAPTCHA_REQUIRED = "{\"ok\":-2}";
    private static final String FRIENDS_HIDDEN = "{\"ok\":-3}";

    private CaptchaHandler captchaHandler;

    private String login;
    private String pass;
    private static final String EMPTY_JSON_OBJECT = "{}";
    private static final JSONArray EMPTY_JSON_ARRAY = new JSONArray();

    /**
     * Used in workaround for userapi bug.
     */
    private int wasMessages = 0;

    private String remix;
    private String session;

    public String getRemix() {
        return remix;
    }

    public String getSession() {
        return session;
    }

    public void setCaptchaHandler(CaptchaHandler captchaHandler) {
        this.captchaHandler = captchaHandler;
    }

    private enum friendsTypes {
        friends, friends_mutual, friends_online, friends_new
    }

    public enum photosTypes {
        photos, photos_with, photos_new
    }

    public enum privateMessagesTypes {
        message, inbox, outbox
    }

    public AbstractHttpClient getHttpClient() {
        return httpClient;
    }

    @SuppressWarnings("unused")
	private VkontakteAPI() {
    }

    public VkontakteAPI(int siteId) {
        this.siteId = siteId;
        HttpParams params = new BasicHttpParams();
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        httpClient = new DefaultHttpClient(cm, params);
        httpClient.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
        HttpClientHelper.setAcceptAllCookies(httpClient);
        HttpClientHelper.addGzipCompression(httpClient);
    }

    /**
     * Login with credentials
     *
     * @param credentials
     * @throws IOException
     * @throws UserapiLoginException
     */
    public void login(Credentials credentials) throws IOException, UserapiLoginException {
        login = credentials.getLogin();
        pass = credentials.getPass();
        remix = credentials.getRemixpass();
        session = credentials.getSid();
        
        if (session != null) {
            String url = UrlBuilder.makeUrl("history");
            HttpGet httpGet = new HttpGet(url + "&sid=" + session);
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            String result;
            if (httpEntity != null) {
                result = EntityUtils.toString(httpEntity);
                httpEntity.consumeContent();
                if (result.equals(SESSION_EXPIRED)) {
                    System.out.println("login with session failed: session expired!");
                } else if (result.equals(CAPTCHA_REQUIRED)) {
                    System.out.println("login with session failed: captcha required! (should never happen)");
                } else {
                    System.out.println("login with session success!");
                    return;//session is ok
                }
            }
        }
        if (remix != null) {
            session = loginWithRemix(remix);
            if (session != null && UserapiLoginException.fromSid(session) == null) {
                System.out.println("login with remix success!");
                return;
            } else {
                System.out.println("login with remix failed!");
            }
        }
        session = loginWithPass(login, pass);
        checkSid(session);
        credentials.setSid(session);
        System.out.println("login with pass success!");
    }

    private void checkSid(String sid) throws UserapiLoginException {
        UserapiLoginException e = UserapiLoginException.fromSid(sid);
        if (e != null) throw e;
    }

    /**
     * Tries to login with login/password
     *
     * @param login - login
     * @param pass  - password
     * @return sid - session id or null in case of internal error
     * @throws IOException in case of connection problems
     */
    public String loginWithPass(String login, String pass) throws IOException {
        String urlString = UrlBuilder.makeLoginUrl(siteId, "force") + "&email=" + URLEncoder.encode(login, "UTF-8") + "&pass=" + URLEncoder.encode(pass, "UTF-8");
        HttpGet get = new HttpGet( Captcha.wrapCaptcha(urlString) );
        HttpResponse response = httpClient.execute(get);
        Header header = response.getFirstHeader("Location");
        String location = header != null ? header.getValue() : null;
        if (location == null || location.indexOf("0;sid=") == -1) return null;
        String sid = location.substring(location.indexOf("0;sid=") + "0;sid=".length());
        List<Cookie> cookies = httpClient.getCookieStore().getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equalsIgnoreCase("remixpassword")) remix = cookie.getValue();
            if (cookie.getName().equalsIgnoreCase("remixmid")) myId = Long.parseLong(cookie.getValue());
        }
        return sid;  
    }


    /**
     * Tries to login with remix password
     *
     * @param remixpass - remix password
     * @return sid - session id or null in case of internal error
     * @throws IOException in case of connection problems
     */
    private String loginWithRemix(String remixpass) throws IOException {
        String urlString = UrlBuilder.makeLoginUrl(siteId, "auto");
        HttpGet get = new HttpGet(urlString);
        get.addHeader("Cookie", "remixpassword=" + remixpass);
        HttpResponse response = httpClient.execute(get);
        Header header = response.getFirstHeader("Location");
        String location = header != null ? header.getValue() : null;
        if (location == null || location.indexOf("0;sid=") == -1) return null;
        else return location.substring(location.indexOf("0;sid=") + "0;sid=".length());
    }

    public void logout() throws IOException {
        String urlString = UrlBuilder.makeLoginUrl(siteId, "logout") + "&sid=" + session;
        HttpGet get = new HttpGet(urlString);
        httpClient.execute(get);
    }

    /**
     * Returns friend list for a user
     *
     * @param id   user ID
     * @param from no. of first entry required (zero-based)
     * @param to   no. of last entry required (plus one)
     * @param type - type of friends to return
     * @return friend list for a user
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     */
    private List<User> getFriends(long id, int from, int to, friendsTypes type) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl(type.name(), id, from, to);
        String jsonText = getTextFromUrl(url);
        return makeFriendsFromString(type, jsonText);
    }

    private List<User> getFriendsOrThrow(long id, int from, int to, friendsTypes type) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        String url = UrlBuilder.makeUrl(type.name(), id, from, to);
        String jsonText = getTextFromUrlOrThrow(url);
        return makeFriendsFromString(type, jsonText);
    }

    private List<User> makeFriendsFromString(friendsTypes type, String jsonText) throws JSONException {
        List<User> friends = new LinkedList<User>();
        if (!jsonText.equals(EMPTY_JSON_OBJECT)) {
            JSONArray fr = EMPTY_JSON_ARRAY;
            if (type == friendsTypes.friends_new) {
                JSONObject object = new JSONObject(jsonText);
                if (object.getInt("n") != 0) {
                    fr = object.getJSONArray("d");
                }
            } else {
                fr = new JSONArray(jsonText);
            }
            for (int i = 0; i < fr.length(); i++) {
                JSONArray userInfo = (JSONArray) fr.get(i);
                User user = new User(userInfo, this);
                if (type == friendsTypes.friends || type == friendsTypes.friends_online)
                    user.setFriend(true);
                if (type == friendsTypes.friends_new)
                    user.setNewFriend(true);
                friends.add(user);
            }
        }
        return friends;
    }

    private List<User> getFriendsOrThrow(long id, friendsTypes type) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        int current = 0;
        int fetchSize = 1024;
        List<User> friends = new LinkedList<User>();
        List<User> friendList;
        do {
            friendList = getFriendsOrThrow(id, current, current + fetchSize, type);
            friends.addAll(friendList);
            current += fetchSize;
        } while (friendList.size() == fetchSize);
        return friends;
    }

    private List<User> getFriends(long id, friendsTypes type) throws IOException, JSONException, UserapiLoginException {
        int current = 0;
        int fetchSize = 1024;
        List<User> friends = new LinkedList<User>();
        List<User> friendList;
        do {
            friendList = getFriends(id, current, current + fetchSize, type);
            friends.addAll(friendList);
            current += fetchSize;
        } while (friendList.size() == fetchSize);
        return friends;
    }

    /**
     * Returns friend list for a user
     *
     * @param userId user ID
     * @return List<User> friends
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     * @throws PageHiddenException    if friends page is hidden
     */
    public List<User> getFriends(long userId) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        return getFriendsOrThrow(userId, friendsTypes.friends);
    }

    /**
     * Returns online friend list for a user
     *
     * @param userId user ID
     * @return List<User> friends
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     * @throws PageHiddenException    if friends page is hidden
     */
    public List<User> getFriendsOnline(long userId) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        return getFriendsOrThrow(userId, friendsTypes.friends_online);
    }

    /**
     * Returns mutual friends list for a user
     *
     * @param userId user ID
     * @return List<User> friends
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     * @throws PageHiddenException    if friends page is hidden
     */
    public List<User> getFriendsMutual(long userId) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        return getFriendsOrThrow(userId, friendsTypes.friends_mutual);
    }

    public List<User> getMyFriends() throws IOException, JSONException, UserapiLoginException {
        return getFriends(myId, friendsTypes.friends);
    }

    public List<User> getMyFriendsOnline() throws IOException, JSONException, UserapiLoginException {
        return getFriends(myId, friendsTypes.friends_online);
    }

    public List<User> getMyNewFriends() throws IOException, JSONException, UserapiLoginException {
        return getFriends(myId, friendsTypes.friends_new);
    }

    /**
     * Add user to friend list/request addition
     *
     * @param userId friend ID
     * @return true is action succeded
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     */
    public boolean addToFriends(long userId) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("add_friend", userId);
        String result = getTextFromUrl(url);
        return true;//todo!
    }

    /**
     * Removes user from friend list/rejects reuest
     *
     * @param userId friend ID
     * @return true is action succeded
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException in case of problems with reply parsing
     */
    public boolean removeFromFriends(long userId) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("del_friend", userId);
        String result = getTextFromUrl(url);
        return true;//todo!
    }

    /**
     * Returns photo list for a user
     *
     * @param id   user id
     * @param from first entry no.
     * @param to   last entry no.
     * @param type - type of photos to return
     * @return the last element in this list
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     * @throws PageHiddenException    if photos page is hidden
     */
    public List<Photo> getPhotos(long id, int from, int to, photosTypes type) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        String url = UrlBuilder.makeUrl(type.name(), id, from, to);
        String jsonText = getTextFromUrlOrThrow(url);
        System.out.println(jsonText);
        List<Photo> photos = new LinkedList<Photo>();
        JSONArray photosJson;
        if (type == photosTypes.photos) {
            photosJson = new JSONArray(jsonText);
        } else {
            photosJson = new JSONObject(jsonText).getJSONArray("d");
        }
        for (int i = 0; i < photosJson.length(); i++) {
            JSONArray photoInfo = (JSONArray) photosJson.get(i);
            Photo photo = Photo.fromJson(photoInfo);
            if (photo != null)
                photos.add(photo);
        }
        return photos;
    }

    public List<Photo> getMyNewPhotos(int count) throws IOException, JSONException, UserapiLoginException {
        List<Photo> photos = new LinkedList<Photo>();
        String url = UrlBuilder.makeUrl(photosTypes.photos_new.name(), -1, 0, count);
        String jsonText = getTextFromUrl(url);
        JSONObject photosJson = new JSONObject(jsonText);
        if (!photosJson.getString("d").equals(EMPTY_JSON_OBJECT)) {
            JSONArray photosArray = photosJson.getJSONArray("d");
            for (int i = 0; i < photosArray.length(); i++) {
                JSONArray photoInfo = (JSONArray) photosArray.get(i);
                Photo photo = Photo.fromJson(photoInfo);
                if (photo != null)
                    photos.add(photo);
            }
        }
        return photos;
    }

    /**
     * Returns messages history objects based on JSON object or null if timestamp is wrong.
     *
     * @param historyObj object parsed from JSON response
     * @return list of messages history or null if timestamp is wrong
     * @throws JSONException in case if any JSON exceptions occur
     */
    public List<MessageHistory> getMessagesHistory(JSONObject historyObj) throws JSONException {
        String ifString = historyObj.getString("h");

        // Wrong timestamp. Unfortunately in Userapi timestamps are not real time stamps. Every new session has DIFFERENT
        // timestamps. So if we got "false" we need to get new timestamp.
        if ("false".equals(ifString))
            return null;

        List<MessageHistory> historyList = new LinkedList<MessageHistory>();

        if (!EMPTY_JSON_OBJECT.equals(ifString)) {
            JSONArray historyArray = historyObj.getJSONArray("h");

            for (int i = 0; i < historyArray.length(); i++) {
                JSONArray mhistory = (JSONArray) historyArray.get(i);
                historyList.add(new MessageHistory(mhistory, this));
            }
        }

        return historyList;
    }

    public List<MessageHistory> getPrivateMessagesHistory(long timestamp) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl(privateMessagesTypes.message.name(), myId, timestamp);
        String jsonText = getTextFromUrl(url);
        JSONObject historyJson = new JSONObject(jsonText);

        return getMessagesHistory(historyJson);
    }

    public List<Message> getPrivateMessages(long id, int from, int to, privateMessagesTypes type) throws IOException, JSONException, UserapiLoginException {
        List<Message> messages = new LinkedList<Message>();
        String url = UrlBuilder.makeUrl(type.name(), id, from, to);
        String jsonText = getTextFromUrl(url);
        JSONObject messagesJson = new JSONObject(jsonText);
//        Long count = messagesJson.getLong("n");
//        Long history = messagesJson.getLong("h");
        if (!messagesJson.getString("d").equals(EMPTY_JSON_OBJECT)) {
            JSONArray messagesArray = messagesJson.getJSONArray("d");
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONArray messageJson = (JSONArray) messagesArray.get(i);
                messages.add(new Message(messageJson, this));
            }
            //todo: total count
        }
        return messages;
    }

    public List<Message> getPrivateMessagesThread(long id, int from, int to) throws IOException, JSONException, UserapiLoginException {
        List<Message> messages = new LinkedList<Message>();
        String url = UrlBuilder.makeUrl(privateMessagesTypes.message.name(), id, from, to);
        String jsonText = getTextFromUrl(url);
        JSONObject messagesJson = new JSONObject(jsonText);
//        Long count = messagesJson.getLong("n");
//        Long history = messagesJson.getLong("h");
        if (!messagesJson.getString("d").equals(EMPTY_JSON_OBJECT)) {
            JSONArray messagesArray = messagesJson.getJSONArray("d");
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONArray messageJson = (JSONArray) messagesArray.get(i);
                messages.add(new Message(messageJson, this));
            }
            //todo: total count
        }
        return messages;
    }

    public MessagesStruct getPrivateMessagesStruct(long id, int from, int to, privateMessagesTypes type) throws IOException, JSONException, UserapiLoginException {
        List<Message> messages = new LinkedList<Message>();
        String url = UrlBuilder.makeUrl(type.name(), id, from, to);
        String jsonText = getTextFromUrl(url);
        JSONObject messagesJson = new JSONObject(jsonText);
        int count = messagesJson.getInt("n");
        long timestamp = messagesJson.getLong("h");
        if (!messagesJson.getString("d").equals(EMPTY_JSON_OBJECT)) {
            JSONArray messagesArray = messagesJson.getJSONArray("d");
            for (int i = 0; i < messagesArray.length(); i++) {
                JSONArray messageJson = (JSONArray) messagesArray.get(i);
                messages.add(new Message(messageJson, this));
            }
        }
        return new MessagesStruct(count, timestamp, messages);
    }

    public MessagesStruct getInboxMessagesStruct(int from, int to) throws IOException, JSONException, UserapiLoginException {
        return getPrivateMessagesStruct(myId, from, to, privateMessagesTypes.inbox);
    }

    public MessagesStruct getOutboxMessagesStruct(int from, int to) throws IOException, JSONException, UserapiLoginException {
        return getPrivateMessagesStruct(myId, from, to, privateMessagesTypes.outbox);
    }

    public List<Message> getInbox(int from, int to) throws IOException, JSONException, UserapiLoginException {
        return getPrivateMessages(myId, from, to, privateMessagesTypes.inbox);
    }

    public List<Message> getOutbox(int from, int to) throws IOException, JSONException, UserapiLoginException {
        return getPrivateMessages(myId, from, to, privateMessagesTypes.outbox);
    }

    /**
     * Getting wall message list for a user
     * @link http://userapi.com/?act=doc#wall
     *
     * @param id user id
     * @param from first entry
     * @param to last entry
     * 
     * @return wall message list
     * 
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     */
    public List<WallMessage> getWallMessages(long id, int from, int to) throws IOException, JSONException, PageHiddenException, UserapiLoginException {
        List<WallMessage> messages = new LinkedList<WallMessage>();
        String url = UrlBuilder.makeUrl("wall", id, from, to);
        String jsonText = getTextFromUrlOrThrow(url);
        JSONObject messagesJson = new JSONObject(jsonText);
        Long count = messagesJson.getLong("n");
        Long history = messagesJson.getLong("h");
        JSONArray messagesArray = messagesJson.getJSONArray("d");
        for (int i = 0; i < messagesArray.length(); i++) {
            JSONArray messageJson = (JSONArray) messagesArray.get(i);
            messages.add(new WallMessage(messageJson, this));
        }
        return messages;
    }

    /**
     * Mark message as read
     *
     * @param messageId - message id
     * @throws IOException
     */

    public void markAsRead(long messageId) throws IOException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("history");
        url += "&read=" + messageId;
        getTextFromUrl(url);
    }

    /**
     * Delete message
     *
     * @param userId    - user id
     * @param messageId - message id
     * @return true if message was successfully deleted and false otherwise
     * @throws IOException
     * @throws org.json.JSONException
     */

    public boolean deleteMessage(long userId, long messageId) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl();
        url += "&act=del_message" + "&id=" + myId + "&wid=" + userId + "_" + messageId;
        JSONObject jsonObject = new JSONObject(getTextFromUrl(url));
        return jsonObject.getInt("ok") == 1;
    }

    /**
     * This class send message to user
     *
     * @param sendingMessage - incapsulated request parameters
     * @return error code
     * @throws IOException
     */
    public String sendMessageToUser(Message sendingMessage)
            throws IOException, UserapiLoginException {
        if ((sendingMessage == null) || (sendingMessage.getText() == null)) {
            throw new DataException("Null message to send");
        }
        URL url = new URL("http://userapi.com/data?act=add_message" +
                "&id=" + sendingMessage.getReceiverId() +
                "&ts=" + sendingMessage.getDate().getTime() +
                "&message=" + URLEncoder.encode(sendingMessage.getText(), "UTF-8") + "&sid=" + session);

        return getTextFromUrl(url.toString());
    }

    /**
     * Returns new messages, new friends, new photos counters and history changes as ChagesHistory
     * Unlimited calls(captcha not required).
     *
     * @param timestamps timestamps for messages, photos, comments, etc.
     * @return new messages, new friends and new photos counters as ChagesHistory.
     * @throws java.io.IOException in case of connection problems.
     * @throws org.json.JSONException in case of JSON parsing problems.
     * @throws UserapiLoginException in case of login problems.
     */
    public ChangesHistory getChangesHistory(Timestamps timestamps) throws IOException, JSONException, UserapiLoginException {
        ChangesHistory history = new ChangesHistory();

        String url = UrlBuilder.makeUrl(timestamps);
        String jsonText = getTextFromUrl(url);
        JSONObject messagesJson = new JSONObject(jsonText);

        // Getting history changes
        if (messagesJson.has("message")) {
            JSONObject historyJson = messagesJson.getJSONObject("message");
            List<MessageHistory> historyList = getMessagesHistory(historyJson);
            history.setMessagesHistory(historyList);
        }

        // Counters
        int messagesCount = messagesJson.has("nm") ? messagesJson.getInt("nm") : 0;

        /*Workaround for bug in userapi: if user has several unread messages and he/she reads one of them, then next
          request to userapi will return zero unread messages, and second request will return correct number*/
        if (wasMessages > 1 && messagesCount == 0) {
            jsonText = getTextFromUrl(UrlBuilder.makeUrl("history"));
            messagesJson = new JSONObject(jsonText);
            messagesCount = messagesJson.has("nm") ? messagesJson.getInt("nm") : 0;
        }
        wasMessages = messagesCount;

        int friendsCount = messagesJson.has("nf") ? messagesJson.getInt("nf") : 0;
        int photosCount = messagesJson.has("nph") ? messagesJson.getInt("nph") : 0;

        history.setFriendsCount(friendsCount);
        history.setMessagesCount(messagesCount);
        history.setPhotosCount(photosCount);

        return history;
    }
    
    public ChangesHistory getChangesHistory() throws IOException, JSONException, UserapiLoginException {
        return getChangesHistory(null);
    }

    //todo!

    public String getLimits(String action) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("get_limits");
        return getTextFromUrl(url);
    }

    /**
     * Returns friends statuses updates
     * Unlimited calls(captcha not required)
     * should not be called for more than 150 items at time
     *
     * @param from first entry no.
     * @param to   last entry no.
     * @return new statuses
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     */
    public List<Status> getTimeline(int from, int to) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("updates_activity", from, to);
        List<Status> statuses = new LinkedList<Status>();
        JSONObject statusesJson = new JSONObject(getTextFromUrl(url));
        if (!statusesJson.getString("d").equals("null")) {
            JSONArray statusesArray = statusesJson.getJSONArray("d");
            for (int i = 0; i < statusesArray.length(); i++) {
                JSONArray messageJson = (JSONArray) statusesArray.get(i);
                statuses.add(Status.fromJson(messageJson));
            }
        }
        return statuses;
    }

    /**
     * Returns friends new friends updates
     * Unlimited calls(captcha not required)
     * should not be called for more than 150 items at time
     *
     * @param from first entry no.
     * @param to   last entry no.
     * @return new friends
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     */
    public List<User> getUpdatesFriends(int from, int to) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("updates_friends", from, to);
        List<User> friends = new LinkedList<User>();
        JSONObject statusesJson = new JSONObject(getTextFromUrl(url));
        if (!statusesJson.getString("d").equals("null")) {
            JSONArray statusesArray = statusesJson.getJSONArray("d");
            for (int i = 0; i < statusesArray.length(); i++) {
                JSONArray messageJson = (JSONArray) statusesArray.get(i);
                friends.add(new User(messageJson, this));
            }
        }
        return friends;
    }

    /**
     * Returns friends new photos
     * Unlimited calls(captcha not required)
     * should not be called for more than 150 items at time
     *
     * @param from first entry no.
     * @param to   last entry no.
     * @return new photos
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     */
    public List<Photo> getUpdatesPhotos(int from, int to) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("updates_photos", from, to);
        List<Photo> photos = new LinkedList<Photo>();
        JSONObject statusesJson = new JSONObject(getTextFromUrl(url));
        if (!statusesJson.getString("d").equals("null")) {
            JSONArray statusesArray = statusesJson.getJSONArray("d");
            for (int i = 0; i < statusesArray.length(); i++) {
                JSONArray photoInfo = (JSONArray) statusesArray.get(i);
                Photo photo = Photo.fromJson(photoInfo);
                if (photo != null)
                    photos.add(photo);
            }
        }
        return photos;
    }

    public List<Status> getStatusHistory(long id, int from, int to, long ts) throws IOException, JSONException, UserapiLoginException {
        List<Status> statuses = new LinkedList<Status>();
        String url = UrlBuilder.makeUrl("activity", id, from, to) + (ts == 0 ? "" : ("&ts=" + ts));
        JSONObject messagesJson = new JSONObject(getTextFromUrl(url));
        JSONArray messagesArray = messagesJson.getJSONArray("d");
        for (int i = 0; i < messagesArray.length(); i++) {
            JSONArray messageJson = (JSONArray) messagesArray.get(i);
            statuses.add(Status.fromJson(messageJson));
        }
        return statuses;
    }

    public List<Status> getStatusHistory(long id) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("activity", id, 0, 0);
        JSONObject messagesJson = new JSONObject(getTextFromUrl(url));
        int count = messagesJson.getInt("n");
        return getStatusHistory(id, 0, count, 0);
    }

    public List<Status> getMyStatusHistory() throws IOException, JSONException, UserapiLoginException {
        return getStatusHistory(myId);
    }

    private ProfileInfo getProfile(long id) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("profile", id);
        JSONObject jsonText = new JSONObject(getTextFromUrl(url));
        return new ProfileInfo(jsonText);
    }

    public ProfileInfo getProfileOrThrow(long id) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("profile", id);
        JSONObject jsonText = new JSONObject(getTextFromUrl(url));
        return new ProfileInfo(jsonText);
    }

    public ProfileInfo getMyProfile() throws IOException, JSONException, UserapiLoginException {
        return getProfile(myId);
    }

    /**
     * Updates current user status
     *
     * @param text - current status
     * @return true is update was successfull
     * @throws java.io.IOException    in case of connection problems
     * @throws org.json.JSONException
     */
    public boolean setStatus(String text) throws IOException, JSONException, UserapiLoginException {
        String url = UrlBuilder.makeUrl("set_activity") + "&text=" + URLEncoder.encode(text, "UTF-8");
        String result = getTextFromUrl(url);
        System.out.println(result);
        JSONObject o = new JSONObject(result);
        return o.getInt("ok") == 1;
    }

    public byte[] getFileFromUrl(String url) throws IOException {
        if (url == null) return new byte[]{};
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        byte[] result = null;
        if (httpEntity != null) {
            try {
                result = HttpClientHelper.httpEntityToByteArray(httpEntity);
            }
            catch (InterruptedIOException e) {
                httpGet.abort();
                throw e;
            }
            finally {
                httpEntity.consumeContent();
            }
        }
        return result;
    }

    private String getTextFromUrlOrThrow(String url) throws IOException, PageHiddenException, UserapiLoginException {
        HttpGet httpGet = new HttpGet(url + "&sid=" + session);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String result = null;
        if (httpEntity != null) {
            result = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
            if (result.equals(FRIENDS_HIDDEN)) {
                throw new PageHiddenException();
            } else if (result.equals(SESSION_EXPIRED)) {
                System.out.println("session expired!");
                login(new Credentials(login, pass, remix));
                return getTextFromUrl(url);
            } else if (result.equals(CAPTCHA_REQUIRED)) {
                System.out.println("captcha required!");
                return doWithCaptchaOrThrow(url);
            }
        }
        return result;
    }

    private String getTextFromUrl(String url) throws IOException, UserapiLoginException {
        HttpGet httpGet = new HttpGet(url + "&sid=" + session);
        HttpResponse response = httpClient.execute(httpGet);
        HttpEntity httpEntity = response.getEntity();
        String result = null;
        if (httpEntity != null) {
            result = EntityUtils.toString(httpEntity);
            httpEntity.consumeContent();
            if (result.equals(SESSION_EXPIRED)) {
                System.out.println("session expired!");
                login(new Credentials(login, pass, remix));
                return getTextFromUrl(url);
            } else if (result.equals(CAPTCHA_REQUIRED)) {
                System.out.println("captcha required!");
                return doWithCaptcha(url);
            }
        }
        return result;
    }

    private String doWithCaptcha(String url) throws IOException, UserapiLoginException {
        if (captchaHandler != null)
            return getTextFromUrl(prepareNewUrl(url));
        else {
            try {
                System.out.println("captcha required, sleeping for 3000 ms");
                Thread.sleep(3000);
            } catch (InterruptedException ignored) {
            }
            return getTextFromUrl(url);
        }
    }

    private String doWithCaptchaOrThrow(String url) throws IOException, PageHiddenException, UserapiLoginException {
        String newUrl = prepareNewUrl(url);
        return getTextFromUrlOrThrow(newUrl);
    }

    private String prepareNewUrl(String url) {
        if (captchaHandler == null) throw new IllegalStateException("captcha handler must be set!");
        String captcha_sid = String.valueOf(Math.abs(new Random().nextLong()));
        String captcha_url = UrlBuilder.makeUrl("captcha") + "&csid=" + captcha_sid;
        String captcha_code = captchaHandler.handleCaptcha(captcha_url);
        return url + "&fcsid=" + captcha_sid + "&fccode=" + captcha_code;
    }
}