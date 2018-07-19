package jp.bap.traning.simplechat.presenter.news;

import java.util.ArrayList;

import jp.bap.traning.simplechat.database.NewsDAO;
import jp.bap.traning.simplechat.model.News;

public class NewsInteractor {

    private NewsDAO mNewsDAO;

    public NewsInteractor() {
        mNewsDAO = new NewsDAO();
    }

    void insertOrUpdateNews(News news) {
        mNewsDAO.insertOrUpdateNews(news);
    }

    void getAllNews(NewsView mNewsView) {
        ArrayList<News> newsArrayList = new ArrayList<>();
        newsArrayList = mNewsDAO.getAllMessage();
        if (newsArrayList.size() > 0) {
            mNewsView.getAllNews(newsArrayList);
        } else {
            mNewsView.errorGetAllNews();
        }
    }

    News getOneNewsFromID(long newsID) {
        News mNews = new News();
        mNews = mNewsDAO.getOneNewsFromID(newsID);
        if (mNews.getDescription() == null) {
            return null;
        } else {
            return mNews;
        }
    }
}
