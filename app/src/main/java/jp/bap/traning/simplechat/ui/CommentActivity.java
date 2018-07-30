package jp.bap.traning.simplechat.ui;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.Comment;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.presenter.comment.CommentPresenter;
import jp.bap.traning.simplechat.presenter.comment.CommentView;
import jp.bap.traning.simplechat.presenter.news.NewsPresenter;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.widget.CustomToolbar_;

@EActivity(R.layout.activity_comment)
public class CommentActivity extends BaseActivity {

    private CommentPresenter commentPresenter;
    private ArrayList<Comment> listComment;
    private CommentAdapter commentAdapter;
    @ViewById
    RecyclerView recyclerViewComment;
    @ViewById
    CircleImageView mAvatarComment;
    @ViewById
    AppCompatEditText edtComment;
    @ViewById
    AppCompatImageButton imgButtonSendComment;
    @ViewById
    ProgressBar mProgressBar;
    @ViewById
    CustomToolbar_ mToolbar;

    @Extra
    News mNews;

    @Override
    public void afterView() {
        setupToolbar();
        init();
        getAllCommentFirst();
        addEvents();
    }

    private void setupToolbar() {
        mToolbar.getCallButton().setVisibility(View.GONE);
        mToolbar.getCallVideoButton().setVisibility(View.GONE);
        mToolbar.setTitle(getResources().getString(R.string.title_comment_activity));
        mToolbar.getBackButton().setOnClickListener(view -> finish());
        mToolbar.getSettingButton().setVisibility(View.GONE);
        mToolbar.setBackgroundColor(Color.WHITE);
    }

    private void init() {
        Common.setAvatar(this, Common.getUserLogin().getId(), mAvatarComment);
        mProgressBar.setVisibility(View.GONE);
        listComment = new ArrayList<>();
        commentAdapter = new CommentAdapter(this, listComment);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewComment.setLayoutManager(mLayoutManager);
        recyclerViewComment.setItemAnimator(new DefaultItemAnimator());
        recyclerViewComment.setAdapter(commentAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(this, 1);
        recyclerViewComment.addItemDecoration(mDividerItemDecoration);
        commentAdapter.notifyDataSetChanged();
    }

    private void getAllCommentFirst() {
        commentPresenter = new CommentPresenter(new CommentView() {
            @Override
            public void getAllComment(ArrayList<Comment> commentArrayList) {
                listComment.clear();
                for (int i = 0; i < commentArrayList.size(); i++) {
                    listComment.add(commentArrayList.get(i));
                }
                commentAdapter.notifyDataSetChanged();
                recyclerViewComment.smoothScrollToPosition(listComment.size() - 1);
            }

            @Override
            public void errorGetAllComment(long newsID) {

            }
        });
        commentPresenter.getAllComment(mNews.getIdNews());
    }

    private void addEvents() {
        recyclerViewComment.setOnTouchListener((view, motionEvent) -> {
            Common.hideKeyboard((Activity) view.getContext());
            return false;
        });
    }

    @Click(R.id.imgButtonSendComment)
    public void sendComment() {
        if (edtComment.getText().toString().isEmpty()) {
            Toast.makeText(this, "Say your feeling...", Toast.LENGTH_SHORT).show();
        } else {
            Comment mComment = new Comment(mNews.getIdNews(), Common.getUserLogin(), edtComment.getText().toString());
            //Update News
            mNews.setCountComment(mNews.getCountComment() + 1);
            new NewsPresenter().insertOrUpdateNews(mNews);
            //Send Event to Server
            if (ChatService.getChat() != null) {
                ChatService.getChat().emitOnComment(mComment);
                ChatService.getChat().emitCreateNews(mNews);
            }
            edtComment.setText("");
        }
    }

    @Override
    public void onCommentReceive(Comment comment) {
        super.onCommentReceive(comment);
        if (comment.getIdNews() == mNews.getIdNews()) {
            if (listComment.contains(comment) == true) {
            } else {
                listComment.add(comment);
                commentAdapter.notifyDataSetChanged();
                recyclerViewComment.smoothScrollToPosition(listComment.size() - 1);
            }
        }
    }
}