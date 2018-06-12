package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User extends RealmObject implements Parcelable{
    private String userName;
    private String password;
    private String avatar;
    private String firstName;
    private String status;
    private int id;

    public User() {
    }

    protected User(Parcel in) {
        userName = in.readString();
        password = in.readString();
        avatar = in.readString();
        firstName = in.readString();
        status = in.readString();
        id = in.readInt();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(password);
        parcel.writeString(avatar);
        parcel.writeString(firstName);
        parcel.writeString(status);
        parcel.writeInt(id);
    }
}
