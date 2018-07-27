package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import jp.bap.traning.simplechat.model.News;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OnActivityResult;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RealmDAO;
import jp.bap.traning.simplechat.presenter.news.NewsPresenter;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.LocaleManager;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.widget.CustomToolbar_;
import jp.bap.traning.simplechat.widget.FlipPageViewTransformer;
import lombok.Getter;

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

    private MoreFragment_ mMoreFragment = new MoreFragment_();
    private NewsFragment_ mNewsFragment = new NewsFragment_();
    public static boolean checkCall = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void afterView() {
        if (SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class).equals("")) {
            LocaleManager.setLocale(getApplicationContext(), "en");
        } else {
            LocaleManager.setLocale(getApplicationContext(), SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class));
        }
        hiddenProgressBar();
        init();
    }

    private void init() {
        mToolbar.setTitle(getResources().getString(R.string.title_friend_fragment));
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.getBackButton().setVisibility(View.GONE);
        mToolbar.getTvTitle().setGravity(Gravity.CENTER);
        ViewCompat.setElevation(mTabLayout, 10);

        //Setup viewPager
        ViewPagerAdapter mViewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.addFragment(mNewsFragment,
                getResources().getString(R.string.title_tab_news),
                R.drawable.selection_icon_news_tablayout);
        mViewpagerAdapter.addFragment(new FriendFragment_(),
                getResources().getString(R.string.title_tab_friend),
                R.drawable.selection_icon_list_tablayout);
        mViewpagerAdapter.addFragment(new ChatFragment_(),
                getResources().getString(R.string.title_tab_chat),
                R.drawable.selection_icon_chat_tablayout);
        mViewpagerAdapter.addFragment(mMoreFragment,
                getResources().getString(R.string.title_tab_more),
                R.drawable.selection_icon_more_tablayout);
        mViewPager.setAdapter(mViewpagerAdapter);
        mViewPager.setOffscreenPageLimit(4);
        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.setCurrentItem(1);
        mViewPager.addOnPageChangeListener(this);
        mViewPager.setPageTransformer(false, new FlipPageViewTransformer());

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
            case 0: {
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setImageDrawable(getResources().getDrawable(R.drawable.ic_add_a_photo));
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                mToolbar.getmTvCreateNews().setVisibility(View.GONE);
                mToolbar.setTitle(getResources().getString(R.string.title_news_fragment));
                mToolbar.getSharingButton().setVisibility(View.GONE);
                break;
            }

            case 1: {
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                mToolbar.getmTvCreateNews().setVisibility(View.GONE);
                mToolbar.getSharingButton().setVisibility(View.GONE);
                mToolbar.setTitle(getResources().getString(R.string.title_friend_fragment));
                break;
            }

            case 2: {
                mToolbar.setVisibility(View.VISIBLE);
                mToolbar.getTvTitle().setVisibility(View.VISIBLE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.VISIBLE);
                mToolbar.getImgButtonSearch().setVisibility(View.VISIBLE);
                mToolbar.getSharingButton().setVisibility(View.GONE);
                mToolbar.getBackButton().setVisibility(View.GONE);
                mToolbar.getmTvCreateNews().setVisibility(View.GONE);
                mToolbar.getSharingButton().setVisibility(View.GONE);
                mToolbar.setTitle(getResources().getString(R.string.title_chat_fragment));
                break;
            }

            case 3: {
                mToolbar.getTvTitle().setVisibility(View.GONE);
                mToolbar.setVisibility(View.GONE);
                mToolbar.getSettingButton().setVisibility(View.GONE);
                mToolbar.getImgButtonAddGroup().setVisibility(View.GONE);
                mToolbar.getImgButtonSearch().setVisibility(View.GONE);
                mToolbar.getmTvCreateNews().setVisibility(View.GONE);
                mToolbar.getSharingButton().setVisibility(View.GONE);
                break;
            }
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkCall = false;
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
        if (mMoreFragment.pickAvatar == true) {
            mMoreFragment.pickAvatar = false;
            mMoreFragment.onActivityResult(requestCode, resultCode, data);
        }
        if (resultCode == RESULT_OK && requestCode == Common.REQUEST_CHOOSE_LANGUAGE_ACTIVITY) {
            Log.d(TAG, "onActivityResult: REQUEST_CHOOSE_LANGUAGE_ACTIVITY");
            recreate();
        }
    }


    @Override
    public void onCall(int roomId, boolean isAudioCall) {
        super.onCall(roomId, isAudioCall);
        if (checkCall == false) {
            checkCall = true;
            CallActivity_.intent(this)
                    .isIncoming(true)
                    .roomId(roomId)
                    .isAudioCall(isAudioCall)
                    .start();
            overridePendingTransition(R.anim.anim_from_midle, R.anim.anim_to_midle);
        } else {
            //Notify the caller
            if (ChatService.getChat() != null) {
                ChatService.getChat().emitCallBusy(Common.CALL_BUSY, roomId);
            }
        }
    }


}
