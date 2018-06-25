package jp.bap.traning.simplechat.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;

/**
 * Created by Admin on 6/13/2018.
 */
@EViewGroup(R.layout.layout_custom_toolbar)
public class CustomToolbar extends RelativeLayout {
    @ViewById
    AppCompatTextView mTvTitle;
    @ViewById
    AppCompatImageButton mImgButtonBack;
    @ViewById
    AppCompatImageButton mImgButtonSetting;
    @ViewById
    AppCompatImageButton mImgButtonCall;
    @ViewById
    AppCompatImageButton mImgButtonCallVideo;
    private Context context;

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


    enum FRAGMENT {

        FRIEND("FriendFragment"),
        CHAT("ChatFragment"),
        MORE("MoreFragment");

        private String fragment;

        FRAGMENT(String fragment) {
            this.fragment = fragment;
        }

        public String getFragment() {
            return this.fragment;
        }
    }

    public void setTitle(String title) {
        mTvTitle.setText(title);
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
    public AppCompatImageButton getSettingButton() {
        return mImgButtonSetting;
    }
    public AppCompatImageButton getCallButton() {
        return mImgButtonCall;
    }
    public AppCompatImageButton getCallVideoButton() {
        return mImgButtonCallVideo;
    }
}
