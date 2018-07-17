package jp.bap.traning.simplechat.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageButton;
import android.widget.ImageView;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class News implements Parcelable {

    private User user;
    private String description;
    private String imageView;

    public News() {
    }


    private News(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        description = in.readString();
        imageView = in.readString();
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
        parcel.writeParcelable(user, i);
        parcel.writeString(description);
        parcel.writeString(imageView);
    }
}
