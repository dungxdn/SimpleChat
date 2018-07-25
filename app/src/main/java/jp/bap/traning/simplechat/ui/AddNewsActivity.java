package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
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
import jp.bap.traning.simplechat.widget.CustomToolbar;

@EActivity(R.layout.activity_add_news)
public class AddNewsActivity extends BaseActivity {

    private static String linkImage = "";

    @ViewById
    CircleImageView mAvatarAddNews;
    @ViewById
    AppCompatTextView txtName;
    @ViewById
    AppCompatEditText edtDescription;
    @ViewById
    AppCompatImageView imgAddNews;
    public String name;
    @ViewById
    ProgressBar mProgressBar;
    @ViewById
    CustomToolbar mToolbar;

    @Override
    public void afterView() {
        setupToolbar();
        init();
    }

    private void setupToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.title_tab_add_news));
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.getSharingButton().setVisibility(View.VISIBLE);
        mToolbar.getBackButton().setVisibility(View.VISIBLE);
        //Add Event
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getSharingButton().setOnClickListener(view -> {
            //            Goi Emit
            if (edtDescription.getText().toString().isEmpty()) {
                Toast.makeText(this, "Just say something....", Toast.LENGTH_SHORT).show();
            } else if (linkImage.isEmpty()) {
                Toast.makeText(this, "Please choose a picture!", Toast.LENGTH_SHORT).show();
            } else {
                //Send Event to Server
                if (ChatService.getChat() != null) {
                    ChatService.getChat().emitCreateNews(new News(Common.getUserLogin(), edtDescription.getText().toString(), linkImage));
                }
                Toast.makeText(getApplicationContext(), "Share News Success!", Toast.LENGTH_SHORT).show();
                Common.hideKeyboard(this);
                finish();
            }
        });
    }

    private void init() {
        mProgressBar.setVisibility(View.GONE);
        Common.setAvatar(this, Common.getUserLogin().getId(), mAvatarAddNews);
        txtName.setText(Common.getUserLogin().getFirstName() + " " + Common.getUserLogin().getLastName());
    }

    public News getNews() {
        if (edtDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "Just say something....", Toast.LENGTH_SHORT).show();
            return null;

        } else if (linkImage.isEmpty()) {
            Toast.makeText(this, "Please choose a picture!", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            return new News(Common.getUserLogin(), edtDescription.getText().toString(), linkImage);
        }
    }

    @Click(R.id.imgAddNews)
    void choosePicture() {
        Common.selectImage(this);
        showProgressBar(mProgressBar);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            hiddenProgressBar(mProgressBar);
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                new UploadImagePresenter().uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        linkImage = result.getData().getLink();
                        Common.setImage(AddNewsActivity.this, linkImage, imgAddNews);
                        hiddenProgressBar(mProgressBar);
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d("AddNewsActiviy", "onError: ");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("AddNewsActiviy", "onFailure: ");
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
