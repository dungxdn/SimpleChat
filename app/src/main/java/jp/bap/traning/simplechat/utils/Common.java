package jp.bap.traning.simplechat.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;

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
    private static final String TAG = "Common";
    public static final String typeText = "text";
    public static final String typeImage = "image";
    public static final String typeLink = "link";

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

    public static String getNameRoomFromRoomId(int roomId) {
        String nameRoom = "";
        Room room = new RoomDAO().getRoomFromRoomId(roomId);
        if (room != null) {
            if (room.getType() == 0) {
                for (User user : room.getUsers()) {
                    if (user.getId() != SharedPrefs.getInstance()
                            .getData(SharedPrefs.KEY_SAVE_ID, Integer.class)) {
                        nameRoom = user.getFirstName()
                                + " "
                                + user.getLastName()
                                + "("
                                + user.getId()
                                + ")";
                        break;
                    }
                }
            } else {
                nameRoom = "Group";
            }
        }
        return nameRoom;
    }

    public static String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    public static Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    //Test
    public static Bitmap readBitmapAndScale(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;                              //Chỉ đọc thông tin ảnh, không đọc dữ liwwuj
        BitmapFactory.decodeFile(path, options);                          //Đọc thông tin ảnh
        options.inSampleSize = 4; //Scale bitmap xuống 4 lần
        options.inJustDecodeBounds = false; //Cho phép đọc dữ liệu ảnh ảnh
        return BitmapFactory.decodeFile(path, options);
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
}
