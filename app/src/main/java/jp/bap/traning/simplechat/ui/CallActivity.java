package jp.bap.traning.simplechat.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.EglBase;
import org.webrtc.IceCandidate;
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
import java.util.ArrayList;
import java.util.List;
import jp.bap.traning.simplechat.service.ChatService;
import jp.bap.traning.simplechat.utils.Common;
import jp.bap.traning.simplechat.utils.CustomPeerConnectionObserver;
import jp.bap.traning.simplechat.utils.CustomSdpObserver;
import jp.bap.traning.simplechat.R;
import jp.bap.traning.simplechat.utils.RequestPermission;


@EActivity(R.layout.activity_call)
public class CallActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    private static boolean permissionCamera =false;
    private static boolean permissionAudio =false;
    @ViewById
    SurfaceViewRenderer mRemoteVideoView;
    @ViewById
    SurfaceViewRenderer mLocalVideoView;
    @ViewById
    AppCompatTextView mTvStatus;
    @ViewById
    AppCompatButton mButtonAccept;
    @ViewById
    AppCompatButton mButtonStop;
    @Extra
    int roomId;
    @Extra
    boolean isIncoming;

    private RequestPermission requestPermission;
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

    @Override
    public void afterView() {
        initPermission();
        checkPermissionToStart();
    }

    private void initPermission() {
        requestPermission = new RequestPermission(CallActivity.this);
        if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            requestPermission.requestCameraPermission();
        }
        else {
            permissionCamera=true;
        }
        if (ContextCompat.checkSelfPermission(CallActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            requestPermission.requestRecordAudioPermission();
        }
        else {
            permissionAudio=true;
        }
    }

    private void checkPermissionToStart() {
        if(permissionCamera==true && permissionAudio==true) {
            mTvStatus.setText("Calling to room "+roomId);
            initVideos();
            getIceServers();
            start();
            if (isIncoming) {
                mButtonAccept.setVisibility(View.VISIBLE);
                mTvStatus.setText("Incoming call from: " + roomId);
            } else {
                ChatService.getChat().emitCall(roomId);
                mButtonAccept.setVisibility(View.GONE);
                mTvStatus.setText("Calling to " + roomId);
            }
        }
        else {
        }
    }

    private void initVideos() {
        //create an EglBase instance: EglBase is a interface class
        rootEglBase = EglBase.create();
        //init the SurfaceViewRenderer using the eglContext
        mLocalVideoView.init(rootEglBase.getEglBaseContext(), null);
        mRemoteVideoView.init(rootEglBase.getEglBaseContext(), null);
        //setZOrderMediaOverlay (boolean isMediaOverlay)
        //Kiểm soát xem bề mặt của bề mặt xem có được đặt trên đầu của một chế độ xem bề mặt thông
        // thường khác trong cửa sổ (nhưng vẫn phía sau cửa sổ chính nó).
        mLocalVideoView.setZOrderMediaOverlay(true);
    }

    //sử dụng ICE để đối phó với NAT và tường lửa
    // Google chạy một máy chủ STUN công khai, stun.l.google.com:19302, như được sử dụng bởi apprtc.appspot.com
    private void getIceServers() {
        PeerConnection.IceServer peerIceServer = PeerConnection.IceServer
                .builder(Common.TURN_URL)
                .createIceServer();
        peerIceServers.add(peerIceServer);
    }

    //    public void start()-- The steps to display video stream from camera to view are:
