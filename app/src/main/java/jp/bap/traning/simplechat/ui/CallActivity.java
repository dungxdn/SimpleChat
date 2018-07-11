package jp.bap.traning.simplechat.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
import org.webrtc.DataChannel;
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.RtpReceiver;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoRenderer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.webrtc.CustomPeerConnectionObserver;
import jp.bap.traning.simplechat.webrtc.CustomSdpObserver;

@EActivity(R.layout.activity_call_video)
public class CallActivity extends BaseActivity {

    private static final String TAG = "CallActivity";
    @ViewById
    SurfaceViewRenderer localSurfaceView;
    @ViewById
    SurfaceViewRenderer remoteSurfaceView;
    @ViewById
    AppCompatTextView tvStatus;
    @ViewById
    AppCompatImageButton btnAccept;
    @ViewById
    AppCompatImageButton btnCancel;
    @Extra
    int roomId;
    @Extra
    boolean isIncoming;

    private PeerConnectionFactory peerConnectionFactory;
    private MediaConstraints audioConstraints;
    private MediaConstraints videoConstraints;
    private MediaConstraints sdpConstraints;
    private VideoSource videoSource;
    private VideoTrack localVideoTrack;
    private AudioSource audioSource;
    private AudioTrack localAudioTrack;
    private VideoCapturer videoCapturerAndroid;

    private VideoRenderer localRenderer;
    private VideoRenderer remoteRenderer;
    private EglBase rootEglBase;

    private PeerConnection localPeer, remotePeer;
    private List<PeerConnection.IceServer> iceServers = new ArrayList<>();

    @Override
    public void afterView() {
        initVideos();
        start();
        getIceServers();
        if (isIncoming) {
            tvStatus.setVisibility(View.VISIBLE);
            tvStatus.setText("Incoming call from " + Common.getFullRoomFromRoomId(roomId).getRoomName());
            btnAccept.setVisibility(View.VISIBLE);
        } else {
            ChatService.getChat().emitCall(roomId);
            tvStatus.setText("Calling to " + Common.getFullRoomFromRoomId(roomId).getRoomName());
            btnAccept.setVisibility(View.GONE);
        }
    }

    private void getIceServers() {
        PeerConnection.IceServer peerIceServer = PeerConnection.IceServer
                .builder(Common.TURN_URL)
                .createIceServer();
        iceServers.add(peerIceServer);
    }

