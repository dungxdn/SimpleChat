package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserPresenter;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserView;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_update_profile)
public class UpdateProfileActivity extends BaseActivity {
    private User userLogin;
    @ViewById
    CustomToolbar_ mToolbar;
    @ViewById
    AppCompatEditText mEdtFirstName;
    @ViewById
    AppCompatEditText mEdtLastName;
    @ViewById
    CircleImageView mAvatar;
    @ViewById
    AppCompatButton mBtnUpdate;
    @ViewById
    ProgressBar mProgressBar;
    private RequestOptions options;
    private String linkImageUpdate;

    @Override
    public void afterView() {
        mToolbar.setTitle(getResources().getString(R.string.str_update_profile));
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
        loadDataUserLogin();
        hiddenProgressBar(mProgressBar);
    }

    private void loadDataUserLogin() {
        userLogin = Common.getUserLogin();
        mEdtFirstName.setText(userLogin.getFirstName());
        mEdtLastName.setText(userLogin.getLastName());
        linkImageUpdate = userLogin.getAvatar();

        options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(this).load(userLogin.getAvatar()).apply(options).into(mAvatar);
    }

    @Click({R.id.mBtnUpdate, R.id.mAvatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mAvatar:
                Common.selectImage(this);
                break;
            case R.id.mBtnUpdate:
                showProgressBar(mProgressBar);
                new UpdateUserPresenter().updateUser(mEdtFirstName.getText().toString(), mEdtLastName.getText().toString(), linkImageUpdate, new UpdateUserView() {
                    @Override
                    public void onSuccess(BaseResponse result) {
                        Toast.makeText(getBaseContext(), "Update success", Toast.LENGTH_SHORT).show();
                        userLogin.setAvatar(linkImageUpdate);
                        userLogin.setFirstName(mEdtFirstName.getText().toString());
                        userLogin.setLastName(mEdtLastName.getText().toString());
                        new UserDAO().insertOrUpdate(userLogin);
                        hiddenProgressBar(mProgressBar);
                        finish();
                    }

                    @Override
                    public void onError(String message, int code) {
                        Toast.makeText(getBaseContext(), "Update Error", Toast.LENGTH_SHORT).show();
                        hiddenProgressBar(mProgressBar);
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getBaseContext(), "Update Failure", Toast.LENGTH_SHORT).show();
                        hiddenProgressBar(mProgressBar);
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            showProgressBar(mProgressBar);
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                new UploadImagePresenter().uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        linkImageUpdate = result.getData().getLink();
                        Glide.with(UpdateProfileActivity.this).load(linkImageUpdate).apply(options).into(mAvatar);
                        hiddenProgressBar(mProgressBar);
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d("MoreFragment", "onError: ");
                        hiddenProgressBar(mProgressBar);
                    }

                    @Override
                    public void onFailure() {
                        Log.d("MoreFragment", "onFailure: ");
                        hiddenProgressBar(mProgressBar);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
