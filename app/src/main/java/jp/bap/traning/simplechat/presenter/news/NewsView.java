package jp.bap.traning.simplechat.presenter.news;

import java.util.ArrayList;

import jp.bap.traning.simplechat.model.News;

public interface NewsView {
    void getAllNews(ArrayList<News> news);
    void errorGetAllNews();
}
