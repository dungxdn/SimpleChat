package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import io.realm.RealmObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User extends RealmObject implements Parcelable{
    private int id;
    private String firstName;
    private String lastName;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
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
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }
}
