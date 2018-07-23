package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class News extends RealmObject implements Parcelable {
    @PrimaryKey
    private long idNews;
    private User user;
    private String description;
    private String imageView;
    private int isLike;
    private RealmList<User> usersLike;
    private int countComment;

    public News() {
    }

    public News(User mUserm, String mDescription, String mImage) {
        this.idNews = System.currentTimeMillis();
        user = mUserm;
        description = mDescription;
        imageView = mImage;
        isLike = 0;
        usersLike = new RealmList<>();
        countComment=0;
    }


    protected News(Parcel in) {
        idNews = in.readLong();
        user = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        imageView = in.readString();
        isLike = in.readInt();
        countComment = in.readInt();
    }

    public static final Creator<News> CREATOR = new Creator<News>() {
        @Override
        public News createFromParcel(Parcel in) {
            return new News(in);
        }

        @Override
        public News[] newArray(int size) {
            return new News[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(idNews);
        parcel.writeParcelable(user, i);
        parcel.writeString(description);
        parcel.writeString(imageView);
        parcel.writeInt(isLike);
        parcel.writeInt(countComment);
    }

    public RealmList<User> getUsersLike() {
        return usersLike;
    }

    public void setUsersLike(RealmList<User> usersLike) {
        this.usersLike = usersLike;
    }
}
