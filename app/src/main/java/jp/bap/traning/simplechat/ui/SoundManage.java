package jp.bap.traning.simplechat.ui;

import android.content.Context;

/**
 * Created by Admin on 7/17/2018.
 */

public class SoundManage {
    private static AudioPlayer mAudioPplayer;

    public static AudioPlayer getAudioPlayer(Context context) {
        if (mAudioPplayer == null) {
            mAudioPplayer = new AudioPlayer(context);
        }
        return mAudioPplayer;
    }

}
