JavaScript
var peerConnection;

/*
 * Setup 'leaveButton' button function.
 */
const leaveButton = document.getElementById('leaveButton');
leaveButton.addEventListener('click', leave);

function leave() {
    console.log('Ending call');
    peerConnection.close();
    signalingWebsocket.close();
    window.location.href = './index.html';
};

/*
 * Prepare websocket for signaling server endpoint.
 */
var signalingWebsocket = new WebSocket("ws://" + window.location.host +
    "/video-conf-tutorial/signal");

signalingWebsocket.onmessage = function(msg) {
    console.log("Got message", msg.data);
    var signal = JSON.parse(msg.data);
    switch (signal.type) {
        case "offer":
            handleOffer(signal);
            break;
        case "answer":
            handleAnswer(signal);
            break;
        // In local network, ICE candidates might not be generated.
        case "candidate":
            handleCandidate(signal);
            break;
        default:
            break;
    }
};

signalingWebsocket.onopen = init();

function sendSignal(signal) {
    if (signalingWebsocket.readyState == 1) {
        signalingWebsocket.send(JSON.stringify(signal));
    }
};

/*
 * Initialize
 */
function init() {
    console.log("Connected to signaling endpoint. Now initializing.");
    preparePeerConnection();
    displayLocalStreamAndSignal(true);

};

/*
 * Prepare RTCPeerConnection & setup event handlers.
 */
function preparePeerConnection() {

     // Using free public google STUN server.
    const configuration = {
        iceServers: [{
            urls: 'stun:stun.l.google.com:19302'
        }]
    };

    // Prepare peer connection object
    peerConnection = new RTCPeerConnection(configuration);
    peerConnection.onnegotiationneeded = async () => {
        console.log('onnegotiationneeded');
        sendOfferSignal();
    };
    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
        	sendSignal(event);
        }
    };

    /*
	 * Track other participant's remote stream & display in UI when available.
	 *
	 * This is how other participant's video & audio will start showing up on my
	 * browser as soon as his local stream added to track of peer connection in
	 * his UI.
	 */
    peerConnection.addEventListener('track', displayRemoteStream);

};

/*
 * Display my local webcam & audio on UI.
 */
async function displayLocalStreamAndSignal(firstTime) {
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

        // For first time, add local stream to peer connection.
        if (firstTime) {
            setTimeout(
                function() {
                    addLocalStreamToPeerConnection(localStream);
                }, 2000);
        }

        // Send offer signal to signaling server endpoint.
        sendOfferSignal();

    } catch (e) {
        alert(`getUserMedia() error: ${e.name}`);
        throw e;
    }
    console.log('Start complete');
};

/*
 * Add local webcam & audio stream to peer connection so that other
 * participant's UI will be notified using 'track' event.
 *
 * This is how my video & audio is sent to other participant's UI.
 */
async function addLocalStreamToPeerConnection(localStream) {
    console.log('Starting addLocalStreamToPeerConnection');
    localStream.getTracks().forEach(track => peerConnection.addTrack(track, localStream));
    console.log('localStream tracks added');
};

/*
 * Display remote webcam & audio in UI.
 */
function displayRemoteStream(e) {
    console.log('displayRemoteStream');
    const remoteVideo = document.getElementById('remoteVideo');
    if (remoteVideo.srcObject !== e.streams[0]) {
        remoteVideo.srcObject = e.streams[0];
        console.log('pc2 received remote stream');
    }
};

/*
 * Send offer to signaling server. This is kind of telling server that my webcam &
 * audio is ready, so notify other participant of my information so that he can
 * view & hear me using 'track' event.
 */
function sendOfferSignal() {
    peerConnection.createOffer(function(offer) {
        sendSignal(offer);
        peerConnection.setLocalDescription(offer);
    }, function(error) {
        alert("Error creating an offer");
    });
};

/*
 * Handle the offer sent by other participant & send back answer to complete the
 * handshake.
 */
function handleOffer(offer) {
    peerConnection
        .setRemoteDescription(new RTCSessionDescription(offer));

    // create and send an answer to an offer
    peerConnection.createAnswer(function(answer) {
        peerConnection.setLocalDescription(answer);
        sendSignal(answer);
    }, function(error) {
        alert("Error creating an answer");
    });

};

/*
 * Finish the handshake by receiving the answer. Now Peer-to-peer connection is
 * established between my browser & other participant's browser. Since both
 * participants are tracking each others stream, they both will be able to view &
 * hear each other.
 */
function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(
        answer));
    console.log("connection established successfully!!");
};

/*
 * Add received ICE candidate to connection. ICE candidate has information about
 * how to connect to remote participant's browser. In local LAN connection, ICE
 * candidate might not be generated.
 */
function handleCandidate(candidate) {
	alert("handleCandidate");
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};

/*
 * Logs names of your webcam & microphone to console just for FYI.
 */
function logVideoAudioTrackInfo(localStream) {
    const videoTracks = localStream.getVideoTracks();
    const audioTracks = localStream.getAudioTracks();
    if (videoTracks.length > 0) {
        console.log(`Using video device: ${videoTracks[0].label}`);
    }
    if (audioTracks.length > 0) {
        console.log(`Using audio device: ${audioTracks[0].label}`);
    }
};
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
59
60
61
62
63
64
65
66
67
68
69
70
71
72
73
74
75
76
77
78
79
80
81
82
83
84
85
86
87
88
89
90
91
92
93
94
95
96
97
98
99
100
101
102
103
104
105
106
107
108
109
110
111
112
113
114
115
116
117
118
119
120
121
122
123
124
125
126
127
128
129
130
131
132
133
134
135
136
137
138
139
140
141
142
143
144
145
146
147
148
149
150
151
152
153
154
155
156
157
158
159
160
161
162
163
164
165
166
167
168
169
170
171
172
173
174
175
176
177
178
179
180
181
182
183
184
185
186
187
188
189
190
191
192
193
194
195
196
197
198
199
200
201
202
203
204
205
206
207
208
209
210
211
212
213
214
215
216
217
218
219
220
221
var peerConnection;

