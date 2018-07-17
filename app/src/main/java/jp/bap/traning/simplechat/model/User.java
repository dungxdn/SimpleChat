package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class User extends RealmObject implements Parcelable {
    @PrimaryKey
    private int id;
    private String firstName;
    private String lastName;
    private String avatar;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
        avatar = in.readString();
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
        dest.writeString(avatar);
    }

    public static Comparator<User> userComparator = new Comparator<User>() {
        @Override
        //if String firstName1 > String firstName2 -> return >0; if == -> return 0; if < ->
        // return <0
        public int compare(User user, User t1) {
            if (user.getLastName().compareTo(t1.getLastName()) == 0) {
                return user.getFirstName().compareTo(t1.getFirstName());
            } else {
                return user.getLastName().compareTo(t1.getLastName());
            }
        }
    };
}
