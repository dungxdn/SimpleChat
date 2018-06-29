package jp.bap.traning.simplechat.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.features.ReturnMode;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.Realm;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Message;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserPresenter;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserView;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.Event;
import jp.bap.traning.simplechat.utils.SharedPrefs;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_more)
public class MoreFragment extends BaseFragment {
    @ViewById
    CircleImageView mImgAvata;
    @ViewById
    AppCompatTextView mTvUserName;
    @ViewById
    AppCompatImageButton mImgButtonEdit;
    @ViewById
    AppCompatButton mButtonLogout;
    UploadImagePresenter mUploadImagePresenter;
    UpdateUserPresenter mUpdateUserPresenter;

    private String linkImage;
    private CircleImageView dialogImgAvata;
    private User userLogin;

    @Override
    public void afterView() {

        mUploadImagePresenter = new UploadImagePresenter();
        mUpdateUserPresenter = new UpdateUserPresenter();

    }


    @Override
    public void onResume() {
        super.onResume();
        userLogin = Common.getUserLogin();
        mTvUserName.setText(userLogin.getFirstName() + " " + userLogin.getLastName());

        linkImage = userLogin.getAvatar();
        Log.d("MoreFragment", "onResume: "+linkImage);
        if (linkImage != null) {
            Glide.with(getContext()).load(linkImage).into(mImgAvata);
        }
    }

    @Click
    void mButtonLogout() {
        logout();
    }

    @Click
    void mImgButtonEdit() {
        setDialogEditProfile();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("MoreFragment", "onActivityResult: ");
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                mUploadImagePresenter.uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        Log.d("MoreFragment", "onSuccess: " + result.toString());
                        linkImage = result.getData().getLink();
                        Glide.with(getContext()).load(linkImage).into(dialogImgAvata);

                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d("MoreFragment", "onError: ");
                    }

                    @Override
                    public void onFailure() {
                        Log.d("MoreFragment", "onFailure: ");
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void logout() {
        //Delete session
        SharedPrefs.getInstance().clear();

        //Delete Realm
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.deleteAll();
        realm.commitTransaction();

        //Stop Connect Server
        getBaseActivity().stopService(new Intent(getBaseActivity(), ChatService.class));

        ApiClient.stopApilient();

        //back to SplashActivity
        SplashActivity_.intent(this).start();
        getActivity().finish();


    }

    private void setDialogEditProfile() {
        Dialog mDialog = new Dialog(getContext());
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_edit_profile_layout);

        dialogImgAvata = mDialog.findViewById(R.id.mImgAvatar);
        AppCompatEditText edtFirstName = mDialog.findViewById(R.id.mEdtFirstName);
        AppCompatEditText edtLastName = mDialog.findViewById(R.id.mEdtLastName);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.mBtnCancel);
        AppCompatButton btnSave = mDialog.findViewById(R.id.mBtnSave);

        edtFirstName.setText(userLogin.getFirstName());
        edtLastName.setText(userLogin.getLastName());
        dialogImgAvata.setOnClickListener(view -> Common.selectImage(getActivity()));
        btnCancel.setOnClickListener(view -> mDialog.dismiss());
        btnSave.setOnClickListener(view -> {
            mUpdateUserPresenter.updateUser(edtFirstName.getText().toString(), edtLastName.getText().toString(), linkImage, new UpdateUserView() {
                @Override
                public void onSuccess(BaseResponse result) {
                    Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();
                    userLogin.setAvatar(linkImage);
                    new UserDAO().insertOrUpdate(userLogin);
                    mDialog.dismiss();
                }

                @Override
                public void onError(String message, int code) {
                    Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Update success", Toast.LENGTH_SHORT).show();

                }
            });
        });
        mDialog.show();
    }
}
