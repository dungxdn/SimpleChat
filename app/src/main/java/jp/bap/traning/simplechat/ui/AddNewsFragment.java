package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EFragment(R.layout.fragment_add_news)
public class AddNewsFragment extends BaseFragment {

    public static String linkImage = "";
    public static boolean pickImage = false;
    @ViewById
    CircleImageView mAvatarAddNews;
    @ViewById
    AppCompatTextView txtName;
    @ViewById
    AppCompatEditText edtDescription;
    @ViewById
    AppCompatImageView imgAddNews;
    public String name;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        Common.setAvatar(getContext(), Common.getUserLogin().getId(), mAvatarAddNews);
        txtName.setText(Common.getUserLogin().getFirstName() + " " + Common.getUserLogin().getLastName());
    }

    public News getNews() {
        if (edtDescription.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "Just say something....", Toast.LENGTH_SHORT).show();
            return null;

        } else if (linkImage.isEmpty()) {
            Toast.makeText(getContext(), "Please choose a picture!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return new News(Common.getUserLogin(), edtDescription.getText().toString(), linkImage);
        }
    }

    @Click(R.id.imgAddNews)
    void choosePicture() {
        pickImage = true;
        Common.selectImage(getContext());
        ((MainActivity) getActivity()).showProgressBar();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("AddNewsFragment", "onActivityResult");
        if (data == null) {
            ((MainActivity) getActivity()).hiddenProgressBar();
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                new UploadImagePresenter().uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        linkImage = result.getData().getLink();
                        Common.setImage(getContext(), linkImage, imgAddNews);
                        ((MainActivity) getActivity()).hiddenProgressBar();
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d("AddNewsFragment", "onError: ");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("AddNewsFragment", "onFailure: ");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
