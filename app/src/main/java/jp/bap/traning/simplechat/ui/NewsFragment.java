package jp.bap.traning.simplechat.ui;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.model.News;
import jp.bap.traning.simplechat.utils.Common;

@EFragment(R.layout.fragment_news)
public class NewsFragment extends BaseFragment {

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
        newsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNewsCome(News news) {
        super.onNewsCome(news);
        newsArrayList.add(0,news);
        newsAdapter.notifyItemInserted(0);
        listViewNews.smoothScrollToPosition(0);

    }
}
