package org.googlecode.vkontakte_android.service;

interface IVkontakteService {
  void update(int what, boolean synchronous);
  
  void sendMessage(String mess, long id);
  void sendStatus(String status);
  
  void login(String login, String pass, String remix, String sid);
  void loginAuth();
  void logout();
  
  void loadPrivateMessages(int type, int first, int last);
  void loadStatuses(int start, int end);
  void loadStatusesByUser(int start, int end, long id);
  
  void loadUsersPhotos(in List<String> l);  
  void loadAllUsersPhotos(); 
  
  //load user's profile to the cache and return if successful
  void loadProfile(long userid);
  void loadMyProfile();
}