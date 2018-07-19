package jp.bap.traning.simplechat.database;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import jp.bap.traning.simplechat.model.News;

public class NewsDAO {

    public NewsDAO(){}

    public void insertOrUpdateNews(News news){
        Realm mRealm = Realm.getDefaultInstance();
        mRealm.executeTransaction(realm -> {
            realm.insertOrUpdate(news);
        });
        mRealm.close();
    }

    public ArrayList<News> getAllMessage() {
        ArrayList<News> newsList = new ArrayList<>();
        Realm mRealm = Realm.getDefaultInstance();
        RealmResults<News> results = mRealm.where(News.class).findAll();
        for (News mNews : results) {
            newsList.add(mRealm.copyFromRealm(mNews));
        }
//        mRealm.close();
        return newsList;
    }

    public News getOneNewsFromID(long idNews){
        Realm mRealm = Realm.getDefaultInstance();
        News mNews = mRealm.where(News.class)
                .equalTo("idNews", idNews)
                .findFirst();
        News result = null;
        if (mNews != null) {
            result = mRealm.copyFromRealm(mNews);
        }
        mRealm.close();
        return result;
    }
}
