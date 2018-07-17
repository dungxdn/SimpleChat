package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Comment implements Parcelable {
    private long idComment;
    private User user;
    private String comment;

    public Comment(User mUser, String mComment) {
        idComment = System.currentTimeMillis();
        user = mUser;
        comment = mComment;
    }

    protected Comment(Parcel in) {
        idComment = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        comment = in.readString();
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
    }
}
