package jp.bap.traning.simplechat.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EView;
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
    };


    @Click
    void mImgButtonBack(){
        Toast.makeText(context, "Back", Toast.LENGTH_SHORT).show();
    }
    @Click
    void mImgButtonSetting(){
        Toast.makeText(context, "Setting", Toast.LENGTH_SHORT).show();
    }

    public void setTitle(String title){
        mTvTitle.setText(title);
    }
    public void setTitleColor(int idColor){
        mTvTitle.setTextColor(idColor);
    }

    public AppCompatImageButton getBackButton() {
        return mImgButtonBack;
    }
    public AppCompatTextView getTvTitle() {
        return mTvTitle;
    }
}
