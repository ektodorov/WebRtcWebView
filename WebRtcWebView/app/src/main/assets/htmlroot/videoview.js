
window.onload = function() {
    console.log("window.onload");
    startCamera()
};

function init() {
    console.log("Now initializing.");
//    const startButton = document.getElementById("startButton");
//    startButton.addEventListener("click", startCamera());
//
//    const stopButton = document.getElementById("stopButton");
//    startButton.addEventListener("click", stopCamera());
}

function startCamera() {
    console.log("Start camera click");
    displayCamera(true);
}

function stopCamera() {
    console.log("Stop camera click");
    const localVideo = document.getElementById('localVideo');
    const stream = localVideo.srcObject;
    stream.getTracks().forEach(function(track) {
      track.stop();
    });
}

async function displayCamera(firstTime) {
    console.log('Requesting local stream');
    const localVideo = document.getElementById('localVideo');
    let localStream;
    try {
        // Capture local video & audio stream & set to local <video> DOM
        // element
        const stream = await navigator.mediaDevices.getUserMedia({
            audio: true,
            video: true
        });
        console.log('Received local stream');
        localVideo.srcObject = stream;
        localStream = stream;
        logVideoAudioTrackInfo(localStream);
    } catch (e) {
        alert(`getUserMedia() error: ${e.name}`);
        throw e;
    }
    console.log('Start complete');
}

function logVideoAudioTrackInfo(localStream) {
    const videoTracks = localStream.getVideoTracks();
    const audioTracks = localStream.getAudioTracks();
    if (videoTracks.length > 0) {
        console.log(`Using video device: ${videoTracks[0].label}`);
    }
    if (audioTracks.length > 0) {
        console.log(`Using audio device: ${audioTracks[0].label}`);
    }
}