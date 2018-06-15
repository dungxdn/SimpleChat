package jp.bap.traning.simplechat.ui;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    TabLayout mTabLayout;
    @ViewById
    ViewPager mViewPager;
    @ViewById
    CustomToolbar_ mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void afterView() {
        init();

    }

    private void init() {
        //Setup viewPager
        ViewPagerAdapter mViewpagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewpagerAdapter.addFragment(new FriendFragment_(), getResources().getString(R.string.title_tab_friend), R.drawable.selection_icon_list_tablayout);
        mViewpagerAdapter.addFragment(new ChatFragment_(), getResources().getString(R.string.title_tab_chat), R.drawable.selection_icon_chat_tablayout);
        mViewpagerAdapter.addFragment(new MoreFragment_(), getResources().getString(R.string.title_tab_more), R.drawable.selection_icon_more_tablayout);
        mViewPager.setAdapter(mViewpagerAdapter);
        mViewPager.setOffscreenPageLimit(3);

        mTabLayout.setupWithViewPager(mViewPager);

        //Setup tab icon
        int length = mTabLayout.getTabCount();
        for (int i = 0; i < length; i++) {
            mTabLayout.getTabAt(i).setCustomView(mViewpagerAdapter.getTabView(i));
        }
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
            View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_custom_tablayout, null);
            AppCompatTextView mTvTitle = view.findViewById(R.id.mTvTitle);
            AppCompatImageView mImgIcon = view.findViewById(R.id.mImgIcon);

            mImgIcon.setImageResource(this.mIconList.get(position));
            mTvTitle.setText(this.mTitleList.get(position));

            return view;
        }

    }


}
