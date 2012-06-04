package org.googlecode.userapi;

/**
 * The timestamps values for messages, photos, etc.
 *
 * @author Ayzen
 */
public class Timestamps {

    private long wallTs = -1;
    private long activitiesTs = -1;
    private long messagesTs = -1;
    private long photosUpdatesTs = -1;
    private long tagedPhotosUpdatesTs = -1;
    private long friendsTs = -1;
    private long activitiesUpdatesTs = -1;
    private long photosCommentsTs = -1;

    public Timestamps() {
    }

    public long getWallTs() {
        return wallTs;
    }

    public void setWallTs(long wallTs) {
        this.wallTs = wallTs;
    }

    public long getActivitiesTs() {
        return activitiesTs;
    }

    public void setActivitiesTs(long activitiesTs) {
        this.activitiesTs = activitiesTs;
    }

    public long getMessagesTs() {
        return messagesTs;
    }

    public void setMessagesTs(long messagesTs) {
        this.messagesTs = messagesTs;
    }

    public long getPhotosUpdatesTs() {
        return photosUpdatesTs;
    }

    public void setPhotosUpdatesTs(long photosUpdatesTs) {
        this.photosUpdatesTs = photosUpdatesTs;
    }

    public long getTagedPhotosUpdatesTs() {
        return tagedPhotosUpdatesTs;
    }

    public void setTagedPhotosUpdatesTs(long tagedPhotosUpdatesTs) {
        this.tagedPhotosUpdatesTs = tagedPhotosUpdatesTs;
    }

    public long getFriendsTs() {
        return friendsTs;
    }

    public void setFriendsTs(long friendsTs) {
        this.friendsTs = friendsTs;
    }

    public long getActivitiesUpdatesTs() {
        return activitiesUpdatesTs;
    }

    public void setActivitiesUpdatesTs(long activitiesUpdatesTs) {
        this.activitiesUpdatesTs = activitiesUpdatesTs;
    }

    public long getPhotosCommentsTs() {
        return photosCommentsTs;
    }

    public void setPhotosCommentsTs(long photosCommentsTs) {
        this.photosCommentsTs = photosCommentsTs;
    }

}
