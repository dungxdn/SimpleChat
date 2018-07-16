package jp.bap.traning.simplechat.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.support.v7.widget.SearchView;
import android.widget.Button;
import android.widget.RelativeLayout;
import jp.bap.traning.simplechat.ui.SearchGroupChatActivity_;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.ui.AddGroupChatActivity_;

/**
 * Created by Admin on 6/13/2018.
 */
@EViewGroup(R.layout.layout_custom_toolbar)
public class CustomToolbar extends RelativeLayout {
    @ViewById
    AppCompatTextView mTvTitle;
    @ViewById
    AppCompatTextView mTvCreateNews;
    @ViewById
    AppCompatImageButton mImgButtonBack;
    @ViewById
    AppCompatImageButton mImgButtonAddGroup;
    @ViewById
    AppCompatImageButton mImgButtonSetting;
    @ViewById
    AppCompatImageButton mImgButtonCall;
    @ViewById
    AppCompatImageButton mImgButtonCallVideo;
    @ViewById
    AppCompatImageButton mImgButtonSearch;
    @ViewById
    SearchView mSearchView;
    @ViewById
    AppCompatButton mButtonSharing;

    private Context context;

    interface Listenner{
        void onShare();
    }
    Listenner mListenner;

    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomToolbar(Context context) {
        super(context);
        this.context = context;
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
    }

    public void setmTvCreateNews(String titleCreateNews) {
        mTvCreateNews.setText(titleCreateNews);
    }

    public void setTitleColor(int idColor) {
        mTvTitle.setTextColor(idColor);
    }

    public AppCompatImageButton getBackButton() {
        return mImgButtonBack;
    }

    public AppCompatTextView getTvTitle() {
        return mTvTitle;
    }
    public AppCompatTextView getmTvCreateNews() {
        return mTvCreateNews;
    }

    public AppCompatImageButton getSettingButton() {
        return mImgButtonSetting;
    }

    public AppCompatImageButton getCallButton() {
        return mImgButtonCall;
    }

    public AppCompatImageButton getCallVideoButton() {
        return mImgButtonCallVideo;
    }

    public AppCompatImageButton getImgButtonAddGroup() {
        return mImgButtonAddGroup;
    }

    public AppCompatImageButton getImgButtonSearch() {
        return mImgButtonSearch;
    }

    public SearchView getSearchView() {
        return mSearchView;
    }

    public AppCompatButton getSharingButton() {
        return mButtonSharing;
    }

    @Click(R.id.mImgButtonAddGroup)
    public void addGroupChat() {
        AddGroupChatActivity_.intent(getContext()).start();
    }

    @Click(R.id.mImgButtonSearch)
    public void showSearchView() {
        SearchGroupChatActivity_.intent(getContext()).start();
    }

    @Click(R.id.mButtonSharing)
    public void shareNews(){ mListenner.onShare();}
}
