package jp.bap.traning.simplechat;


import android.support.v4.app.Fragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment
public abstract class BaseFragment extends Fragment{
    public abstract void afterView();

    @AfterViews
    public void initView() {
        this.afterView();
    }
}