    private void start() {
        //Initialize PeerConnectionFactory globals.
        PeerConnectionFactory.InitializationOptions initializationOptions =
                PeerConnectionFactory.InitializationOptions.builder(this)
                        .setEnableVideoHwAcceleration(true)
                        .createInitializationOptions();
        PeerConnectionFactory.initialize(initializationOptions);

        //Create a new PeerConnectionFactory instance - using Hardware encoder and decoder.
        PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(
                rootEglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
        peerConnectionFactory = new PeerConnectionFactory(options, defaultVideoEncoderFactory, defaultVideoDecoderFactory);

        //Now create a VideoCapturer instance. Callback methods are there if you want to do something! Duh!
        videoCapturerAndroid = createVideoCapturer();
        //Create MediaConstraints - Will be useful for specifying video and audio constraints. More on this later!
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
        localSurfaceView.setVisibility(View.VISIBLE);


        //create a videoRenderer based on SurfaceViewRenderer instance
        localRenderer = new VideoRenderer(localSurfaceView);
        localVideoTrack.addRenderer(localRenderer);

        localSurfaceView.setMirror(true);
        remoteSurfaceView.setMirror(true);
    }

    @Click(R.id.btnAccept)
    public void btnAccept() {
        tvStatus.setText("Waitting...");
        ChatService.getChat().emitCallAccept(roomId);
        btnAccept.setVisibility(View.GONE);
    }

    @Click(R.id.btnCancel)
    public void btnCancel() {
        ChatService.getChat().emitCallStop(roomId);
        stop();
    }

    private void initVideos() {
        rootEglBase = EglBase.create();
        localSurfaceView.init(rootEglBase.getEglBaseContext(), null);
        remoteSurfaceView.init(rootEglBase.getEglBaseContext(), null);
        localSurfaceView.setZOrderMediaOverlay(true);
//        remoteSurfaceView.setZOrderMediaOverlay(true);
    }

    private VideoCapturer createVideoCapturer() {
        VideoCapturer videoCapturer;
        videoCapturer = createCameraCapturer(new Camera1Enumerator(false));
        return videoCapturer;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // Trying to find a front facing camera!
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // We were not able to find a front cam. Look for other cameras
        for (String deviceName : deviceNames) {
            if (!enumerator.isFrontFacing(deviceName)) {
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);
                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        return null;
    }

    private void createPeerConnection() {
        PeerConnection.RTCConfiguration rtcConfig =
                new PeerConnection.RTCConfiguration(iceServers);
        // TCP candidates are only useful when connecting to a server that supports
        // ICE-TCP.
        rtcConfig.tcpCandidatePolicy = PeerConnection.TcpCandidatePolicy.DISABLED;
        rtcConfig.bundlePolicy = PeerConnection.BundlePolicy.MAXBUNDLE;
        rtcConfig.rtcpMuxPolicy = PeerConnection.RtcpMuxPolicy.REQUIRE;
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;


        //create sdpConstraints
        sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"));

        //creating localPeer
        localPeer = peerConnectionFactory.createPeerConnection(rtcConfig, new CustomPeerConnectionObserver("localPeerCreation") {
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
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onAddStream(MediaStream mediaStream) {
                super.onAddStream(mediaStream);
                Log.d(TAG, "onAddStream: ");
                gotRemoteStream(mediaStream);
            }
        });

        addStreamToLocalPeer();
    }

    private void addStreamToLocalPeer() {
        //creating local mediastream
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("102");
        stream.addTrack(localAudioTrack);
        stream.addTrack(localVideoTrack);
        localPeer.addStream(stream);
    }

    private void gotRemoteStream(MediaStream stream) {
        //we have remote video stream. add to the renderer.
        final VideoTrack videoTrack = stream.videoTracks.get(0);
        runOnUiThread(() -> {
            try {
                remoteRenderer = new VideoRenderer(remoteSurfaceView);
                remoteSurfaceView.setVisibility(View.VISIBLE);
                videoTrack.addRenderer(remoteRenderer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }


    @Override
    public void onCallAccept() {
        super.onCallAccept();
        createPeerConnection();
        createOffer();
    }


    @Override
    public void onCallContent(JSONObject data) {
        super.onCallContent(data);
        try {
            String type = data.getString("type");
            if (type.equalsIgnoreCase("offer")) {
                onReceiveOffer(data);
            } else if (type.equalsIgnoreCase("answer")) {
                onReceiveAnswer(data);
            } else if (type.equalsIgnoreCase("candidate")) {
                onReceiveCandidate(data);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCallStop() {
        super.onCallStop();
        stop();
    }

    private void createOffer() {
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetDescription"), sessionDescription);
                JSONObject data = new JSONObject();
                try {
                    data.put("type", sessionDescription.type.canonicalForm());
                    data.put("sdp", sessionDescription.description);
                    ChatService.getChat().emitCallContent(data, roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, sdpConstraints);
    }


    private void onReceiveOffer(JSONObject data) {
        try {
            if (localPeer == null) {
                createPeerConnection();
            }
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"),
                    new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
            createAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void createAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDescription"), sessionDescription);
                JSONObject data = new JSONObject();
                try {
                    data.put("type", sessionDescription.type.ANSWER);
                    data.put("sdp", sessionDescription.description);
                    ChatService.getChat().emitCallContent(data, roomId);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                createPeerConnection();
            }

            @Override
            public void onCreateFailure(String s) {
                super.onCreateFailure(s);
            }

        }, new MediaConstraints());

    }

    private void onReceiveAnswer(JSONObject data) {
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemoteDescription"),
                    new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            createPeerConnection();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void onReceiveCandidate(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void stop() {
        if (videoCapturerAndroid != null) {
            videoCapturerAndroid.dispose();
        }
        if (localSurfaceView != null) {
            localSurfaceView.release();
        }
        if (remoteSurfaceView != null) {
            remoteSurfaceView.release();
        }
        finish();
    }


}
