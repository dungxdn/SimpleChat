package jp.bap.traning.simplechat.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.service.ChatService;

import static jp.bap.traning.simplechat.utils.SharedPrefs.KEY_SAVE_ID;

/**
 * Created by dungpv on 6/13/18.
 */

public class Common {
    public static final String URL_SERVER = "http://18.216.126.225:3000";
    public static final String ACTION_SOCKET_EVENT = "action.socket.event";
    public static final int REQUEST_LOGIN = 100;
    public static final int STATUS_SUCCESS = 200;
    public static final int REQUEST_LOGOUT_VALUE = 101;
    public static final String REQUEST_LOGOUT_KEY = "KEY_LOGOUT";
    private static final String TAG = "Common";
    public static final String typeText = "text";
    public static final String typeImage = "image";
    public static final String typeLink = "link";
    public static final int mMineId = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID, Integer.class);

    public static void connectToServerSocket(Context context, String host, int token) {
        if (ChatService.getChat() == null) {
            Intent i = new Intent(context, ChatService.class);
            i.putExtra("host", host);
            i.putExtra("token", token);
            context.startService(i);
        } else {
            Log.d(TAG, "connectToServerSocket: Service started ");
        }
    }

    public static Room getRoomWithUser(int userId) {
        return new RoomDAO().getRoomWithUser(userId);
    }

    public static Room getFullRoomFromRoomId(int roomId) {
        Room room = new RoomDAO().getRoomFromRoomId(roomId);
        if (room != null) {
            if (room.getType() == 0) {
                for (User user : room.getUsers()) {
                    if (user.getId() != SharedPrefs.getInstance()
                            .getData(SharedPrefs.KEY_SAVE_ID, Integer.class)) {
                        room.setRoomName(user.getFirstName()
                                + " "
                                + user.getLastName()
                                + "("
                                + user.getId()
                                + ")("
                                + room.getRoomId()
                                + ")");
                        room.setAvatar(user.getAvatar());
                        break;
                    }
                }
            }
        }
        return room;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;
            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static User getUserLogin() {
        int id = SharedPrefs.getInstance().getData(KEY_SAVE_ID, Integer.class);
        //get user from Realm
        return new UserDAO().getUser(id);
    }

    public static void selectImage(Activity activity) {
        ImagePicker.create(activity)
                .returnMode(ReturnMode.GALLERY_ONLY)
                .single()
                .start();
    }

    public static boolean checkValidUser(ArrayList<User> users) {
        int i = 0;
        while (i < users.size()) {
            if (users.get(i).getId() == Common.mMineId) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
