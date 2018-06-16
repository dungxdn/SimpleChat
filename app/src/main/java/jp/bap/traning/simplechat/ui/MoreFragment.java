package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.SharedPrefs;

/**
 * Created by Admin on 6/13/2018.
 */
@EFragment(R.layout.fragment_more)
public class MoreFragment extends BaseFragment {
    @ViewById
    CircleImageView mImgAvata;
    @ViewById
    AppCompatTextView mTvUserName;
    @ViewById
    AppCompatImageButton mImgButtonEdit;
    @ViewById
    AppCompatButton mButtonLogout;

    @Override
    public void afterView() {
        User user = getUserLogin();
        mTvUserName.setText(user.getFirstName()+" "+user.getLastName());
    }

    private User getUserLogin(){
        int id = SharedPrefs.getInstance().getData(SharedPrefs.KEY_SAVE_ID,Integer.class);
        return new UserDAO().getUser(id);
    }
    @Click
    void mButtonLogout(){
        Toast.makeText(getContext(), "LOGOUT", Toast.LENGTH_SHORT).show();
    }
}
