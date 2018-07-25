package jp.bap.traning.simplechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.LocaleManager;
import jp.bap.traning.simplechat.utils.SharedPrefs;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_choose_language)
public class ChooseLanguageActivity extends BaseActivity {
    private final String TAG = "ChooseLanguageActivity";
    @ViewById
    ListView lvLanguage;
    @ViewById
    CustomToolbar_ mToolbar;

    ArrayAdapter<String> mAdapter;
    private String[] mListLanguage;

    @Override
    public void afterView() {
        init();
    }

    void init() {
        mToolbar.setTitle(getResources().getString(R.string.str_language));
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mListLanguage = getResources().getStringArray(R.array.language_array);
        lvLanguage.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, mListLanguage);
        lvLanguage.setAdapter(mAdapter);
        if (SharedPrefs.getInstance().getData(Common.KEY_CHOOSE_LANGUAGE, String.class).equals("vi")) {
            lvLanguage.setItemChecked(1, true);
        } else {
            lvLanguage.setItemChecked(0, true);
        }
        lvLanguage.setOnItemClickListener((adapterView, view, i, l) -> {
            ListView lv = (ListView) adapterView;
            TextView tv = (TextView) lv.getChildAt(i);
            CheckedTextView cv = (CheckedTextView) tv;
            if (!cv.isChecked())
                cv.setChecked(true);
            switch (i) {
                case 0:
                    LocaleManager.setLocale(getApplicationContext(), "en");
                    break;
                case 1:
                    LocaleManager.setLocale(getApplicationContext(), "vi");
                    break;
            }
            recreate();
            setResult(Common.REQUEST_CHOOSE_LANGUAGE_ACTIVITY);
        });
    }
}
