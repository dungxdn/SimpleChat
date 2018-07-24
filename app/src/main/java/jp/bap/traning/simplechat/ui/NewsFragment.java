package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.presenter.news.NewsPresenter;
import jp.bap.traning.simplechat.presenter.news.NewsView;
import jp.bap.traning.simplechat.utils.Common;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment {
    private NewsPresenter mNewsPresenter;
    private ArrayList<News> newsArrayList;
    private NewsAdapter newsAdapter;
    @ViewById
    RecyclerView listViewNews;

    @Override
    public void afterView() {
        init();
    }

    private void init() {
        newsArrayList = new ArrayList<>();
        newsAdapter = new NewsAdapter(getActivity(), newsArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        listViewNews.setLayoutManager(mLayoutManager);
        listViewNews.setItemAnimator(new DefaultItemAnimator());
        listViewNews.setAdapter(newsAdapter);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), 1);
        listViewNews.addItemDecoration(mDividerItemDecoration);
        //
        mNewsPresenter = new NewsPresenter(new NewsView() {
            @Override
            public void getAllNews(ArrayList<News> news) {
                newsArrayList.clear();
                for (int i = 0; i < news.size(); i++) {
                    newsArrayList.add(0, news.get(i));
                }
                newsAdapter.notifyDataSetChanged();
                listViewNews.smoothScrollToPosition(0);
            }

            @Override
            public void errorGetAllNews() {
                Log.d("NewsFragment", "Error Get All News");
            }
        });
        //Get All News
        mNewsPresenter.getAllNews();
    }

    @Override
    public void onNewsCome(News news) {
        super.onNewsCome(news);
        int position = checkValidNews(news.getIdNews());
        if (position >= 0) {
            newsArrayList.get(position).setCountComment(news.getCountComment());
            newsAdapter.notifyItemChanged(position);
        } else {
            SoundManage.setAudioForMsgAndCall(getContext(),R.raw.you_have_a_new,false);
            newsArrayList.add(0, news);
            newsAdapter.notifyItemInserted(0);
            listViewNews.smoothScrollToPosition(0);
        }
    }

    @Override
    public void onUserLikeNews(User mUser, News mNews) {
        super.onUserLikeNews(mUser, mNews);
        int position = checkValidNews(mNews.getIdNews());
        if (mUser.getId() == Common.getUserLogin().getId()) {
        } else if (position >= 0) {
            newsArrayList.get(position).setIsLike(mNews.getIsLike());
            if (checkValidUser(mUser.getId(), Common.covertFromRealmListToArrayList(newsArrayList.get(position).getUsersLike())) == false) {
                newsArrayList.get(position).getUsersLike().add(mUser);
            }
            newsAdapter.notifyItemChanged(position);
        }
    }

    private int checkValidNews(long id) {
        for (int i = 0; i < newsArrayList.size(); i++) {
            if (id == newsArrayList.get(i).getIdNews()) {
                return i;
            }
        }
        return -1;
    }

    private boolean checkValidUser(int id, ArrayList<User> mUserList) {
        for (int i = 0; i < mUserList.size(); i++) {
            if (id == mUserList.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().overridePendingTransition(R.anim.anim_slides_in_left,0);
    }
}
