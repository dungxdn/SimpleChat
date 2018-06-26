package jp.bap.traning.simplechat.presenter.chattalks;

public interface ChatTalksListener {
    void onRequestURLSuccess(String link, String title);

    void onRequestURLFailed(String link);
}
