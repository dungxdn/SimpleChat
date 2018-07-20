package jp.bap.traning.simplechat.ui;


import android.content.Context;
import android.media.MediaPlayer;
import android.os.Vibrator;

/**
 * Created by Admin on 7/17/2018.
 */
public class AudioPlayer {
    private Vibrator mVibrator;
    private MediaPlayer mMediaPlayer;

    private static AudioPlayer mInstance = null;

    private AudioPlayer(Context context) {
        mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    public static AudioPlayer getInstance(Context context){
        if(mInstance == null){
            mInstance = new AudioPlayer(context);
        }
        return mInstance;
    }

    public void stopMedia() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }

    }

    public void play(Context context, int rid) {
        stopMedia();
        mMediaPlayer = MediaPlayer.create(context, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                stopMedia();
            }
        });

        mMediaPlayer.start();

    }
    public void playAndRepeat(Context context, int rid) {
        stopMedia();
        mMediaPlayer = MediaPlayer.create(context, rid);
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                playAndRepeat(context, rid);
            }
        });

        mMediaPlayer.start();

    }

    public void setVibratorRepeat (){
        long[] mVibratePattern = new long[]{0, 400, 200, 400, 1000};
        mVibrator.vibrate(mVibratePattern, 0);
    }

    public void setVibratorNoRepeat (){
        mVibrator.vibrate(500);
    }

    public void stopVibrator(){
        mVibrator.cancel();
    }


}