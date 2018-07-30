package jp.bap.traning.simplechat.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import de.hdodenhof.circleimageview.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.database.UserDAO;
import jp.bap.traning.simplechat.model.Room;
import jp.bap.traning.simplechat.model.User;
import jp.bap.traning.simplechat.utils.Permission;
import jp.bap.traning.simplechat.webrtc.CustomPeerConnectionObserver;
import jp.bap.traning.simplechat.webrtc.CustomSdpObserver;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import pl.bclogic.pulsator4droid.library.PulsatorLayout;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.CameraVideoCapturer;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.Logging;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

@EActivity(R.layout.activity_call)
public class CallActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @ViewById
    SurfaceViewRenderer mRemoteVideoView;
    @ViewById
    SurfaceViewRenderer mLocalVideoView;
    @ViewById
    AppCompatTextView mtvStatus;
    @ViewById
    CircleImageView mImgAvatarCallAudio;
    @ViewById
    AppCompatImageButton mBtnAccept;
    @ViewById
    AppCompatImageButton mBtnSwitchCamera;
    @ViewById
    AppCompatImageButton mBtnTurnOnSpeaker;
    @ViewById
    AppCompatImageButton mBtnTurnOffSpeaker;
    @ViewById
    AppCompatImageButton mBtnTurnOnVideoCam;
    @ViewById
    AppCompatImageButton mBtnTurnOffVideoCam;
    @ViewById
    TextView mTvTurnOffVideoCam;
    @ViewById
    PulsatorLayout pulsatorLayout;
    @Extra
    int roomId;
    @Extra
    boolean isIncoming;
    @Extra
    boolean isAudioCall;

    private PeerConnectionFactory peerConnectionFactory;
    private MediaConstraints audioConstraints;
    private MediaConstraints videoConstraints;
    private MediaConstraints sdpConstraints;
    private VideoSource videoSource;
    private VideoTrack localVideoTrack;
    private AudioSource audioSource;
    private AudioTrack localAudioTrack;
    private VideoRenderer localRenderer;
    private VideoRenderer remoteRenderer;
    private PeerConnection localPeer;
    private EglBase rootEglBase;
    private boolean gotUserMedia;
    private VideoCapturer videoCapturerAndroid;
    private List<PeerConnection.IceServer> peerIceServers = new ArrayList<>();
    MediaStream mediaStreamLocal;
    MediaStream mediaStreamRemote;
    private Room mRoom;
    private AudioManager mAudioManager;
    public Animation animationShake;

    private String[] permissionRequired = new String[] {
            Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
    };

    @Override
    public void afterView() {
        mRoom = Common.getFullRoomFromRoomId(roomId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Permission.initPermission(this, permissionRequired);
        }
        init();
    }

    public void init() {
        initVideos();
        getIceServers();
        start();
        RequestOptions options = new RequestOptions();
        options.centerCrop();
        options.placeholder(R.drawable.ic_avatar_default);
        options.error(R.drawable.ic_avatar_default);
        Glide.with(this).load(mRoom.getAvatar()).apply(options).into(mImgAvatarCallAudio);
        mImgAvatarCallAudio.setVisibility(View.VISIBLE);
        mLocalVideoView.setVisibility(View.GONE);
        mRemoteVideoView.setVisibility(View.GONE);
        if (isIncoming) {
            animationShake = AnimationUtils.loadAnimation(CallActivity.this, R.anim.anim_shake_button_accept);
            if (isAudioCall) {
                mtvStatus.setText(getResources().getString(R.string.text_incoming_call_audio) + " "+mRoom.getRoomName());
            } else {
                mtvStatus.setText(getResources().getString(R.string.text_incoming_call_video)+ " "+mRoom.getRoomName());
            }
            SoundManage.setAudioForMsgAndCall(this, R.raw.wedonttalkanymore, true);
            mBtnAccept.setVisibility(View.VISIBLE);
            mBtnAccept.startAnimation(animationShake);
            pulsatorLayout.start();
        } else {
            if (isAudioCall) {
                if (ChatService.getChat() != null) {
                    ChatService.getChat().emitCall(roomId, true);
                }
                mtvStatus.setText(getResources().getString(R.string.text_call_audio_to) + " "+mRoom.getRoomName());
            } else {
                if (ChatService.getChat() != null) {
                    ChatService.getChat().emitCall(roomId, false);
                }
                mtvStatus.setText(getResources().getString(R.string.text_call_video_to) + " "+mRoom.getRoomName());
            }
            mBtnAccept.setVisibility(View.GONE);
            pulsatorLayout.start();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for (int r : grantResults) {
            if (r != PackageManager.PERMISSION_GRANTED) {
                ChatService.getChat().emitCallStop(roomId);
                Toast.makeText(this, getResources().getString(R.string.text_accept_permission),
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private void initVideos() {
        rootEglBase = EglBase.create();
        if (!isAudioCall) {
            mLocalVideoView.init(rootEglBase.getEglBaseContext(), null);
            mRemoteVideoView.init(rootEglBase.getEglBaseContext(), null);
            mLocalVideoView.setZOrderMediaOverlay(true);
        }
    }

    private void getIceServers() {
        PeerConnection.IceServer peerIceServerSTUN =
                PeerConnection.IceServer.builder(Common.STUN_URL).createIceServer();
        peerIceServers.add(peerIceServerSTUN);
    }

    public void start() {
        //Initialize PeerConnectionFactory globals.
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(this)
                        .setEnableVideoHwAcceleration(true)
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        //Create a new PeerConnectionFactory instance - using Hardware encoder and decoder.
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                rootEglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */
                true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory =
                new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
        peerConnectionFactory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();

        //set external speaker if video call
        mAudioManager = (AudioManager) getSystemService(this.AUDIO_SERVICE);

        //Now create a VideoCapturer instance.
        videoCapturerAndroid = createFrontCameraCapturer(new Camera1Enumerator(false));
        //Create MediaConstraints - Will be useful for specifying video and audio constraints.
        audioConstraints = new MediaConstraints();
        videoConstraints = new MediaConstraints();

        //Create a VideoSource instance
        if (videoCapturerAndroid != null) {
            videoSource = peerConnectionFactory.createVideoSource(videoCapturerAndroid);
        }
        localVideoTrack = peerConnectionFactory.createVideoTrack("100", videoSource);

        //create an AudioSource instance
        audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        localAudioTrack = peerConnectionFactory.createAudioTrack("101", audioSource);

        if (videoCapturerAndroid != null) {
            videoCapturerAndroid.startCapture(1280, 720, 30);
        }
        if (!isAudioCall) {
            mLocalVideoView.setVisibility(View.VISIBLE);
        } else {
            mLocalVideoView.setVisibility(View.GONE);
        }
        //create a videoRenderer based on SurfaceViewRenderer instance
        localRenderer = new VideoRenderer(mLocalVideoView);
        // And finally, with our VideoRenderer ready, we
        // can add our renderer to the VideoTrack.
        localVideoTrack.addRenderer(localRenderer);

        mLocalVideoView.setMirror(true);
        mRemoteVideoView.setMirror(true);

        gotUserMedia = true;
    }

    private VideoCapturer createFrontCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            Log.d("DeviceName", deviceName);
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // Front facing camera not found, try something else
        Log.d(TAG, "Looking for other cameras.");
        for (String deviceName : deviceNames) {
            Log.d("DeviceName", deviceName);
            if (!enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating other camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private void switchCamera() {
        if (videoCapturerAndroid != null) {
            if (videoCapturerAndroid instanceof CameraVideoCapturer) {
                CameraVideoCapturer cameraVideoCapturer =
                        (CameraVideoCapturer) videoCapturerAndroid;
                cameraVideoCapturer.switchCamera(null);
                mLocalVideoView.setMirror(false);
                mRemoteVideoView.setMirror(false);
            } else {

            }
        }
    }

    /**
     * Creating the local peerconnection instance
     */
    private void createPeerConnection() {
        PeerConnection.RTCConfiguration rtcConfig =
                new PeerConnection.RTCConfiguration(peerIceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy =
                PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;

        sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(
                new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));

        localPeer = peerConnectionFactory.createPeerConnection(rtcConfig,
                new CustomPeerConnectionObserver("localPeerCreation") {
                    @Override
                    public void onIceCandidate(IceCandidate iceCandidate) {
                        super.onIceCandidate(iceCandidate);
                        try {
                            JSONObject object = new JSONObject();
                            object.put("type", "candidate");
                            object.put("label", iceCandidate.sdpMLineIndex);
                            object.put("id", iceCandidate.sdpMid);
                            object.put("candidate", iceCandidate.sdp);
                            ChatService.getChat().emitCallContent(object, roomId);
                            //                            mLocalVideoView.setZOrderMediaOverlay
                            // (true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onAddStream(MediaStream mediaStream) {
                        Log.d(TAG, "Received Remote stream");
                        mediaStreamRemote = mediaStream;
                        super.onAddStream(mediaStream);
                        gotRemoteStream(mediaStream);
                    }
                });

        addStreamToLocalPeer();
    }

    /**
     * Adding the stream to the localpeer
     */
    private void addStreamToLocalPeer() {
        //creating local mediastream
        mediaStreamLocal = peerConnectionFactory.createLocalMediaStream("102");
        mediaStreamLocal.addTrack(localAudioTrack);
        mediaStreamLocal.addTrack(localVideoTrack);
        localPeer.addStream(mediaStreamLocal);
    }

    /**
     * This method is called when the app is initiator - We generate the offer and send it over
     * through socket
     * to remote peer
     */
    private void doCall() {
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"),
                        sessionDescription);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", sessionDescription.type.canonicalForm());
                    obj.put("sdp", sessionDescription.description);
                    ChatService.getChat().emitCallContent(obj, roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, sdpConstraints);
    }

    /**
     * Received remote peer's media stream. we will get the first video track and render it
     */
    private void gotRemoteStream(MediaStream stream) {
        //we have remote video stream. add to the renderer.
        final VideoTrack videoTrack = stream.videoTracks.get(0);
        runOnUiThread(() -> {
            try {
                remoteRenderer = new VideoRenderer(mRemoteVideoView);
                mRemoteVideoView.setVisibility(View.VISIBLE);
                videoTrack.addRenderer(remoteRenderer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Click({
            R.id.mBtnAccept, R.id.mBtnStop, R.id.mBtnSwitchCamera, R.id.mBtnTurnOnSpeaker,
            R.id.mBtnTurnOffSpeaker, R.id.mBtnTurnOnVideoCam, R.id.mBtnTurnOffVideoCam
    })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.mBtnStop:
                SoundManage.stop(this);
                mtvStatus.setText(getResources().getString(R.string.text_call_end));
                if (ChatService.getChat() != null) {
                    ChatService.getChat().emitCallStop(roomId);
                }
                stop();
                break;

            case R.id.mBtnAccept:
                SoundManage.stop(this);
                pulsatorLayout.stop();
                pulsatorLayout.setVisibility(View.GONE);
                if (isAudioCall) {
                    mImgAvatarCallAudio.setVisibility(View.VISIBLE);
                    mRemoteVideoView.setVisibility(View.GONE);
                    mLocalVideoView.setVisibility(View.GONE);
                    mBtnTurnOnSpeaker.setVisibility(View.VISIBLE);
                    mAudioManager.setSpeakerphoneOn(false);
                } else {
                    mBtnTurnOnSpeaker.setVisibility(View.GONE);
                    mImgAvatarCallAudio.setVisibility(View.GONE);
                    mRemoteVideoView.setVisibility(View.VISIBLE);
                    mLocalVideoView.setVisibility(View.VISIBLE);
                    mBtnSwitchCamera.setVisibility(View.VISIBLE);
                    mAudioManager.setSpeakerphoneOn(false);
                    mBtnTurnOffVideoCam.setVisibility(View.VISIBLE);
                }
                mtvStatus.setVisibility(View.GONE);
                mBtnAccept.clearAnimation();
                mBtnAccept.setVisibility(View.GONE);
                ChatService.getChat().emitCallAccept(roomId);
                break;

            case R.id.mBtnSwitchCamera:
                switchCamera();
                break;

            case R.id.mBtnTurnOnSpeaker:
                mBtnTurnOnSpeaker.setVisibility(View.GONE);
                mBtnTurnOffSpeaker.setVisibility(View.VISIBLE);
                mAudioManager.setSpeakerphoneOn(true);
                break;

            case R.id.mBtnTurnOffSpeaker:
                mBtnTurnOffSpeaker.setVisibility(View.GONE);
                mBtnTurnOnSpeaker.setVisibility(View.VISIBLE);
                mAudioManager.setSpeakerphoneOn(false);
                break;

            case R.id.mBtnTurnOnVideoCam:
                mBtnTurnOnVideoCam.setVisibility(View.GONE);
                mBtnTurnOffVideoCam.setVisibility(View.VISIBLE);
                mBtnSwitchCamera.setVisibility(View.VISIBLE);
                mLocalVideoView.setVisibility(View.VISIBLE);
                if (videoCapturerAndroid != null) {
                    videoCapturerAndroid.startCapture(1280, 720, 30);
                    ChatService.getChat()
                            .emitTurnOnCamera(roomId, true, Common.getUserLogin().getId());
                }
                break;

            case R.id.mBtnTurnOffVideoCam:
                mBtnTurnOffVideoCam.setVisibility(View.GONE);
                mBtnTurnOnVideoCam.setVisibility(View.VISIBLE);
                mBtnSwitchCamera.setVisibility(View.GONE);
                mLocalVideoView.setVisibility(View.GONE);
                if (videoCapturerAndroid != null) {
                    try {
                        videoCapturerAndroid.stopCapture();
                        ChatService.getChat()
                                .emitTurnOnCamera(roomId, false, Common.getUserLogin().getId());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onCallContent(JSONObject data) {
        super.onCallContent(data);
        try {
            Log.d("onCallContent", "Json Received :: " + data.toString());
            String type = data.getString("type");
            if (type.equalsIgnoreCase("offer")) {
                onOfferReceived(data);
            } else if (type.equalsIgnoreCase("answer")) {
                onAnswerReceived(data);
            } else if (type.equalsIgnoreCase("candidate")) {
                onIceCandidateReceived(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCallBusy(int status) {
        super.onCallBusy(status);
        //Intent qua CallBusyActivity
        CallBusyActivity_.intent(this).status(Common.CALL_BUSY).mUser(Common.getFriendFromRoom(mRoom)).start();
        stop();
    }

    @Override
    public void onCallAccept() {
        super.onCallAccept();
        pulsatorLayout.stop();
        pulsatorLayout.setVisibility(View.GONE);
        createPeerConnection();
        doCall();
        if (isAudioCall) {
            try {
                mRemoteVideoView.setVisibility(View.GONE);
                mLocalVideoView.setVisibility(View.GONE);
                mBtnTurnOnSpeaker.setVisibility(View.VISIBLE);
                mAudioManager.setSpeakerphoneOn(false);
                videoCapturerAndroid.stopCapture();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            mImgAvatarCallAudio.setVisibility(View.GONE);
            mRemoteVideoView.setVisibility(View.VISIBLE);
            mLocalVideoView.setVisibility(View.VISIBLE);
            mBtnSwitchCamera.setVisibility(View.VISIBLE);
            mAudioManager.setSpeakerphoneOn(true);
            mBtnTurnOffVideoCam.setVisibility(View.VISIBLE);
        }
        mtvStatus.setVisibility(View.GONE);
    }

    @Override
    public void onCallStop() {
        super.onCallStop();
        stop();
        SoundManage.stop(this);
    }

    @Override
    public void onTurnOnCamera(boolean isOn, int userId) {
        super.onTurnOnCamera(isOn, userId);
        if (isOn) {
            mRemoteVideoView.setVisibility(View.VISIBLE);
            mTvTurnOffVideoCam.setVisibility(View.GONE);
            mImgAvatarCallAudio.setVisibility(View.GONE);
            mBtnTurnOnVideoCam.setBackgroundResource(R.drawable.ic_turn_on_videocam_white);
            mBtnTurnOffVideoCam.setBackgroundResource(R.drawable.ic_turn_off_videocam_white);
            mBtnSwitchCamera.setBackgroundResource(R.drawable.ic_switch_camera_white);
        } else {
            User user = new UserDAO().getUser(userId);
            String userName = user.getFirstName() + " " + user.getLastName();
            mRemoteVideoView.setVisibility(View.GONE);
            mTvTurnOffVideoCam.setText(userName + " was turn off camera!");
            mTvTurnOffVideoCam.setVisibility(View.VISIBLE);
            mImgAvatarCallAudio.setVisibility(View.VISIBLE);
            mBtnTurnOnVideoCam.setBackgroundResource(R.drawable.ic_turn_on_videocam_black);
            mBtnTurnOffVideoCam.setBackgroundResource(R.drawable.ic_turn_off_videocam_black);
            mBtnSwitchCamera.setBackgroundResource(R.drawable.ic_switch_camera_black);
        }
    }

    public void onOfferReceived(final JSONObject data) {
        Log.d(TAG, "Received Offer");
        try {
            if (localPeer == null) {
                createPeerConnection();
            }
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"),
                    new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
            doAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onAnswerReceived(JSONObject data) {
        Log.d(TAG, "Received Answer");
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"),
                    new SessionDescription(SessionDescription.Type.fromCanonicalForm(
                            data.getString("type").toLowerCase()), data.getString("sdp")));
            createPeerConnection();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onIceCandidateReceived(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"),
                    data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"),
                        sessionDescription);
                JSONObject obj = new JSONObject();
                try {
                    obj.put("type", sessionDescription.type.canonicalForm());
                    obj.put("sdp", sessionDescription.description);
                    ChatService.getChat().emitCallContent(obj, roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                createPeerConnection();
            }
        }, sdpConstraints);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stop() {
        if (mediaStreamLocal != null) {
            mediaStreamLocal.removeTrack(localVideoTrack);
            mediaStreamLocal.removeTrack(localAudioTrack);
            mediaStreamLocal.dispose();
        }
        if (audioConstraints != null) {
            audioConstraints = null;
        }
        if (audioSource != null) {
            audioSource.dispose();
        }
        if (videoCapturerAndroid != null) {
            try {
                videoCapturerAndroid.stopCapture();
                videoCapturerAndroid.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (mLocalVideoView != null) {
            mLocalVideoView.release();
        }
        if (mRemoteVideoView != null) {
            mRemoteVideoView.release();
        }
        if (localPeer != null) {
            localPeer.close();
        }
        MainActivity.checkCall = false;
        finish();
    }
}