//    Create and initialize PeerConnectionFactory
//    Create a VideoCapturer instance which uses the camera of the device
//    Create a VideoSource from the Capturer
//    Create a VideoTrack from the source
//    Create a video renderer using a SurfaceViewRenderer view and add it to the VideoTrack instance
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
                rootEglBase.getEglBaseContext(),  /* enableIntelVp8Encoder */true,  /* enableH264HighProfile */true);
        DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(rootEglBase.getEglBaseContext());
        peerConnectionFactory = new PeerConnectionFactory(options, defaultVideoEncoderFactory, defaultVideoDecoderFactory);


        //Now create a VideoCapturer instance.
        videoCapturerAndroid = createCameraCapturer(new Camera1Enumerator(false));

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
        mLocalVideoView.setVisibility(View.VISIBLE);
        //create a videoRenderer based on SurfaceViewRenderer instance
        localRenderer = new VideoRenderer(mLocalVideoView);
        // And finally, with our VideoRenderer ready, we
        // can add our renderer to the VideoTrack.
        localVideoTrack.addRenderer(localRenderer);

        //a small method to provide a mirror effect to the SurfaceViewRenderer
        mLocalVideoView.setMirror(true);
        mRemoteVideoView.setMirror(true);

        gotUserMedia = true;
    }

    private VideoCapturer createCameraCapturer(CameraEnumerator enumerator) {
        final String[] deviceNames = enumerator.getDeviceNames();

        // First, try to find front facing camera
        Log.d(TAG, "Looking for front facing cameras.");
        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                Log.d(TAG, "Creating front facing camera capturer.");
                VideoCapturer videoCapturer = enumerator.createCapturer(deviceName, null);

                if (videoCapturer != null) {
                    return videoCapturer;
                }
            }
        }

        // If We were not able to find a front camera, Look for other cameras
        for (String deviceName : deviceNames) {
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
        rtcConfig.continualGatheringPolicy = PeerConnection.ContinualGatheringPolicy.GATHER_CONTINUALLY;
        // Use ECDSA encryption.
        rtcConfig.keyType = PeerConnection.KeyType.ECDSA;

        //Create Session Description Protocol contrains
        sdpConstraints = new MediaConstraints();
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveAudio", "true"));
        sdpConstraints.mandatory.add(new MediaConstraints.KeyValuePair("OfferToReceiveVideo", "true"));

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
                Log.d(TAG, "Received Remote stream");
                super.onAddStream(mediaStream);
                gotRemoteStream(mediaStream);
            }
        });
//        Creating local media stream
        addStreamToLocalPeer();
    }

    /**
     * Adding the stream to the localpeer
     */
    private void addStreamToLocalPeer() {
        //creating local mediastream
        MediaStream stream = peerConnectionFactory.createLocalMediaStream("102");
        stream.addTrack(localAudioTrack);
        stream.addTrack(localVideoTrack);
        localPeer.addStream(stream);
    }

    /**
     * This method is called when the app is initiator - We generate the offer and send it over through socket
     * to remote peer
     */
    private void doCall() {
        localPeer.createOffer(new CustomSdpObserver("localCreateOffer") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocalDesc"), sessionDescription);
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

    @Click({R.id.mButtonAccept, R.id.mButtonStop})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.mButtonStop:
                mTvStatus.setText("Call ended!!!");
                ChatService.getChat().emitCallStop(roomId);
                stop();
                break;

            case R.id.mButtonAccept:
                mButtonAccept.setVisibility(View.GONE);
                mTvStatus.setText("Call started!!!");
                ChatService.getChat().emitCallAccept(roomId);
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
    public void onCallAccept() {
        mButtonAccept.setVisibility(View.GONE);
        mTvStatus.setText("Call Started!!!");
        super.onCallAccept();
        createPeerConnection();
        doCall();
    }

    @Override
    public void onCallStop() {
        super.onCallStop();
        stop();
    }

    public void onOfferReceived(final JSONObject data) {
        Log.d(TAG, "Received Offer");
        try {
            if (localPeer == null) {
                createPeerConnection();
            }
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.OFFER, data.getString("sdp")));
            doAnswer();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onAnswerReceived(JSONObject data) {
        Log.d(TAG, "Received Answer");
        try {
            localPeer.setRemoteDescription(new CustomSdpObserver("localSetRemote"), new SessionDescription(SessionDescription.Type.fromCanonicalForm(data.getString("type").toLowerCase()), data.getString("sdp")));
            createPeerConnection();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onIceCandidateReceived(JSONObject data) {
        try {
            localPeer.addIceCandidate(new IceCandidate(data.getString("id"), data.getInt("label"), data.getString("candidate")));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doAnswer() {
        localPeer.createAnswer(new CustomSdpObserver("localCreateAns") {
            @Override
            public void onCreateSuccess(SessionDescription sessionDescription) {
                super.onCreateSuccess(sessionDescription);
                localPeer.setLocalDescription(new CustomSdpObserver("localSetLocal"), sessionDescription);
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
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void stop() {
        if (videoCapturerAndroid != null) {
            videoCapturerAndroid.dispose();
        }
        if (mLocalVideoView != null) {
            mLocalVideoView.release();
        }
        if (mRemoteVideoView != null) {
            mRemoteVideoView.release();
        }
        finish();
    }

        @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case Common.MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionCamera=true;
                } else {
                    mTvStatus.setText("You don't accept Camera Permission");
                }
                break;
            }
            case Common.MY_PERMISSIONS_REQUEST_AUDIO: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionAudio=true;
                } else {
                    mTvStatus.setText("You don't accept Audio Permission");
                }
                break;
            }
        }
        checkPermissionToStart();
    }

}
