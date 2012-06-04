/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/mast/workspaces/weightlosetogether/LWTv2_1/src/org/googlecode/vkontakte_android/service/IVkontakteService.aidl
 */
package org.googlecode.vkontakte_android.service;
public interface IVkontakteService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements org.googlecode.vkontakte_android.service.IVkontakteService
{
private static final java.lang.String DESCRIPTOR = "org.googlecode.vkontakte_android.service.IVkontakteService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an org.googlecode.vkontakte_android.service.IVkontakteService interface,
 * generating a proxy if needed.
 */
public static org.googlecode.vkontakte_android.service.IVkontakteService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = (android.os.IInterface)obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof org.googlecode.vkontakte_android.service.IVkontakteService))) {
return ((org.googlecode.vkontakte_android.service.IVkontakteService)iin);
}
return new org.googlecode.vkontakte_android.service.IVkontakteService.Stub.Proxy(obj);
}
public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_update:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
boolean _arg1;
_arg1 = (0!=data.readInt());
this.update(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
long _arg1;
_arg1 = data.readLong();
this.sendMessage(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_sendStatus:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendStatus(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
java.lang.String _arg2;
_arg2 = data.readString();
java.lang.String _arg3;
_arg3 = data.readString();
this.login(_arg0, _arg1, _arg2, _arg3);
reply.writeNoException();
return true;
}
case TRANSACTION_loginAuth:
{
data.enforceInterface(DESCRIPTOR);
this.loginAuth();
reply.writeNoException();
return true;
}
case TRANSACTION_logout:
{
data.enforceInterface(DESCRIPTOR);
this.logout();
reply.writeNoException();
return true;
}
case TRANSACTION_loadPrivateMessages:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
int _arg2;
_arg2 = data.readInt();
this.loadPrivateMessages(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_loadStatuses:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
this.loadStatuses(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_loadStatusesByUser:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
int _arg1;
_arg1 = data.readInt();
long _arg2;
_arg2 = data.readLong();
this.loadStatusesByUser(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_loadUsersPhotos:
{
data.enforceInterface(DESCRIPTOR);
java.util.List<java.lang.String> _arg0;
_arg0 = data.createStringArrayList();
this.loadUsersPhotos(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_loadAllUsersPhotos:
{
data.enforceInterface(DESCRIPTOR);
this.loadAllUsersPhotos();
reply.writeNoException();
return true;
}
case TRANSACTION_loadProfile:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.loadProfile(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_loadMyProfile:
{
data.enforceInterface(DESCRIPTOR);
this.loadMyProfile();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements org.googlecode.vkontakte_android.service.IVkontakteService
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
public void update(int what, boolean synchronous) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(what);
_data.writeInt(((synchronous)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_update, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void sendMessage(java.lang.String mess, long id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(mess);
_data.writeLong(id);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void sendStatus(java.lang.String status) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(status);
mRemote.transact(Stub.TRANSACTION_sendStatus, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void login(java.lang.String login, java.lang.String pass, java.lang.String remix, java.lang.String sid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(login);
_data.writeString(pass);
_data.writeString(remix);
_data.writeString(sid);
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loginAuth() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loginAuth, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void logout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_logout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadPrivateMessages(int type, int first, int last) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeInt(first);
_data.writeInt(last);
mRemote.transact(Stub.TRANSACTION_loadPrivateMessages, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadStatuses(int start, int end) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(start);
_data.writeInt(end);
mRemote.transact(Stub.TRANSACTION_loadStatuses, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadStatusesByUser(int start, int end, long id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(start);
_data.writeInt(end);
_data.writeLong(id);
mRemote.transact(Stub.TRANSACTION_loadStatusesByUser, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadUsersPhotos(java.util.List<java.lang.String> l) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStringList(l);
mRemote.transact(Stub.TRANSACTION_loadUsersPhotos, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadAllUsersPhotos() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loadAllUsersPhotos, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
//load user's profile to the cache and return if successful

public void loadProfile(long userid) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(userid);
mRemote.transact(Stub.TRANSACTION_loadProfile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
public void loadMyProfile() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_loadMyProfile, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_update = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_sendStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_loginAuth = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_logout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_loadPrivateMessages = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_loadStatuses = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_loadStatusesByUser = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_loadUsersPhotos = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_loadAllUsersPhotos = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_loadProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_loadMyProfile = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
}
public void update(int what, boolean synchronous) throws android.os.RemoteException;
public void sendMessage(java.lang.String mess, long id) throws android.os.RemoteException;
public void sendStatus(java.lang.String status) throws android.os.RemoteException;
public void login(java.lang.String login, java.lang.String pass, java.lang.String remix, java.lang.String sid) throws android.os.RemoteException;
public void loginAuth() throws android.os.RemoteException;
public void logout() throws android.os.RemoteException;
public void loadPrivateMessages(int type, int first, int last) throws android.os.RemoteException;
public void loadStatuses(int start, int end) throws android.os.RemoteException;
public void loadStatusesByUser(int start, int end, long id) throws android.os.RemoteException;
public void loadUsersPhotos(java.util.List<java.lang.String> l) throws android.os.RemoteException;
public void loadAllUsersPhotos() throws android.os.RemoteException;
//load user's profile to the cache and return if successful

public void loadProfile(long userid) throws android.os.RemoteException;
public void loadMyProfile() throws android.os.RemoteException;
}
