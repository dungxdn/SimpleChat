package jp.bap.traning.simplechat.ui;

/**
 * Created by Admin on 7/17/2018.
 */

public class AudioManage {
    private static AudioPlayer mAudioPplayer;

    public static AudioPlayer getAudioPlayer(){
        if(mAudioPplayer == null){
            mAudioPplayer = new AudioPlayer();
        }
        return mAudioPplayer;
    }
}
