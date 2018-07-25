package jp.bap.traning.simplechat.presenter.chattalks;

public interface ChatTalksListener {
    void onRequestURLSuccess(String link, String title, String srcImage);

    void onRequestURLFailed(String link);
}
