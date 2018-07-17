package jp.bap.traning.simplechat.ui;


import android.content.Context;
import android.media.MediaPlayer;

/**
 * Created by Admin on 7/17/2018.
 */
public class AudioPlayer {

    private static MediaPlayer mMediaPlayer;

    public void stop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    public void play(Context c, int rid) {
        stop();

        mMediaPlayer = MediaPlayer.create(c, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stop();
            }
        });

        mMediaPlayer.start();
    }

}