/*
 * Setup 'leaveButton' button function.
 */
const leaveButton = document.getElementById('leaveButton');
leaveButton.addEventListener('click', leave);

function leave() {
    console.log('Ending call');
    peerConnection.close();
    signalingWebsocket.close();
    window.location.href = './index.html';
};

/*
 * Prepare websocket for signaling server endpoint.
 */
var signalingWebsocket = new WebSocket("ws://" + window.location.host +
    "/video-conf-tutorial/signal");

signalingWebsocket.onmessage = function(msg) {
    console.log("Got message", msg.data);
    var signal = JSON.parse(msg.data);
    switch (signal.type) {
        case "offer":
            handleOffer(signal);
            break;
        case "answer":
            handleAnswer(signal);
            break;
        // In local network, ICE candidates might not be generated.
        case "candidate":
            handleCandidate(signal);
            break;
        default:
            break;
    }
};

signalingWebsocket.onopen = init();

function sendSignal(signal) {
    if (signalingWebsocket.readyState == 1) {
        signalingWebsocket.send(JSON.stringify(signal));
    }
};

/*
 * Initialize
 */
function init() {
    console.log("Connected to signaling endpoint. Now initializing.");
    preparePeerConnection();
    displayLocalStreamAndSignal(true);

};

/*
 * Prepare RTCPeerConnection & setup event handlers.
 */
function preparePeerConnection() {

     // Using free public google STUN server.
    const configuration = {
        iceServers: [{
            urls: 'stun:stun.l.google.com:19302'
        }]
    };

    // Prepare peer connection object
    peerConnection = new RTCPeerConnection(configuration);
    peerConnection.onnegotiationneeded = async () => {
        console.log('onnegotiationneeded');
        sendOfferSignal();
    };
    peerConnection.onicecandidate = function(event) {
        if (event.candidate) {
        	sendSignal(event);
        }
    };

    /*
	 * Track other participant's remote stream & display in UI when available.
	 *
	 * This is how other participant's video & audio will start showing up on my
	 * browser as soon as his local stream added to track of peer connection in
	 * his UI.
	 */
    peerConnection.addEventListener('track', displayRemoteStream);

};

/*
 * Display my local webcam & audio on UI.
 */
async function displayLocalStreamAndSignal(firstTime) {
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

        // For first time, add local stream to peer connection.
        if (firstTime) {
            setTimeout(
                function() {
                    addLocalStreamToPeerConnection(localStream);
                }, 2000);
        }

        // Send offer signal to signaling server endpoint.
        sendOfferSignal();

    } catch (e) {
        alert(`getUserMedia() error: ${e.name}`);
        throw e;
    }
    console.log('Start complete');
};

/*
 * Add local webcam & audio stream to peer connection so that other
 * participant's UI will be notified using 'track' event.
 *
 * This is how my video & audio is sent to other participant's UI.
 */
async function addLocalStreamToPeerConnection(localStream) {
    console.log('Starting addLocalStreamToPeerConnection');
    localStream.getTracks().forEach(track => peerConnection.addTrack(track, localStream));
    console.log('localStream tracks added');
};

/*
 * Display remote webcam & audio in UI.
 */
function displayRemoteStream(e) {
    console.log('displayRemoteStream');
    const remoteVideo = document.getElementById('remoteVideo');
    if (remoteVideo.srcObject !== e.streams[0]) {
        remoteVideo.srcObject = e.streams[0];
        console.log('pc2 received remote stream');
    }
};

/*
 * Send offer to signaling server. This is kind of telling server that my webcam &
 * audio is ready, so notify other participant of my information so that he can
 * view & hear me using 'track' event.
 */
function sendOfferSignal() {
    peerConnection.createOffer(function(offer) {
        sendSignal(offer);
        peerConnection.setLocalDescription(offer);
    }, function(error) {
        alert("Error creating an offer");
    });
};

/*
 * Handle the offer sent by other participant & send back answer to complete the
 * handshake.
 */
function handleOffer(offer) {
    peerConnection
        .setRemoteDescription(new RTCSessionDescription(offer));

    // create and send an answer to an offer
    peerConnection.createAnswer(function(answer) {
        peerConnection.setLocalDescription(answer);
        sendSignal(answer);
    }, function(error) {
        alert("Error creating an answer");
    });

};

/*
 * Finish the handshake by receiving the answer. Now Peer-to-peer connection is
 * established between my browser & other participant's browser. Since both
 * participants are tracking each others stream, they both will be able to view &
 * hear each other.
 */
function handleAnswer(answer) {
    peerConnection.setRemoteDescription(new RTCSessionDescription(
        answer));
    console.log("connection established successfully!!");
};

/*
 * Add received ICE candidate to connection. ICE candidate has information about
 * how to connect to remote participant's browser. In local LAN connection, ICE
 * candidate might not be generated.
 */
function handleCandidate(candidate) {
	alert("handleCandidate");
    peerConnection.addIceCandidate(new RTCIceCandidate(candidate));
};

/*
 * Logs names of your webcam & microphone to console just for FYI.
 */
function logVideoAudioTrackInfo(localStream) {
    const videoTracks = localStream.getVideoTracks();
    const audioTracks = localStream.getAudioTracks();
    if (videoTracks.length > 0) {
        console.log(`Using video device: ${videoTracks[0].label}`);
    }
    if (audioTracks.length > 0) {
        console.log(`Using audio device: ${audioTracks[0].label}`);
    }
};