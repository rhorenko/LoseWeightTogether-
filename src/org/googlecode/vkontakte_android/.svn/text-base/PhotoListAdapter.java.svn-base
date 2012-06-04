package org.googlecode.vkontakte_android;

//import android.app.Activity;
import android.content.Context;
import android.util.Log;
//import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import org.googlecode.userapi.PageHiddenException;
import org.googlecode.userapi.Photo;
import org.googlecode.userapi.UserapiLoginException;
import org.googlecode.userapi.VkontakteAPI;
import org.json.JSONException;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class PhotoListAdapter extends BaseAdapter {

    private List<Photo> photos = new LinkedList<Photo>();
    private Context context;
    //private boolean loading = false;

    public int getCount() {
        return photos.size();
    }

    public Object getItem(int pos) {
        return pos;
    }

    public long getItemId(int pos) {
        return pos;
    }

    public PhotoListAdapter(Context context, int layout, VkontakteAPI api) {
        this.context = context;
//        int layout1 = layout;
  //      LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        try {
            try {
                try {
                    photos = api.getPhotos(api.myId, 0, 10, VkontakteAPI.photosTypes.photos);
                } catch (UserapiLoginException e) {
                    e.printStackTrace();
                }
            } catch (PageHiddenException e) {
                e.printStackTrace(); //todo!
            }
            Log.w("photos:", photos.size() + "");
            notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public View getView(int pos, View v, ViewGroup p) {
        //TODO ? na figa
        ImageView view = new ImageView(context);
//        byte[] image = new byte[0];
//        try {
//            image = photos.get(pos).getImageUrl();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        view.setImageBitmap(BitmapFactory.decodeByteArray(image, 0, image.length));
        return view;
    }


    public void prepareData() {
//        this.count += 1;
//        loading = true;
//        this.notifyDataSetChanged();
    }
}