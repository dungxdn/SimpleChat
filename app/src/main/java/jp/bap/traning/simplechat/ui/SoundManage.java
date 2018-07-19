package jp.bap.traning.simplechat.ui;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

/**
 * Created by Admin on 7/17/2018.
 */

public class SoundManage {
    private static AudioPlayer mAudioPlayer;

    public static void setAudioForMsgAndCall(Context context, int srcAudio, boolean isRepeat) {
        mAudioPlayer = AudioPlayer.getInstance(context);
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        switch (am.getRingerMode()) {
            case AudioManager.RINGER_MODE_SILENT:
                Log.i("MyApp", "Silent mode");
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                if (isRepeat) {
                    mAudioPlayer.setVibratorRepeat();
                }else{
                    mAudioPlayer.setVibratorNoRepeat();
                }
                Log.i("MyApp", "Vibrate mode");
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                if (isRepeat) {
                    mAudioPlayer.setVibratorRepeat();
                    mAudioPlayer.playAndRepeat(context,srcAudio);
                }else{
                    mAudioPlayer.setVibratorNoRepeat();
                    mAudioPlayer.play(context, srcAudio);
                }
                Log.i("MyApp", "Normal mode");
                break;
        }
    }

    public static void stop(Context context){
        mAudioPlayer = AudioPlayer.getInstance(context);
        mAudioPlayer.stopVibrator();
        mAudioPlayer.stopMedia();
    }
}
