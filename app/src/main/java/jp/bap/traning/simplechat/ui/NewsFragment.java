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
        fakeData();
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


    private void fakeData() {
        News news = new News(Common.getUserLogin(), "I like her", "http://www2.pictures.zimbio.com/gi/Alyssa+Lynch+Milly+Presentation+September+Rg9V50kmCHTl.jpg");
        News news2 = new News(Common.getUserLogin(), "I'm the best", "https://vcdn-thethao.vnecdn.net/2018/07/11/ronaldo5-2974-1531298043.jpg");
        newsArrayList.add(news);
        newsArrayList.add(news2);

        newsAdapter.notifyDataSetChanged();
    }
}
