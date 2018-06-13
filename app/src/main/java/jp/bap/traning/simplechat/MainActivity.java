package jp.bap.traning.simplechat;

import android.support.v7.widget.AppCompatEditText;
import android.util.Log;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_main)
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    AppCompatEditText mEditText;

    @Override
    public void afterView() {

    }
}
