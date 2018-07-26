package jp.bap.traning.simplechat.ui;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RealmDAO;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserPresenter;
import jp.bap.traning.simplechat.presenter.updateuser.UpdateUserView;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.BaseResponse;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ApiClient;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.service.ImgurClient;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_more)
public class MoreFragment extends BaseFragment {
    public static boolean pickAvatar = false;
    @ViewById
    CircleImageView mImgAvata;
    @ViewById
    AppCompatTextView mTvUserName;
    @ViewById
    ImageView mCoverPhoto;

    UploadImagePresenter mUploadImagePresenter;
    UpdateUserPresenter mUpdateUserPresenter;

    private User userLogin;
    private RequestOptions options;
    private RealmDAO mRealmDAO;


    @Override
    public void afterView() {
        mUploadImagePresenter = new UploadImagePresenter();
        mUpdateUserPresenter = new UpdateUserPresenter();
        mRealmDAO = new RealmDAO();

        options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDataUserLogin();
    }

    @Override
    public void onStart() {
        super.onStart();
        mRealmDAO.realmChanged((o, check) -> {
            loadDataUserLogin();
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        //rove MessageListener
        mRealmDAO.removeRealmChanged();
    }

    private void loadDataUserLogin() {
        userLogin = Common.getUserLogin();
        RequestOptions options1 = new RequestOptions();
        options1.centerCrop();
        options1.placeholder(R.drawable.test1);
        options1.error(R.drawable.test1);
        Glide.with(getContext()).load(userLogin.getAvatar()).apply(options1).into(mCoverPhoto);
        mTvUserName.setText(userLogin.getFirstName() + " " + userLogin.getLastName());
        Glide.with(getContext()).load(userLogin.getAvatar()).apply(options).into(mImgAvata);
    }

    @Click({R.id.lnUpdateProfile, R.id.lnAbout, R.id.lnLanguage, R.id.mlnLogout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.lnUpdateProfile:
                UpdateProfileActivity_.intent(getActivity()).start();
                break;
            case R.id.mlnLogout:
                logout();
                break;
            case R.id.lnLanguage:
                ChooseLanguageActivity_.intent(this).startForResult(Common.REQUEST_CHOOSE_LANGUAGE_ACTIVITY);
                break;
            case R.id.lnAbout:
                AboutActivity_.intent(getActivity()).start();
                break;
        }
    }

    private void logout() {
        //Delete session
        String language = SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class);
        SharedPrefs.getInstance().clear();
        SharedPrefs.getInstance().putData(Common.KEY_CHOOSE_LANGUAGE, language);

        //Stop Connect Server
        getBaseActivity().stopService(new Intent(getBaseActivity(), ChatService.class));

        ApiClient.stopApilient();
        ImgurClient.stopImgurClient();

        //back to SplashActivity
        SplashActivity_.intent(this).extra(Common.REQUEST_LOGOUT_KEY, Common.REQUEST_LOGOUT).start();
        getActivity().finish();
    }

    @OnActivityResult(Common.REQUEST_CHOOSE_LANGUAGE_ACTIVITY)
    public void reload() {
        getActivity().recreate();
    }
}
