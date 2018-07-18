package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = false)
public class Comment extends RealmObject implements Parcelable {
    @PrimaryKey
    private long idComment;
    private User user;
    private String comment;
    private long idNews;

    public Comment() {
    }

    public Comment(long id,User mUser, String mComment) {
        idNews = id;
        idComment = System.currentTimeMillis();
        user = mUser;
        comment = mComment;
    }


    protected Comment(Parcel in) {
        idComment = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readString();
        idNews = in.readLong();
    }

    public static final Creator<Comment> CREATOR = new Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(idComment);
        parcel.writeParcelable(user, i);
        parcel.writeString(comment);
        parcel.writeLong(idNews);
    }
}
