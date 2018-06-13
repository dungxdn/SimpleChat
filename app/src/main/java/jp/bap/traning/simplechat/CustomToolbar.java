package jp.bap.traning.simplechat;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

import org.androidannotations.annotations.EView;
import org.androidannotations.annotations.EViewGroup;

import jp.bap.traning.simplechat.R;

/**
 * Created by Admin on 6/13/2018.
 */
@EViewGroup(R.layout.layout_custom_toolbar)
public class CustomToolbar extends RelativeLayout {
    public CustomToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CustomToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomToolbar(Context context) {
        super(context);
    }
}
