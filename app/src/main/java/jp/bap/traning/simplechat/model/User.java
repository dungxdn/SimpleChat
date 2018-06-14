package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@Data
public class User extends RealmObject implements Parcelable{
    @PrimaryKey
    private int id;
    private String userName;
    private String firstName;
    private String lastName;
    private String avatar;
    private String status;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        userName = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        avatar = in.readString();
        status = in.readString();
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
        parcel.writeInt(id);
        parcel.writeString(userName);
        parcel.writeString(firstName);
        parcel.writeString(lastName);
        parcel.writeString(avatar);
        parcel.writeString(status);
    }
}
