package jp.bap.traning.simplechat.ui;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import de.hdodenhof.circleimageview.CircleImageView;
import io.realm.RealmList;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.RoomDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.RoomData;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomPresenter;
import jp.bap.traning.simplechat.presenter.addrooms.AddRoomView;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImagePresenter;
import jp.bap.traning.simplechat.presenter.uploadimage.UploadImageView;
import jp.bap.traning.simplechat.response.AddRoomResponse;
import jp.bap.traning.simplechat.response.ImageResponse;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_add_group_chat)
public class AddGroupChatActivity extends BaseActivity {

    private AddGroupChatAdapter mAddGroupChatAdapter;
    private ArrayList<User> mUserList;
    private List<Integer> mIdList;
    private AddRoomPresenter mAddRoomPresenter;
    private static int sTYPE_GROUP = 1;
    private RealmList<User> mUserRealmList;
    private static String TAG = "AddGroupChat";
    @Nullable
    private static String sGroupName = null;
    @Nullable
    private static String sLinkImgAvatar = null;

    @ViewById
    CustomToolbar_ mToolbar;

    @ViewById
    RecyclerView mRecyclerFriend;

    @ViewById
    ProgressBar mProgressBar;

    @ViewById
    EditText mEdtGroupName;

    @ViewById
    CircleImageView mImgAvatar;

    @ViewById
    Button mBtnCreate;

    @AfterViews
    public void afterView() {
        overridePendingTransition(R.anim.anim_together,0);
        setupToolbar();
        init();
    }

    public void setupToolbar() {
        mToolbar.setTitle(getResources().getString(R.string.title_new_group));
        mToolbar.getBackButton().setOnClickListener(v -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
    }

    public void init() {
        sGroupName = mEdtGroupName.getText().toString();
        mProgressBar.setVisibility(View.GONE);
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
        mUserRealmList = new RealmList<>();
        mAddRoomPresenter = new AddRoomPresenter();
        mIdList = new ArrayList<>();
        if (mIdList.size() != 0) {
            mIdList.clear();
        }
        mUserList = new ArrayList<>();
        mAddGroupChatAdapter =
                new AddGroupChatAdapter(this, mUserList, new AddGroupChatAdapter.Listener() {
                    @Override
                    public void onAddId(int id) {
                        mIdList.add(id);
                    }

                    @Override
                    public void onRemoveId(int id) {
                        mIdList.remove(new Integer(id));
                    }
                });
        RecyclerView.LayoutManager mLayoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerFriend.setLayoutManager(mLayoutManager);
        mRecyclerFriend.setAdapter(mAddGroupChatAdapter);
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    @Click({R.id.mBtnCreate, R.id.mImgAvatar})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnCreate:
                addGroupChat();
                break;
            case R.id.mImgAvatar:
                Common.selectImage(this);
                showProgressBar(mProgressBar);
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ChatService.getChat() != null) {
            ChatService.getChat().getUsersOnline();
        }
    }

    void addGroupChat() {
        Common.hideKeyboard(AddGroupChatActivity.this);
        showProgressBar(mProgressBar);
        //if haven't pick someone
        if (mIdList.size() <= 0) {
            hiddenProgressBar(mProgressBar);
            Toast.makeText(this, "Pick someone!", Toast.LENGTH_SHORT).show();
            return;
        }
        //
        List<Integer> mIdListFully = new ArrayList<>();
        mIdListFully.clear();
        mIdListFully.add(Common.getUserLogin().getId());
        for (Integer i : mIdList) {
            mIdListFully.add(i);
        }
        //if group have 2 people
        if (mIdListFully.size() == 2) {
            int result = Common.isRoomExits(new RoomDAO().getAllRoom(), mIdListFully);
            sTYPE_GROUP = 0;
            sLinkImgAvatar = null;
            sGroupName = null;
            //if existsed room
            if (result != Common.DEFAULT_VALUE_IF_NOT_EXITS_GROUP) {
                Log.d(TAG, "Get Exists Group (" + result + ")");
                ChatTalksActivity_.intent(AddGroupChatActivity.this).roomId(result).start();
                hiddenProgressBar(mProgressBar);
                finish();
                return;
            }
        } else {
            sTYPE_GROUP = 1;
        }
        //if haven't input groups's name
        if (mIdListFully.size() > 2 && mEdtGroupName.getText().toString().isEmpty()) {
            hiddenProgressBar(mProgressBar);
            Toast.makeText(this, "Please, insert group's name", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mIdListFully.size() > 2) {
            sGroupName = mEdtGroupName.getText().toString().trim();
        }
        //init RealmList to add to realm
        mUserRealmList.clear();
        for (Integer i : mIdList) {
            for (User u : mUserList) {
                if (u.getId() == i) {
                    mUserRealmList.add(u);
                    break;
                }
            }
        }
        mUserRealmList.add(Common.getUserLogin());
        //add Room to server
        mAddRoomPresenter.addroom(mIdList, sTYPE_GROUP, sGroupName, sLinkImgAvatar, new AddRoomView() {
            @Override
            public void onSuccess(AddRoomResponse result) {
                hiddenProgressBar(mProgressBar);
                //insert or update room to Realm
                Common.insertOrUpdateRoomToRealm(result, mUserRealmList);
                //Start ChatActivity
                Log.d(TAG, "Create New Group");
                ChatTalksActivity_.intent(AddGroupChatActivity.this)
                        .roomId(result.getData().getRoomId())
                        .start();
                finish();
            }

            @Override
            public void onError(String message, int code) {
                hiddenProgressBar(mProgressBar);
                Toast.makeText(AddGroupChatActivity.this, code + ", " + message, Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure() {
                hiddenProgressBar(mProgressBar);
                Toast.makeText(AddGroupChatActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            hiddenProgressBar(mProgressBar);
        } else {
            if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
                Image image = ImagePicker.getFirstImageOrNull(data);
                try {
                    File file = new File(image.getPath());
                    new UploadImagePresenter().uploadImage("", "", "", "", file, new UploadImageView() {
                        @Override
                        public void onSuccess(ImageResponse result) {
                            sLinkImgAvatar = result.getData().getLink();
                            Common.setImage(AddGroupChatActivity.this, sLinkImgAvatar, mImgAvatar);
                            hiddenProgressBar(mProgressBar);
                        }

                        @Override
                        public void onError(String message, int code) {
                            Toast.makeText(AddGroupChatActivity.this, message, Toast.LENGTH_SHORT)
                                    .show();
                        }

                        @Override
                        public void onFailure() {
                            Toast.makeText(AddGroupChatActivity.this, "Failed", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //get friend list from API

    @Override
    public void onReceiverListUsersOnline(ArrayList<User> users) {
        super.onReceiverListUsersOnline(users);
        mUserList.clear();
        for (User u : users) {
            mUserList.add(u);
        }
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOnline(User users) {
        super.onUserOnline(users);
        mUserList.add(users);
        mAddGroupChatAdapter.notifyDataSetChanged();
    }

    @Override
    public void onUserOffline(User users) {
        super.onUserOffline(users);
        mUserList.remove(users);
        if (mIdList.contains(new Integer(users.getId()))) {
            mIdList.remove(new Integer(users.getId()));
        }
        mAddGroupChatAdapter.notifyDataSetChanged();
    }
}
