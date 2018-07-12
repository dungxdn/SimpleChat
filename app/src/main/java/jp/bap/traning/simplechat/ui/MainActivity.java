package jp.bap.traning.simplechat.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import jp.bap.traning.simplechat.utils.Event;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.util.ArrayList;

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
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;
import lombok.Getter;

import org.json.JSONObject;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    TabLayout mTabLayout;
    @ViewById
    ViewPager mViewPager;
    @ViewById
    CustomToolbar_ mToolbar;
    @ViewById
    ProgressBar mProgressBar;

    @Getter
    private RealmDAO mRealmDAO;

    private final String FRIEND_TITLE = "Friends";
    private final String CHAT_TITLE = "Chat";
    private final String MORE_TITLE = "More";
    private final String NEWS_TITLE = "News";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void afterView() {
        Log.d(TAG, "onCreate: ");
        hiddenProgressBar();
        init();
    }

    private void init() {
        mToolbar.setTitle(NEWS_TITLE);
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.getBackButton().setVisibility(View.GONE);
        mToolbar.getTvTitle().setGravity(Gravity.CENTER);
        ViewCompat.setElevation(mTabLayout, 10);

        //Setup viewPager
        ViewPagerAdapter mViewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.addFragment(new NewsFragment_(),
                getResources().getString(R.string.title_tab_news),
                R.drawable.ic_news);
        mViewpagerAdapter.addFragment(new FriendFragment_(),
                getResources().getString(R.string.title_tab_friend),
                R.drawable.selection_icon_list_tablayout);
        mViewpagerAdapter.addFragment(new ChatFragment_(),
                getResources().getString(R.string.title_tab_chat),
                R.drawable.selection_icon_chat_tablayout);
        mViewpagerAdapter.addFragment(new MoreFragment_(),
                getResources().getString(R.string.title_tab_more),
                R.drawable.selection_icon_more_tablayout);
        mViewPager.setAdapter(mViewpagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(this);

        //Setup tab icon
        int length = mTabLayout.getTabCount();
        for (int i = 0; i < length; i++) {
            mTabLayout.getTabAt(i).setCustomView(mViewpagerAdapter.getTabView(i));
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                mToolbar.setTitle(NEWS_TITLE);
                break;

            case 1:
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                mToolbar.setTitle(FRIEND_TITLE);
                break;

            case 2:
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.VISIBLE);
                mToolbar.getImgButtonSearch().setVisibility(View.VISIBLE);
                mToolbar.setTitle(CHAT_TITLE);
                break;

            case 3:
                mToolbar.getTvTitle().setVisibility(View.GONE);
                mToolbar.getSettingButton().setVisibility(View.VISIBLE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mTitleList = new ArrayList<>();
        private final ArrayList<Integer> mIconList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title, int icon) {
            mFragmentList.add(fragment);
            mTitleList.add(title);
            mIconList.add(icon);
        }

        public View getTabView(int position) {
            View view = LayoutInflater.from(getApplicationContext())
                    .inflate(R.layout.layout_custom_tablayout, null);
            AppCompatTextView mTvTitle = view.findViewById(R.id.mTvTitle);
            AppCompatImageView mImgIcon = view.findViewById(R.id.mImgIcon);

            mImgIcon.setImageResource(this.mIconList.get(position));
            mTvTitle.setText(this.mTitleList.get(position));

            return view;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
        ChatService.setChatManagerNull();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: ");
    }

    public void showProgressBar() {
        showProgressBar(mProgressBar);
    }

    public void hiddenProgressBar() {
        hiddenProgressBar(mProgressBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: ");
        showProgressBar();
        if (data != null) {
            showProgressBar();
        } else {
            hiddenProgressBar();
        }
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            Image image = ImagePicker.getFirstImageOrNull(data);
            try {
                File mFile = new File(image.getPath());
                new UploadImagePresenter().uploadImage("", "", "", "", mFile, new UploadImageView() {
                    @Override
                    public void onSuccess(ImageResponse result) {
                        Log.d(TAG, "onSuccess: " + result.toString());
                        String linkImage = result.getData().getLink();
                        setDialogEditProfile(Common.getUserLogin(), linkImage);
                        hiddenProgressBar();
                    }

                    @Override
                    public void onError(String message, int code) {
                        Log.d(TAG, "onError: ");
                        Toast.makeText(MainActivity.this, "Error when upload image", Toast.LENGTH_SHORT).show();
                        hiddenProgressBar();
                    }

                    @Override
                    public void onFailure() {
                        Log.d(TAG, "onFailure: ");
                        Toast.makeText(MainActivity.this, "Upload image fail", Toast.LENGTH_SHORT).show();
                        hiddenProgressBar();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static String mFirstName = Common.getUserLogin().getFirstName();
    public static String mLastname = Common.getUserLogin().getLastName();

    public void setDialogEditProfile(User user, String linkImage) {
        Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.dialog_edit_profile_layout);
        mDialog.setCancelable(false);

        CircleImageView dialogImgAvata = mDialog.findViewById(R.id.mImgAvatar);
        AppCompatEditText edtFirstName = mDialog.findViewById(R.id.mEdtFirstName);
        AppCompatEditText edtLastName = mDialog.findViewById(R.id.mEdtLastName);
        AppCompatButton btnCancel = mDialog.findViewById(R.id.mBtnCancel);
        AppCompatButton btnSave = mDialog.findViewById(R.id.mBtnSave);
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(this).load(linkImage).apply(options).into(dialogImgAvata);
        edtFirstName.setText(mFirstName);
        edtLastName.setText(mLastname);
        dialogImgAvata.setOnClickListener(view -> {
                    Common.selectImage(this);
                    mFirstName = edtFirstName.getText().toString();
                    mLastname = edtLastName.getText().toString();
                    mDialog.dismiss();
                }
        );
        btnCancel.setOnClickListener(view -> {
            mDialog.dismiss();
            mFirstName = user.getFirstName();
            mLastname = user.getLastName();
        });
        btnSave.setOnClickListener(view -> {
            new UpdateUserPresenter().updateUser(edtFirstName.getText().toString(), edtLastName.getText().toString(), linkImage, new UpdateUserView() {
                @Override
                public void onSuccess(BaseResponse result) {
                    Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                    user.setAvatar(linkImage);
                    user.setFirstName(edtFirstName.getText().toString());
                    user.setLastName(edtLastName.getText().toString());
                    new UserDAO().insertOrUpdate(user);
                    mDialog.dismiss();
                }

                @Override
                public void onError(String message, int code) {
                    Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                }
            });
        });
        mDialog.show();
    }

    @Override
    public void onCall(int roomId) {
        super.onCall(roomId);
        CallActivity_.intent(this)
                .isIncoming(true)
                .roomId(roomId)
                .start();
    }
}
